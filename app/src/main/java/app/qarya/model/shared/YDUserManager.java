package app.qarya.model.shared;

import android.content.Context;
import android.content.SharedPreferences;


import com.facebook.login.LoginManager;
import com.google.gson.JsonSyntaxException;

import app.qarya.MyApplication;
import app.qarya.model.models.User;
import app.qarya.model.models.responses.AuthResponse;
import app.qarya.model.models.responses.ConfigsResponse;
import app.qarya.presentation.base.MyActivity;

import java.util.Arrays;
import java.util.List;

import timber.log.Timber;
import tn.core.util.Utilities;

/**
 * Created by Yassine on 5/10/2018.
 */

public class YDUserManager {
    public static final String PREFERENCES_NAME = "yassine";

    public final static String KEY_EMAIL_HINT = "KEY_EMAIL_HINT";
    public final static String KEY_AUTH= "KEY_AUTH";
    public final static String KEY_CONFIG = "KEY_CONFIG";
    public final static String KEY_SHOWCASES = "KEY_SHOWCASES";
    public final static String KEY_TOPICS = "KEY_TOPICS";
    
    public final static String API_KEY = "Y_API_KEY";
    public final static String LAST_CONFIG_KEY = "Y_API_KEY";
    public final static String BACKGROUND_KEY = "Y_BACKGROUND_KEY";

    public final static String COUNTRY_KEY = "Y_COUNTRY";
    public final static String CATEGORY_KEY = "Y_CATEGORY";
    public final static String LAST_NOTIF_TIME_KEY = "Y_LAST_NOTIF_TIME";
    public final static String KEY_ADS_AFTER = "KEY_ADS_AFTER";



    public static AuthResponse authResponse;
    public static Showcases showcases;


    public static boolean isSubscribedTo(String topic) {
        List<String> list = getTopics();
        return list.contains(topic);
    }
    public static void removeTopic(String value) {
        List<String> list = getTopics();
        if(list.contains(value)){
            list.remove(value);
            saveTopics(list);
        }else
            MyActivity.log(value+" topic doesn't exist: ", YDUserManager.class.getName());
    }
    public static void addTopic(String value) {
        List<String> list = getTopics();
        if(!list.contains(value)){
            list.add(value);
            saveTopics(list);
        }else
            MyActivity.log(value+" topic already exist: ", YDUserManager.class.getName());
    }

    public static void saveTopics(List<String> list) {
        StringBuilder bldr = new StringBuilder();
        for(String item : list){
            bldr.append(item);
            bldr.append(",");
        }

        String data = bldr.toString();
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_TOPICS, data)
                .apply();
    }
    public static List<String> getTopics() {
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String data = sp.getString(KEY_TOPICS, "");
        return Arrays.asList(data.split(","));
    }







    public static void save(Showcases showcases) {
        YDUserManager.showcases = showcases;
        YDUserManager.save(YDUserManager.KEY_SHOWCASES,    Utilities.getGsonParser().toJson(showcases));
    }
    public static Showcases showcases(){
        if (YDUserManager.showcases != null) return YDUserManager.showcases;
        String json = get(YDUserManager.KEY_SHOWCASES);
        if (json==null || json.isEmpty() || "null".equals(json)) return new Showcases();
        MyActivity.log("=>>>>>>>>> ☺☺☻• "+json);
        try {
            Showcases showcases = Utilities.getGsonParser().fromJson(json, Showcases.class);
            Timber.d("Returned active user from memory: %s", showcases.toString());
            YDUserManager.showcases = showcases;
            return showcases;
        }catch (JsonSyntaxException e){
            return new Showcases();
        }
    }






    public static void setBackground(String path) {
        ConfigsResponse c = configs();
        if(c==null){
            c = new ConfigsResponse();
        }
        c.setBackground(path);
        save(c);
    }
    public static void save(ConfigsResponse configs) {
        YDUserManager.save(YDUserManager.KEY_CONFIG,    Utilities.getGsonParser().toJson(configs));
    }
    public static ConfigsResponse configs(){
        String jsonTxt = get(YDUserManager.KEY_CONFIG);
        if (jsonTxt==null || jsonTxt.isEmpty() || "null".equals(jsonTxt)) return null;
        //MyActivity.log("=>>>>>>>>> ☺☺☻• "+jsonTxt);
        try {
            ConfigsResponse data = Utilities.getGsonParser().fromJson(jsonTxt, ConfigsResponse.class);
            //Timber.d("Returned configs from memory: %s", data.toString());
            return data;
        }catch (JsonSyntaxException e){
            return null;
        }
    }


    public static void save(AuthResponse authResponse) {
        YDUserManager.authResponse = authResponse;
        YDUserManager.save(YDUserManager.KEY_AUTH,    Utilities.getGsonParser().toJson(authResponse));
    }
    public static AuthResponse saveFromUser(User user) {
        authResponse = auth();
        if (user!=null){
            authResponse.setName(user.getName());
            authResponse.setPhoto(user.getPhoto());
            YDUserManager.save(YDUserManager.KEY_AUTH,    Utilities.getGsonParser().toJson(authResponse));
        }
        return authResponse;
    }
    public static AuthResponse auth(){
        if (authResponse!=null) return authResponse;
        String auth = get(YDUserManager.KEY_AUTH);
        if (auth==null || auth.isEmpty() || "null".equals(auth)) return null;
        //MyActivity.log("=>>>>>>>>> ☺☺☻• "+auth);
        try {
            authResponse = Utilities.getGsonParser().fromJson(auth, AuthResponse.class);
            //Timber.d("Returned active user from memory: %s", authResponse.toString());
            return authResponse;
        }catch (JsonSyntaxException e){
            return null;
        }
    }

    public static Context getContext(){
        return MyApplication.get();
    }

    public static void setUserEmailHint(String emailHint){
        save(KEY_EMAIL_HINT, emailHint);
    }
    public static String getUserEmailHint(){
        return get(KEY_EMAIL_HINT);
    }

    public static void save(String key, String value) {
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit().putString(key, value)
                .apply();
    }
    public static void saveLong(String key, long value) {
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value)
                .apply();
    }
    public static void saveInt(String key, int value) {
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value)
                .apply();
    }

    public static String get(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        return sp.getString(key, null);
    }

    public static long getLong(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }
    public static int getInt(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }
    public static int getAdsAfter() {
        int a = getInt(KEY_ADS_AFTER);
        return a==0 ? 9999999 : a ;
    }



    public static boolean check() {
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getString(KEY_AUTH, null) == null ? false : true ;
    }

    public static void logout() {
        YDUserManager.authResponse = null;
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_AUTH, null)
                .putString(COUNTRY_KEY, null)
                .putString(CATEGORY_KEY, null)
                .putString(LAST_NOTIF_TIME_KEY, null)
                .apply();
        LoginManager.getInstance().logOut();
    }










    public static void saveBool(String key, boolean value) {
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit().putBoolean(key, value)
                .apply();
    }

    public static boolean getBool(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        return sp.getBoolean(key, false);
    }
}
