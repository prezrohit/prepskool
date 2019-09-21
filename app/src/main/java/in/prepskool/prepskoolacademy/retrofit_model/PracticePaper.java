package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PracticePaper {
    @SerializedName("id")
    private int id;

    @SerializedName("label")
    private String label;

    @SerializedName("data")
    private ArrayList<PracticePaperData> practicePaperDataList;

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<PracticePaperData> getPracticePaperDataList() {
        return practicePaperDataList;
    }
}
