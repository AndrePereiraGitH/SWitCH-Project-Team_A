package TOPSECRET.repository;

import TOPSECRET.domain.City;
import TOPSECRET.domain.Country;

import java.util.ArrayList;
import java.util.List;

public class CityRepository {
    private List<City> _cities;

    public CityRepository() {
        _cities = new ArrayList<City>();
    }

    public City createAndAddNewCity(Country _country, String _cityName) {
        // New city is created (TODO inquire regarding if it should be boolean here and change methods that call upon this one)
        City newCity = new City(_country, _cityName);

        // Making sure City doesn't already exist*
        if (!existsByName(newCity)) {
            _cities.add(newCity); // A new City is added if existsByName returns false
            return newCity;
        }
        throw new IllegalArgumentException("This city already exists in the system");
    }


    //*The method which is called upon above written here. Again should it be "isExistent"? or some other name?
    //this is private because it's an auxiliary method used only in this class. No other class should have to call upon it

    private boolean existsByName(City city) {
        for (City newCity : this._cities) {
            if (city.equals(newCity)) {
                return true;
            }
        }
        return false;
    }

}

// STANDARDIZE METHODS when data persistence becomes relevant.


