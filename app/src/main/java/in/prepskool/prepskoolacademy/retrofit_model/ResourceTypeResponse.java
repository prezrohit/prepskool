package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResourceTypeResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("resourceTypes")
    private ArrayList<ResourceType> resourceTypeList;

    public String getStatus() {
        return status;
    }

    public ArrayList<ResourceType> getResourceTypeList() {
        return resourceTypeList;
    }
}
