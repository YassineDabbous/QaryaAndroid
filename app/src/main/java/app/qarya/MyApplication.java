package app.qarya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import io.fabric.sdk.android.services.common.SafeToast;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;


import app.qarya.model.shared.YDUserManager;
import app.qarya.model.models.responses.AuthResponse;
import app.qarya.presentation.base.MyActivity;
import app.qarya.model.MyRetrofit;
import app.qarya.model.RestAPI;
import app.qarya.presentation.services.PushHandler;
import app.qarya.utils.MyConst;

import java.io.IOException;
import java.net.URISyntaxException;

import tn.core.util.LocaleManager;


/**
 * Created by X on 1/19/2018.
 */

public class MyApplication extends MultiDexApplication {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        MyActivity.log("attachBaseContext");
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
        MyActivity.log( "onConfigurationChanged: " + newConfig.locale.getLanguage());
    }
    

    public void toast(String s){
        Toast toast = SafeToast.makeText(this, s, SafeToast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Timber.plant(new Timber.DebugTree());

        //BaseAdapter.ADS_ENABLED = YDUserManager.configs()!= null ? YDUserManager.configs().getActive(): false;
        MobileAds.initialize(this, getResources().getString(R.string.admob_initialize));

        initPushNotifs();

        // ssl for android <=4
        MyRetrofit.installGooglePlayServicesProvider(this);

        if(YDUserManager.auth()!=null) Crashlytics.setUserIdentifier("user "+YDUserManager.auth().getId());


        restAPI = myRetrofit.getApi(this);

/*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            Configuration configuration = getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("ar"));
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }*/
    }

    void initPushNotifs(){
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new PushHandler(this))
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }


    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = get().getResources().getDisplayMetrics(); // null exception
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }


    public RestAPI getRestAPI(){
        return restAPI;
    }
    public OkHttpClient getHttpClient(){
        return myRetrofit.client;
    }

    MyRetrofit myRetrofit = new MyRetrofit();
    RestAPI restAPI;
    public static MyApplication get(Context activity){ return (MyApplication) activity.getApplicationContext(); }
    public static MyApplication get(Activity activity){ return (MyApplication) activity.getApplication(); }


    private static MyApplication mInstance;
    public static synchronized MyApplication get() {
        return mInstance;
    }
    public static void goTo(Class<?> to) {
        Intent intent = new Intent(mInstance, to).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        mInstance.startActivity(intent);
    }



    ////////////////////////////////////////////////////
    class CustomHeaderInterceptor implements Interceptor{
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            AuthResponse ar = YDUserManager.auth();
            String tkn = ar!=null ? ar.getToken() : "";
            String key = "WXpKV2FtTnRWakJZTW5Sc1pWRmZNalV0TURNdE1qQXhPUT09";
            Request.Builder builder = originalRequest.newBuilder().
                    addHeader("api-key", key==null? "":key).
                    addHeader("api-token", tkn==null? "":tkn);

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    };
    public HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Timber.e(message);
                //MyActivity.log("Interceptor => "+message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private Socket mSocket;
    {
        try {
            IO.Options options = new IO.Options();
            options.port = 1215;
            options.transports = new String[]{WebSocket.NAME, Polling.NAME};
            //options.query = "api_token=zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";

            final OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new CustomHeaderInterceptor())
                    .addInterceptor(httpLoggingInterceptor())
                    .build();
            options.webSocketFactory = httpClient;
            options.callFactory = httpClient;



            Log.i("Qarya Socket", "Connecting ...");
            mSocket = IO.socket(MyConst.SOCKET, options);
            //mSocket = IO.socket(Constants.CHAT_SERVER_URL);


/*
            Emitter.Listener onSocketTransport = args -> {

                if(args!=null && args.length > 0){
                    Transport transport = (Transport) args[0];
                    // Adding headers when EVENT_REQUEST_HEADERS is called
                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
                            // modify request headers
                            headers.put("Cookie", Arrays.asList("foo=1;"));
                            headers.put("api_token", Arrays.asList("api_tokenapi_tokenapi_tokenapi_tokenapi_token"));
                        }
                    });

                    transport.on(Transport.EVENT_ERROR, args1 -> {
                        Exception e = (Exception) args1[0];
                        Log.d("Watan", "AppBackgroundService onSocketTransport event error " + e);
                        e.printStackTrace();
                        e.getCause().printStackTrace();
                    });

                    transport.on(Transport.EVENT_RESPONSE_HEADERS, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            @SuppressWarnings("unchecked")
                            Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
                            // access response headers
                            String cookie = headers.get("Set-Cookie").get(0);
                        }
                    });
                } else {
                    Log.e("Watan", "args nulloooooooooooooooooooooooooo");
                    Log.e("Watan", "args nulloooooooooooooooooooooooooo");
                    Log.e("Watan", "args nulloooooooooooooooooooooooooo");
                }
            };
            mSocket.on(Manager.EVENT_TRANSPORT, onSocketTransport);
*/
        } catch (URISyntaxException e) {
            Log.e("WATAN", "index=" + e);
            Log.e("WATAN", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public Socket getSocket() {
        return mSocket;
    }
}