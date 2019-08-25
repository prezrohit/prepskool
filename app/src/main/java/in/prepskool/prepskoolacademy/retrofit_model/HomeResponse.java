package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("boards")
    private ArrayList<Board> boardsList;

    @SerializedName("practice_papers")
    private ArrayList<PracticePaper> practicePapersList;

    @SerializedName("ncert")
    private ArrayList<Ncert> ncertTypeList;

    public String getStatus() {
        return status;
    }

    public ArrayList<Board> getBoardsList() {
        return boardsList;
    }

    public ArrayList<PracticePaper> getPracticePapersList() {
        return practicePapersList;
    }

    public ArrayList<Ncert> getNcertTypeList() {
        return ncertTypeList;
    }
}
