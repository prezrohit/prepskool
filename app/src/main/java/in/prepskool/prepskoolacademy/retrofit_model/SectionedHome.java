package in.prepskool.prepskoolacademy.retrofit_model;

import java.util.ArrayList;

public class SectionedHome {
    private String sectionLabel;
    private ArrayList<Home> homeDataList;

    public SectionedHome() {
        sectionLabel = "";
        homeDataList = new ArrayList<>();
    }

    public SectionedHome(String sectionLabel, ArrayList<Home> homeDataList) {
        this.sectionLabel = sectionLabel;
        this.homeDataList = homeDataList;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public ArrayList<Home> getHomeDataList() {
        return homeDataList;
    }

    public void generateListByNcert(String label, ArrayList<NcertData> ncertList) {
        this.sectionLabel = label;
        for (NcertData ncertData : ncertList)
            this.homeDataList.add(new Home(ncertData));
    }

    public void generateListByPracticePaper(String label, ArrayList<PracticePaperData> practicePapersList) {
        this.sectionLabel = label;
        for (PracticePaperData practicePaperData : practicePapersList)
            homeDataList.add(new Home(practicePaperData));
    }

    public void generateListByBoard(String label, ArrayList<BoardData> boardsList) {
        this.sectionLabel = label;
        for (BoardData boardData : boardsList)
            homeDataList.add(new Home(boardData));
    }
}
