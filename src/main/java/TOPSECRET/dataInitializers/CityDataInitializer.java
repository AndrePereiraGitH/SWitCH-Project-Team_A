package TOPSECRET.dataInitializers;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CityDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CityDataInitializer.class);

    private final ICityRepository cityRepository;
    private final CityFactory cityFactory;

    public CityDataInitializer(ICityRepository cityRepository, CityFactory cityFactory) {
        this.cityRepository = cityRepository;
        this.cityFactory = cityFactory;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("[CityDataInitializer] Seeding cities...");
        cityRepository.save(buildPorto());
        cityRepository.save(buildLisbon());
        cityRepository.save(buildLondon());
        cityRepository.save(buildNewYork());
        cityRepository.save(buildWashingtonDC());
        cityRepository.save(buildLosAngeles());
        cityRepository.save(buildChicago());
        log.info("[CityDataInitializer] Done — {} cities seeded.", 7);
    }

    private City buildPorto() {
        return cityFactory.create(new CityId("Porto"), new CountryId("PORTUGAL"), "Porto");
    }

    private City buildLisbon() {
        return cityFactory.create(new CityId("Lisbon"), new CountryId("PORTUGAL"), "Lisbon");
    }

    private City buildLondon() {
        return cityFactory.create(new CityId("London"), new CountryId("UNITED_KINGDOM"), "London");
    }

    private City buildNewYork() {
        return cityFactory.create(new CityId("New York"), new CountryId("UNITED_STATES"), "New York");
    }

    private City buildWashingtonDC() {
        return cityFactory.create(new CityId("Washington DC"), new CountryId("UNITED_STATES"), "Washington D.C.");
    }

    private City buildLosAngeles() {
        return cityFactory.create(new CityId("Los Angeles"), new CountryId("UNITED_STATES"), "Los Angeles");
    }

    private City buildChicago() {
        return cityFactory.create(new CityId("Chicago"), new CountryId("UNITED_STATES"), "Chicago");
    }
}