package in.prepskool.prepskoolacademy.app;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreferences {

    private SharedPreferences sharedPreferences;

    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_PHONE = "phone";
    private static String KEY_HAS_SKIPPED = "has_skipped";
    private static String KEY_FIREBASE_TOKEN = "firebase_token";

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

    public void setFirebaseToken(String firebaseToken) {
        editor.putString(KEY_FIREBASE_TOKEN, firebaseToken);
        editor.apply();
    }

    public String getFirebaseToken() {
        return sharedPreferences.getString(KEY_FIREBASE_TOKEN, null);
    }

    public void setHasSkipped(boolean hasSkipped) {
        editor.putBoolean(KEY_HAS_SKIPPED, hasSkipped);
        editor.apply();
    }

    public boolean hasSkipped() {
        return sharedPreferences.getBoolean(KEY_HAS_SKIPPED, false);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

}
