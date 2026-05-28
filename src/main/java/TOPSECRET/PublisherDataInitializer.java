package TOPSECRET;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.ValueObjects.Address;
import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer that populates the database with sample {@link Publisher} entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Publishers are built via {@link PublisherFactory} to respect
 * domain invariants, and persisted through {@link IPublisherRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
public class PublisherDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PublisherDataInitializer.class);

    private final IPublisherRepository publisherRepository;
    private final PublisherFactory publisherFactory;

    public PublisherDataInitializer(IPublisherRepository publisherRepository,
                                    PublisherFactory publisherFactory) {
        this.publisherRepository = publisherRepository;
        this.publisherFactory = publisherFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) {
        log.info("[PublisherDataInitializer] Seeding publisher data...");

        publisherRepository.save(buildPenguinRandomHouse());
        publisherRepository.save(buildHarperCollins());
        publisherRepository.save(buildNationalGeographicSociety());

        log.info("[PublisherDataInitializer] Done — {} publishers seeded.", 3);
    }

    // =========================================================================
    // Sample Publishers
    // =========================================================================

    /**
     * Penguin Random House — a major book publisher.
     */
    private Publisher buildPenguinRandomHouse() {
        Email email = new Email("info", "penguinrandomhouse.com");
        Address address = new Address("1745 Broadway", "B1", "10019", new CityId("New York"));
        Contact contact = new Contact("2128407000", "1", email, address);

        return publisherFactory.create("Penguin Random House", "500123456", contact);
    }

    /**
     * HarperCollins — a major book publisher.
     */
    private Publisher buildHarperCollins() {
        Email email = new Email("contact", "harpercollins.com");
        Address address = new Address("195 Broadway", "A1", "10007", new CityId("New York"));
        Contact contact = new Contact("2126075000", "1", email, address);

        return publisherFactory.create("HarperCollins", "500234567", contact);
    }

    /**
     * National Geographic Society — publisher of the National Geographic magazine.
     */
    private Publisher buildNationalGeographicSociety() {
        Email email = new Email("askus", "nationalgeographic.com");
        Address address = new Address("1145 17th Street NW", "1", "20036", new CityId("Washington DC"));
        Contact contact = new Contact("2027754000", "1", email, address);

        return publisherFactory.create("National Geographic Society", "500345678", contact);
    }
}