package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResourceResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("resources")
    private ArrayList<Resource> resourceList;

    public String getStatus() {
        return status;
    }

    public ArrayList<Resource> getResourceList() {
        return resourceList;
    }
}
