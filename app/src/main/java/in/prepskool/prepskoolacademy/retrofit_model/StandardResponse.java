package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StandardResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("standards")
    private ArrayList<Standard> standardsList;

    public String getStatus() {
        return status;
    }

    public ArrayList<Standard> getStandardsList() {
        return standardsList;
    }
}
