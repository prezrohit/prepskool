package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

public class NotificationToken {

    @SerializedName("old_token")
    private String oldToken;

    @SerializedName("token")
    private String token;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("type")
    private String type;
}
