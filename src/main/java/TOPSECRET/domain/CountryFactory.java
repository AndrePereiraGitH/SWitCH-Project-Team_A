package TOPSECRET.domain;

public class CountryFactory {
    public Country create(String countryName) {
            return new Country(countryName);
        }
}
