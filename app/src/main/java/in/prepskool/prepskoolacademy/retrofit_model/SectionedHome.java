package in.prepskool.prepskoolacademy.retrofit_model;

import android.util.Log;

import java.util.ArrayList;

public class SectionedHome {
    private int sectionId;
    private String sectionLabel;
    private ArrayList<Home> homeDataList;

    private static final String TAG = "SectionedHome";

    public SectionedHome() {
        sectionLabel = "";
        homeDataList = new ArrayList<>();
    }

    public SectionedHome(int sectionId, String sectionLabel, ArrayList<Home> homeDataList) {
        this.sectionId = sectionId;
        this.sectionLabel = sectionLabel;
        this.homeDataList = homeDataList;
    }

    public int getSectionId() {
        return sectionId;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public ArrayList<Home> getHomeDataList() {
        return homeDataList;
    }


    public void generateListByNcert(int sectionId, String label, ArrayList<NcertData> ncertList) {
        this.sectionId = sectionId;
        this.sectionLabel = label;
        for (NcertData ncertData : ncertList) {
            this.homeDataList.add(new Home(ncertData));
        }
    }

    public void generateListByPracticePaper(int sectionId, String label, ArrayList<PracticePaperData> practicePapersList) {
        this.sectionId = sectionId;
        this.sectionLabel = label;
        for (PracticePaperData practicePaperData : practicePapersList) {
            homeDataList.add(new Home(practicePaperData));
        }
    }

    public void generateListByBoard(int sectionId, String label, ArrayList<BoardData> boardsList) {
        this.sectionId = sectionId;
        this.sectionLabel = label;
        for (BoardData boardData : boardsList) {
            homeDataList.add(new Home(boardData));
        }
    }
}
