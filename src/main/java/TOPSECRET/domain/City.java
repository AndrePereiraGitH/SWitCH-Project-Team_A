package TOPSECRET.domain;

import java.util.Objects;

public class City {

    private Country _country;

    private String _cityName;


    public City(Country country, String cityName) {
        if (cityName == null) {
            throw new IllegalArgumentException("A city name cannot be left blank");
        }

        if (country == null)
            throw new IllegalArgumentException("Country cannot be left blank");

        _country = country;
        _cityName = cityName;
    }

    public Country getCityCountry() {
        return _country;
    }

    public String getCityName() {
        return _cityName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof City city)) return false;
        return Objects.equals(_country, city._country) && Objects.equals(_cityName, city._cityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_country, _cityName);
    }
}
