package TOPSECRET.dataInitializers;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer that populates the database with sample
 * CollectionOfPublications entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Collections are built via
 * {@link CollectionOfPublicationsFactory}
 * to respect domain invariants, and persisted through
 * {@link ICollectionOfPublicationsRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
public class CollectionOfPublicationsDataInitializer implements ApplicationRunner {

    private static final Logger log =
            LoggerFactory.getLogger(CollectionOfPublicationsDataInitializer.class);

    private final ICollectionOfPublicationsRepository collectionRepository;
    private final CollectionOfPublicationsFactory collectionFactory;

    public CollectionOfPublicationsDataInitializer(
            ICollectionOfPublicationsRepository collectionRepository,
            CollectionOfPublicationsFactory collectionFactory) {

        this.collectionRepository = collectionRepository;
        this.collectionFactory = collectionFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) {

        log.info("[CollectionOfPublicationsDataInitializer] Seeding collections...");

        collectionRepository.save(buildFantasyCollection());

        collectionRepository.save(buildScienceCollection());

        collectionRepository.save(buildHistoricalCollection());

        log.info("[CollectionOfPublicationsDataInitializer] Done — {} collections seeded.", 3);
    }

    // =========================================================================
    // Sample Collections
    // =========================================================================

    /**
     * Fantasy publications collection.
     */
    private CollectionOfPublications buildFantasyCollection() {

        return collectionFactory.create(
                "Epic Fantasy Collection",
                new GenreId("FANTASY"),
                new UserId("user-001")
        );
    }

    /**
     * Science publications collection.
     */
    private CollectionOfPublications buildScienceCollection() {

        return collectionFactory.create(
                "Science Essentials",
                new GenreId("SCIENCE"),
                new UserId("user-002")
        );
    }

    /**
     * Historical publications collection.
     */
    private CollectionOfPublications buildHistoricalCollection() {

        return collectionFactory.create(
                "Historical Masterpieces",
                new GenreId("HISTORICAL"),
                new UserId("user-003")
        );
    }
}
