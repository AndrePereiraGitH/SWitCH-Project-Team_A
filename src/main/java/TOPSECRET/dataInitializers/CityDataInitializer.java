package TOPSECRET.dataInitializers;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer that populates the database with sample City
 * entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Cities are built via {@link CityFactory}
 * to respect domain invariants, and persisted through
 * {@link ICityRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
public class CityDataInitializer implements ApplicationRunner {

    private static final Logger log =
            LoggerFactory.getLogger(CityDataInitializer.class);

    private final ICityRepository cityRepository;
    private final CityFactory cityFactory;

    public CityDataInitializer(
            ICityRepository cityRepository,
            CityFactory cityFactory) {

        this.cityRepository = cityRepository;
        this.cityFactory = cityFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) {

        log.info("[CityDataInitializer] Seeding cities...");

        cityRepository.save(buildPorto());

        cityRepository.save(buildLisbon());

        cityRepository.save(buildLondon());

        log.info("[CityDataInitializer] Done — {} cities seeded.", 3);
    }

    // =========================================================================
    // Sample Cities
    // =========================================================================

    /**
     * Portuguese city example.
     */
    private City buildPorto() {

        return cityFactory.create(
                new CountryId("PT"),
                "Porto"
        );
    }

    /**
     * Portuguese capital example.
     */
    private City buildLisbon() {

        return cityFactory.create(
                new CountryId("PT"),
                "Lisbon"
        );
    }

    /**
     * United Kingdom city example.
     */
    private City buildLondon() {

        return cityFactory.create(
                new CountryId("UK"),
                "London"
        );
    }
}