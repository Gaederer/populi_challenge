package populi.com.populichallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Country implements Parcelable {

    public String id;
    public String name;
    public List<City> cities;

    protected Country(Parcel in) {
        id = in.readString();
        name = in.readString();
        cities = in.createTypedArrayList(City.CREATOR);
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
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
        dest.writeTypedList(cities);
    }
}
