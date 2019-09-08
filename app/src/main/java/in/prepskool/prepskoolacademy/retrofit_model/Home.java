package in.prepskool.prepskoolacademy.retrofit_model;

import in.prepskool.prepskoolacademy.R;

public class Home {
    private int id;
    private String name;
    private int iconId;

    public Home(NcertData ncertData) {
        this.id = ncertData.getId();
        this.name = ncertData.getName();
        this.iconId = R.mipmap.ic_launcher;
    }

    public Home(PracticePaperData practicePaperData) {
        this.id = practicePaperData.getId();
        this.name = practicePaperData.getName();
        this.iconId = R.mipmap.ic_launcher;
    }

    public Home(BoardData boardData) {
        this.id = boardData.getId();
        this.name = boardData.getName();
        this.iconId = R.mipmap.ic_launcher;
    }

    public Home(int id, String name, int iconId) {
        this.id = id;
        this.name = name;
        this.iconId = iconId;
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

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
