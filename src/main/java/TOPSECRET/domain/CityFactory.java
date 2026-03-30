package TOPSECRET.domain;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CityID;

/**
 * Factory responsible for creating {@link City} domain objects.
 * <p>
 * This factory centralizes the instantiation logic of {@code City} to ensure that
 * all domain constraints are respected and that object creation is standardized
 * across the codebase.
 * <p>
 * The {@code CityFactory} is used by repositories and application services
 * (e.g., {@code CityRepository}, US008 controller) to avoid direct construction
 * of {@code City} instances and prevent duplicated validation logic.
 */

public class CityFactory {

    /**
     * Creates a new {@link City} instance with the given parameters.
     * <p>
     * This method delegates domain validation to the {@link City} constructor.
     * All invariants defined in the {@code City} domain class must be satisfied,
     * otherwise an exception is expected to be thrown by {@code City}.
     *
     * @param country the country to which the city belongs; must not be {@code null}
     * @param cityName the name of the city; must not be {@code null} or blank
     * @return a valid {@link City} instance
     * @throws IllegalArgumentException if any domain constraint is violated
     */

    public City create(Country country, String cityName) {

        return new City(country, cityName);
    }
}