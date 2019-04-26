package in.prepskool.prepskoolacademy.model;

import java.util.ArrayList;

public class SectionHome {

    private String sectionLabel;
    private ArrayList<Home> itemArrayList;

    public SectionHome(String sectionLabel, ArrayList<Home> itemArrayList) {
        this.sectionLabel = sectionLabel;
        this.itemArrayList = itemArrayList;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public ArrayList<Home> getItemArrayList() {
        return itemArrayList;
    }
}
