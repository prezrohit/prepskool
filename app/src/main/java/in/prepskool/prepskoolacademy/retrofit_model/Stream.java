package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Stream {
    private int id;
    private String name;
    private String displayName;
    private String icon;

    @SerializedName("subjects")
    private ArrayList<Subject> subjectsList;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public ArrayList<Subject> getSubjectsList() {
        return subjectsList;
    }
}
