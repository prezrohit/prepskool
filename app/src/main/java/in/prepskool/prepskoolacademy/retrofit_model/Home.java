package in.prepskool.prepskoolacademy.retrofit_model;

import in.prepskool.prepskoolacademy.R;

public class Home {
    private int id;
    private String name;
    private int iconId;
    private String displayName;

    private static final String TAG = "Home";

    public Home(NcertData ncertData) {
        this.id = ncertData.getId();
        this.name = ncertData.getName();
        this.iconId = R.mipmap.ic_launcher;
        this.displayName = ncertData.getDisplayName();
    }

    public Home(PracticePaperData practicePaperData) {
        this.id = practicePaperData.getId();
        this.name = practicePaperData.getName();
        this.iconId = R.mipmap.ic_launcher;
        this.displayName = practicePaperData.getDisplayName();
    }

    public Home(BoardData boardData) {
        this.id = boardData.getId();
        this.name = boardData.getName();
        this.iconId = R.mipmap.ic_launcher;
        this.displayName = boardData.getDisplayName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
