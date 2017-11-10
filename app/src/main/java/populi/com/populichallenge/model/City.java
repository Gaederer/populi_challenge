package populi.com.populichallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class City implements Parcelable {

    public String id;
    public String name;
    public List<Tour> tours;

    protected City(Parcel in) {
        id = in.readString();
        name = in.readString();
        tours = in.createTypedArrayList(Tour.CREATOR);
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeTypedList(tours);
    }
}
