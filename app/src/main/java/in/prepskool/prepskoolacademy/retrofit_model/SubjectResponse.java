package in.prepskool.prepskoolacademy.retrofit_model;

import java.util.ArrayList;

public class SubjectResponse {
    private String status;
    private ArrayList<Subject> subjectList;

    public String getStatus() {
        return status;
    }

    public ArrayList<Subject> getSubjectList() {
        return subjectList;
    }
}
