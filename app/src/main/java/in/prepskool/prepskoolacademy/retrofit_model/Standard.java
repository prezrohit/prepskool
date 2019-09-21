package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

public class Standard {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("icon")
    private String icon;

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
}
