package TOPSECRET.controller;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;

/**
 * Controller responsible for handling the registration of a new country in the system.
 * <p>
 * Corresponds to User Story US007: As an Administrator, I want to register
 * a new country in the System.
 * </p>
 */
public class US007ToRegisterCountryController {

    /** Repository used to persist and manage {@link Country} instances. */
    private final ICountryRepository _countryRepository;

    /**
     * Constructs a new {@code US007ToRegisterCountryController} with the given repository.
     *
     * @param countryRepository the repository used to store countries;
     *                          must not be {@code null}
     */
    public US007ToRegisterCountryController(ICountryRepository countryRepository) {
        this._countryRepository = countryRepository;
    }

    /**
     * Registers a new country in the system.
     * <p>
     * Called by the UI with the name of the country to be registered.
     * Validates that the name is not {@code null} or blank before
     * delegating to the repository.
     * </p>
     *
     * @param _countryName the name of the country to register;
     *                     must not be {@code null} or blank
     * @return the newly registered {@link Country}
     * @throws IllegalArgumentException if {@code _countryName} is {@code null},
     *                                  blank, invalid, or already registered
     */
    public Country registerNewCountry(String _countryName) {
        if (_countryName == null || _countryName.isBlank()) {
            throw new IllegalArgumentException("Country name cannot be null or blank");
        }
        return _countryRepository.createAndAdd(_countryName);
    }
}