package in.prepskool.prepskoolacademy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StudyMaterial implements Parcelable {

    private String name;
    private int imgId;

    public StudyMaterial(String name, int imgId) {
        this.name = name;
        this.imgId = imgId;
    }

    protected StudyMaterial(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public int getImgId() {
        return imgId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudyMaterial> CREATOR = new Creator<StudyMaterial>() {
        @Override
        public StudyMaterial createFromParcel(Parcel in) {
            return new StudyMaterial(in);
        }

        @Override
        public StudyMaterial[] newArray(int size) {
            return new StudyMaterial[size];
        }
    };
}
