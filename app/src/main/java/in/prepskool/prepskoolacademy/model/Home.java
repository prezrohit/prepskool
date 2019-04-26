package in.prepskool.prepskoolacademy.model;

public class Home {

    private String label;
    private int imgId;

    public Home(String label, int imgId) {
        this.label = label;
        this.imgId = imgId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
