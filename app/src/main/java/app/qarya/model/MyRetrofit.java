package app.qarya.model;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.qarya.BuildConfig;
import app.qarya.model.models.responses.AuthResponse;
import app.qarya.model.shared.YDUserManager;
import app.qarya.utils.MyConst;

import tn.core.model.net.custom.CacheableInterceptor;
import tn.core.model.net.custom.OfflineCacheInterceptor;
import tn.core.model.net.custom.OnlineCacheInterceptor;
import tn.core.model.net.net.NetworkUtils;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MyRetrofit {


    RestAPI restAPI;
    public OkHttpClient client;
    Context ctx;
    public RestAPI getApi(Context context){
        ctx = context;
        client = okHttpClient(cache(file(context)), httpLoggingInterceptor());
        if (restAPI==null) restAPI = restAPI(retrofit(client, gsonConverterFactory(gson())));
        return restAPI;
    }

    public RestAPI restAPI(Retrofit retrofit){
        return retrofit.create(RestAPI.class);
    }

    public Retrofit retrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(MyConst.BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }




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

    public OkHttpClient okHttpClient(Cache cache, HttpLoggingInterceptor httpLoggingInterceptor ){
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                AuthResponse ar = YDUserManager.auth();
                String tkn = ar!=null ? ar.getToken() : "";
                String key = "WXpKV2FtTnRWakJZTW5Sc1pWRmZNalV0TURNdE1qQXhPUT09";
                Request.Builder builder = originalRequest.newBuilder().
                        addHeader("api-city", "1").
                        addHeader("api-key", key==null? "":key).
                        addHeader("api-token", tkn==null? "":tkn).
                        addHeader("Authorization", tkn==null? "Bearer ":"Bearer "+tkn);

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        };

        if(BuildConfig.DEBUG){
            // no cache.
            return new OkHttpClient()
                    .newBuilder()
                    .cache(cache)
                    .addInterceptor(interceptor)
                    .addInterceptor(httpLoggingInterceptor)
                    //.addNetworkInterceptor(new CacheableInterceptor())
                    //.addNetworkInterceptor(new OnlineCacheInterceptor())
                    //.addInterceptor(new OfflineCacheInterceptor(NetworkUtils.isOnline(ctx)))
                    //.connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                    .build();
        }
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(interceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(new CacheableInterceptor())
                .addNetworkInterceptor(new OnlineCacheInterceptor())
                .addInterceptor(new OfflineCacheInterceptor(NetworkUtils.isOnline(ctx)))
                //.connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                .build();
    }




    public Cache cache(File file){
        return new Cache(file, 10 * 1024  * 1024 ); //10 MB
    }

    public File file(Context context){
        File cacheFile = new File(context.getCacheDir(), "HttpCache");
        cacheFile.mkdirs();
        return cacheFile;
    }




    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }

    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }




    public static void installGooglePlayServicesProvider(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //Devices with Android 5.1+ should support TLS 1.x out of the box
            try {
                ProviderInstaller.installIfNeeded(context);
            } catch (GooglePlayServicesRepairableException e) {
                Log.e("ProviderInstaller", "Google Play Services is out of date!", e);
                GoogleApiAvailability.getInstance().showErrorNotification(context, e.getConnectionStatusCode());
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.e("ProviderInstaller", "Google Play Services is unavailable!", e);
            }
        }
    }
}
