package in.prepskool.prepskoolacademy.retrofit_model;

import java.util.ArrayList;

public class ResourceResponse {
    private String status;
    private ArrayList<Resource> resourceList;

    public String getStatus() {
        return status;
    }

    public ArrayList<Resource> getResourceList() {
        return resourceList;
    }
}
