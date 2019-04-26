package in.prepskool.prepskoolacademy.model;

import java.util.ArrayList;

public class SectionModel {

    private String sectionLabel;
    private ArrayList<Pdf> itemArrayList;

    public SectionModel(String sectionLabel, ArrayList<Pdf> itemArrayList) {
        this.sectionLabel = sectionLabel;
        this.itemArrayList = itemArrayList;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public ArrayList<Pdf> getItemArrayList() {
        return itemArrayList;
    }
}
