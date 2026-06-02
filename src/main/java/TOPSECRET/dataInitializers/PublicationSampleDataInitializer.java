package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Data initializer that populates the database with sample {@link PublicationSample} entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Publication samples are built via {@link PublicationSampleFactory} to respect
 * domain invariants, and persisted through {@link IPublicationSampleRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
@Order(3)
public class PublicationSampleDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PublicationSampleDataInitializer.class);

    private final IPublicationSampleRepository publicationSampleRepository;
    private final PublicationSampleFactory publicationSampleFactory;

    public PublicationSampleDataInitializer(IPublicationSampleRepository publicationSampleRepository,
                                            PublicationSampleFactory publicationSampleFactory) {
        this.publicationSampleRepository = publicationSampleRepository;
        this.publicationSampleFactory = publicationSampleFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) {
        log.info("[PublicationSampleDataInitializer] Seeding publication sample data...");

        publicationSampleRepository.save(buildNewBookSample());
        publicationSampleRepository.save(buildGoodConditionBookSample());
        publicationSampleRepository.save(buildWornMagazineSample());
        publicationSampleRepository.save(buildFairConditionBookSample());

        log.info("[PublicationSampleDataInitializer] Done — {} publication samples seeded.", 4);
    }

    // =========================================================================
    // Sample PublicationSamples
    // =========================================================================

    /**
     * A brand-new copy of a modern book, owned by user-001.
     * Referenced by ListingDataInitializer as pub-sample-001.
     */
    private PublicationSample buildNewBookSample() {
        PublicationSampleId id = new PublicationSampleId("pub-sample-001");
        BibliographicalDataId bdId = new BibliographicalDataId("bd-001");
        UserId ownerId = new UserId("user-001");
        EditionId editionId = new EditionId("edition-001");

        return publicationSampleFactory.create(
                id,
                bdId,
                ownerId,
                PublicationStatus.READ,
                editionId,
                Condition.POOR,
                null,
                null);
    }

    /**
     * A good-condition copy of a vintage book, owned by user-002.
     * Referenced by ListingDataInitializer as pub-sample-002.
     */
    private PublicationSample buildGoodConditionBookSample() {
        PublicationSampleId id = new PublicationSampleId("pub-sample-002");
        BibliographicalDataId bdId = new BibliographicalDataId("bd-002");
        UserId ownerId = new UserId("user-002");
        EditionId editionId = new EditionId("edition-002");

        return publicationSampleFactory.create(
                id,
                bdId,
                ownerId,
                PublicationStatus.READ,
                editionId,
                Condition.POOR,
                null,
                null);
    }

    /**
     * A worn copy of a magazine, owned by user-002.
     * Referenced by ListingDataInitializer as pub-sample-003.
     */
    private PublicationSample buildWornMagazineSample() {
        PublicationSampleId id = new PublicationSampleId("pub-sample-003");
        BibliographicalDataId bdId = new BibliographicalDataId("bd-003");
        UserId ownerId = new UserId("user-002");
        EditionId editionId = new EditionId("edition-003");

        return publicationSampleFactory.create(
                id,
                bdId,
                ownerId,
                PublicationStatus.READ,
                editionId,
                Condition.POOR,
                null,
                null);
    }

    /**
     * A fair-condition collector's edition book, owned by user-003.
     * Referenced by ListingDataInitializer as pub-sample-004.
     */
    private PublicationSample buildFairConditionBookSample() {
        PublicationSampleId id = new PublicationSampleId("pub-sample-004");
        BibliographicalDataId bdId = new BibliographicalDataId("bd-004");
        UserId ownerId = new UserId("user-003");
        EditionId editionId = new EditionId("edition-001");

        return publicationSampleFactory.create(
                id,
                bdId,
                ownerId,
                PublicationStatus.READ,
                editionId,
                Condition.POOR,
                null,
                null);
    }
}