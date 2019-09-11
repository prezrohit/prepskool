package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResourceList {
    @SerializedName("year")
    private String year;

    @SerializedName("resources")
    private ArrayList<Resource> resourceList;

    public String getYear() {
        return year;
    }

    public ArrayList<Resource> getResourceList() {
        return resourceList;
    }
}
