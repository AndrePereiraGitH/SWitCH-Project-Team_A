package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(ApplicationArguments args) {
        log.info("[PublicationSampleDataInitializer] Seeding publication sample data...");

        publicationSampleRepository.save(buildNewBookSample());
        publicationSampleRepository.save(buildGoodConditionBookSample());
        publicationSampleRepository.save(buildWornMagazineSample());
        publicationSampleRepository.save(buildFairConditionBookSample());
        publicationSampleRepository.save(buildSecondLordOfTheRingsSample());

        log.info("[PublicationSampleDataInitializer] Done — {} publication samples seeded.", 5);
    }

    /**
     * A poor-condition copy of The Lord of the Rings, owned by user-001.
     * In library-001 and collection-001. Used by listing-001 (direct sale).
     */
    private PublicationSample buildNewBookSample() {
        return publicationSampleFactory.create(
                new PublicationSampleId("pub-sample-001"),
                new BibliographicalDataId("bd-001"),
                new UserId("user-001"),
                PublicationStatus.READ,
                new EditionId("edition-001"),
                Condition.POOR,
                new LibraryId("library-001"),
                new CollectionOfPublicationsId("collection-001"));
    }

    /**
     * A poor-condition copy of Dom Quixote, owned by user-002.
     * In library-002 and collection-002. Used by listing-002 and listing-004.
     */
    private PublicationSample buildGoodConditionBookSample() {
        return publicationSampleFactory.create(
                new PublicationSampleId("pub-sample-002"),
                new BibliographicalDataId("bd-002"),
                new UserId("user-002"),
                PublicationStatus.READ,
                new EditionId("edition-002"),
                Condition.POOR,
                new LibraryId("library-002"),
                new CollectionOfPublicationsId("collection-002"));
    }

    /**
     * A poor-condition copy of Os Lusíadas, owned by user-002.
     * In library-002, no collection. Used by listing-002.
     */
    private PublicationSample buildWornMagazineSample() {
        return publicationSampleFactory.create(
                new PublicationSampleId("pub-sample-003"),
                new BibliographicalDataId("bd-003"),
                new UserId("user-002"),
                PublicationStatus.READ,
                new EditionId("edition-003"),
                Condition.POOR,
                new LibraryId("library-002"),
                null);
    }

    /**
     * A poor-condition copy of Foundation, owned by user-003.
     * In library-003 and collection-003. Used by listing-003.
     */
    private PublicationSample buildFairConditionBookSample() {
        return publicationSampleFactory.create(
                new PublicationSampleId("pub-sample-004"),
                new BibliographicalDataId("bd-004"),
                new UserId("user-003"),
                PublicationStatus.READ,
                new EditionId("edition-001"),
                Condition.POOR,
                new LibraryId("library-003"),
                new CollectionOfPublicationsId("collection-003"));
    }

    /**
     * A second copy of The Lord of the Rings, owned by user-001.
     * In library-001, no collection. Used by listing-005 (auction).
     */
    private PublicationSample buildSecondLordOfTheRingsSample() {
        return publicationSampleFactory.create(
                new PublicationSampleId("pub-sample-005"),
                new BibliographicalDataId("bd-001"),
                new UserId("user-001"),
                PublicationStatus.READ,
                new EditionId("edition-001"),
                Condition.VERY_GOOD,
                new LibraryId("library-001"),
                null);
    }
}