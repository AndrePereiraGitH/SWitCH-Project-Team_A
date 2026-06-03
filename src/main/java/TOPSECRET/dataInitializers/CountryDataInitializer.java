package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CountryDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CountryDataInitializer.class);

    private final ICountryRepository countryRepository;
    private final CountryFactory countryFactory;

    public CountryDataInitializer(ICountryRepository countryRepository, CountryFactory countryFactory) {
        this.countryRepository = countryRepository;
        this.countryFactory = countryFactory;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("[CountryDataInitializer] Seeding countries...");
        countryRepository.save(buildPortugal());
        countryRepository.save(buildUnitedKingdom());
        countryRepository.save(buildUnitedStates());
        log.info("[CountryDataInitializer] Done — {} countries seeded.", 3);
    }

    private Country buildPortugal() {
        return countryFactory.create("PORTUGAL"); // Reverted to full name
    }

    private Country buildUnitedKingdom() {
        return countryFactory.create("UNITED_KINGDOM"); // Reverted to full name
    }

    private Country buildUnitedStates() {
        return countryFactory.create("UNITED_STATES"); // Reverted to full name
    }
}