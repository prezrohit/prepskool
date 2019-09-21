package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Ncert {
    @SerializedName("id")
    private int id;

    @SerializedName("label")
    private String label;

    @SerializedName("data")
    private ArrayList<NcertData> ncertDataList;

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<NcertData> getNcertDataList() {
        return ncertDataList;
    }
}
