package TOPSECRET.domain;

public class Country {

    private String _countryName;

    //Construtor
    public Country(String _countryName) {

        //Country name is null
        if (_countryName == null) {
            throw new IllegalArgumentException("Country name is null");
        }

        //Check if country is real (exists in the "Countries" ENUM)
        try {
            TOPSECRET.domain.Enum.Countries.valueOf(_countryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Country name is not valid");
        }

        //Country is valid (is in ENUM "Countries" and isn't null)
        this._countryName = _countryName;
    }

    //Empty Constructor - for testing purposes
    public Country() {
    }

    //Getter for countryName
    public String getCountryName() {
        return _countryName;
    }

}
