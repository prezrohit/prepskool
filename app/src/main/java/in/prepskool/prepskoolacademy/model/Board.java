package in.prepskool.prepskoolacademy.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Board extends ExpandableGroup<StudyMaterial> {

    private String title;
    private int imgId;
    private List<StudyMaterial> childrenList;

    public Board(String title, int imgId, List<StudyMaterial> childrenList) {
        super(title, childrenList);

        this.title = title;
        this.imgId = imgId;
        this.childrenList = childrenList;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public List<StudyMaterial> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<StudyMaterial> childrenList) {
        this.childrenList = childrenList;
    }
}
