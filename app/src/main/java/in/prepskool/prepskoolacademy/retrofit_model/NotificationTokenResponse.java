package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

public class NotificationTokenResponse {
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }
}
