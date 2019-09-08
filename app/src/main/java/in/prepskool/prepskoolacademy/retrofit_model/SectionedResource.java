package in.prepskool.prepskoolacademy.retrofit_model;

import java.util.ArrayList;

public class SectionedResource {
    private String sectionLabel;
    private ArrayList<Resource> resourceList;

    public SectionedResource(String sectionLabel, ArrayList<Resource> resourceList) {
        this.sectionLabel = sectionLabel;
        this.resourceList = resourceList;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public ArrayList<Resource> getResourceList() {
        return resourceList;
    }
}
