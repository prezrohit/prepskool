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

    public NotificationToken(String oldToken, String token, String userId, String type) {
        this.oldToken = oldToken;
        this.token = token;
        this.userId = userId;
        this.type = type;
    }
}
