package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResourceResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("resources_list")
    private ArrayList<ResourceList> resourceList;

    public String getStatus() {
        return status;
    }

    public ArrayList<ResourceList> getResourceList() {
        return resourceList;
    }
}
