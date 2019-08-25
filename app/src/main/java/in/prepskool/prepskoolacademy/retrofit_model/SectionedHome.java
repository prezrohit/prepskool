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

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public ArrayList<Home> getHomeDataList() {
        return homeDataList;
    }

    public void setHomeDataList(ArrayList<Home> homeDataList) {
        this.homeDataList = homeDataList;
    }

    public void generateListByNcert(ArrayList<Ncert> ncertList) {
        this.sectionLabel = "NCERT";
        for (Ncert ncert: ncertList)
            this.homeDataList.add(new Home(ncert));
    }

    public void generateListByPracticePaper(ArrayList<PracticePaper> practicePapersList) {
        this.sectionLabel = "CBSE PRACTICE PAPERS";
        for (PracticePaper practicePaper: practicePapersList)
            homeDataList.add(new Home(practicePaper));
    }

    public void generateListByBoard(ArrayList<Board> boardsList) {
        this.sectionLabel = "SCHOOL BOARDS";
        for (Board board: boardsList)
            homeDataList.add(new Home(board));
    }
}
