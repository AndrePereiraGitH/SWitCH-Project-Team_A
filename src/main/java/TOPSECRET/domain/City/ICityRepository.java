package TOPSECRET.domain.City;

public interface ICityRepository {

    /**
     * Persists a {@link City} in the repository.
     *
     * @param city the city to persist; must not be {@code null}
     * @return the persisted {@link City}
     * @throws IllegalArgumentException if the city already exists or any constraint is violated
     */

        City save (City city);
}