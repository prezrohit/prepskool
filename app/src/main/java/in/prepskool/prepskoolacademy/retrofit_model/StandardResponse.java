package in.prepskool.prepskoolacademy.retrofit_model;

import java.util.ArrayList;

public class StandardResponse {
    private String status;
    private ArrayList<Standard> standardsList;

    public String getStatus() {
        return status;
    }

    public ArrayList<Standard> getStandardsList() {
        return standardsList;
    }
}
