package in.prepskool.prepskoolacademy.app;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreferences {

    private SharedPreferences sharedPreferences;
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_TOKEN = "token";
    private static String KEY_PHONE = "phone";

    private SharedPreferences.Editor editor;

    public AppSharedPreferences(Context context) {
        String PREF_NAME = "PrepskoolAcademy";
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setName(String name) {
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString(KEY_NAME, null);
    }

    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public void setPhone(String phone) {
        editor.putString(KEY_PHONE, phone);
        editor.apply();
    }

    public String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, null);
    }

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}
