package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubjectResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("subjects")
    private ArrayList<Subject> subjectList;

    public String getStatus() {
        return status;
    }

    public ArrayList<Subject> getSubjectList() {
        return subjectList;
    }
}
