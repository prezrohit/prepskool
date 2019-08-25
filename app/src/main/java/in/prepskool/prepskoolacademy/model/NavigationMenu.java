package in.prepskool.prepskoolacademy.model;

public class NavigationMenu {
    private Menu menuId;
    private String title;
    private int iconId;

    public NavigationMenu(Menu menuId, String title, int iconId) {
        this.menuId = menuId;
        this.title = title;
        this.iconId = iconId;
    }

    public NavigationMenu(String title, int iconId) {
        this.title = title;
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public Menu getMenuId() {
        return menuId;
    }

    public void setMenuId(Menu menuId) {
        this.menuId = menuId;
    }

    public enum Menu {
        NCERT_BOOK, SAVED_FILES, CBSE, ICSE, DELHI, ONLINE_TEST, RATE_APP, SHARE, ABOUT_US, PRIVACY, NCERT_NOTES, TOPPER_ANSWERS,
        CHAPTER_WISE_QUES, PAST_YEAR_PAPER, MARKING_SCHEME, SAMPLE_PAPER
    }
}
