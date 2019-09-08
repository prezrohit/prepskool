package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("ncert")
    private Ncert ncert;

    @SerializedName("practice_papers")
    private PracticePaper practicePaper;

    @SerializedName("boards")
    private Board board;

    public String getStatus() {
        return status;
    }

    public Ncert getNcert() {
        return ncert;
    }

    public PracticePaper getPracticePaper() {
        return practicePaper;
    }

    public Board getBoard() {
        return board;
    }
}
