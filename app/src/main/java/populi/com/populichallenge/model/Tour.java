package populi.com.populichallenge.model;

import android.os.Parcel;
import android.os.Parcelable;



public class Tour implements Parcelable{

    public String uid;
    public String title;
    public String image;
    public String desc;

    protected Tour(Parcel in) {
        uid = in.readString();
        title = in.readString();
        image = in.readString();
        desc = in.readString();
    }

    public static final Creator<Tour> CREATOR = new Creator<Tour>() {
        @Override
        public Tour createFromParcel(Parcel in) {
            return new Tour(in);
        }

        @Override
        public Tour[] newArray(int size) {
            return new Tour[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(desc);
    }
}
