package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StreamResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("streams")
    private ArrayList<Stream> streamList;

    public String getStatus() {
        return status;
    }

    public ArrayList<Stream> getStreamList() {
        return streamList;
    }
}
