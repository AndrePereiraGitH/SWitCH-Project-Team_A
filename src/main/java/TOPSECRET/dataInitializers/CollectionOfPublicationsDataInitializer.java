package TOPSECRET.dataInitializers;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
@Order(2)
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
     * Fantasy publications collection, owned by user-001.
     */
    private CollectionOfPublications buildFantasyCollection() {
        CollectionOfPublicationsId id = new CollectionOfPublicationsId("collection-001");

        return collectionFactory.create(
                id,
                "Epic Fantasy Collection",
                new GenreId("GENRE4"),   // Fantasy
                new UserId("user-001"),
                true,
                LocalDateTime.now()
        );
    }

    /**
     * Science publications collection, owned by user-002.
     */
    private CollectionOfPublications buildScienceCollection() {
        CollectionOfPublicationsId id = new CollectionOfPublicationsId("collection-002");

        return collectionFactory.create(
                id,
                "Science Essentials",
                new GenreId("GENRE2"),   // Science Fiction
                new UserId("user-002"),
                true,
                LocalDateTime.now()
        );
    }

    /**
     * Historical publications collection, owned by user-003.
     */
    private CollectionOfPublications buildHistoricalCollection() {
        CollectionOfPublicationsId id = new CollectionOfPublicationsId("collection-003");

        return collectionFactory.create(
                id,
                "Historical Masterpieces",
                new GenreId("GENRE8"),   // Historical Fiction
                new UserId("user-003"),
                true,
                LocalDateTime.now()
        );
    }
}