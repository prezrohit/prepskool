package in.prepskool.prepskoolacademy.retrofit_model;

import in.prepskool.prepskoolacademy.R;

public class Home {
    private String id;
    private String name;
    private int iconId;

    public Home(Ncert ncert) {
        this.id = ncert.getId();
        this.name = ncert.getName();
        this.iconId = R.mipmap.ic_launcher;
    }

    public Home(PracticePaper practicePaper) {
        this.id = practicePaper.getId();
        this.name = practicePaper.getName();
        this.iconId = R.mipmap.ic_launcher;
    }

    public Home(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.iconId = R.mipmap.ic_launcher;
    }

    public Home(String id, String name, int iconId) {
        this.id = id;
        this.name = name;
        this.iconId = iconId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
