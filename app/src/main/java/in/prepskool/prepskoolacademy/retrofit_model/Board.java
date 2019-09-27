package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Board {
    @SerializedName("label")
    private String label;

    @SerializedName("data")
    private ArrayList<BoardData> boardDataList;

    public String getLabel() {
        return label;
    }

    public ArrayList<BoardData> getBoardDataList() {
        return boardDataList;
    }
}
