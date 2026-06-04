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

    @Override
    public void run(ApplicationArguments args) {
        log.info("[CollectionOfPublicationsDataInitializer] Seeding collections...");

        collectionRepository.save(buildFantasyCollection());
        collectionRepository.save(buildScienceCollection());
        collectionRepository.save(buildHistoricalCollection());

        log.info("[CollectionOfPublicationsDataInitializer] Done — {} collections seeded.", 3);
    }

    /**
     * Fantasy publications collection, owned by user-001. Private, no expiry.
     */
    private CollectionOfPublications buildFantasyCollection() {
        return collectionFactory.create(
                new CollectionOfPublicationsId("collection-001"),
                "Epic Fantasy Collection",
                new GenreId("GENRE4"),   // Fantasy
                new UserId("user-001"),
                true,   // private
                null    // no expiry
        );
    }

    /**
     * Science publications collection, owned by user-002. Private, no expiry.
     */
    private CollectionOfPublications buildScienceCollection() {
        return collectionFactory.create(
                new CollectionOfPublicationsId("collection-002"),
                "Science Essentials",
                new GenreId("GENRE2"),   // Science Fiction
                new UserId("user-002"),
                true,
                null
        );
    }

    /**
     * Historical publications collection, owned by user-003. Private, no expiry.
     */
    private CollectionOfPublications buildHistoricalCollection() {
        return collectionFactory.create(
                new CollectionOfPublicationsId("collection-003"),
                "Historical Masterpieces",
                new GenreId("GENRE8"),   // Historical Fiction
                new UserId("user-003"),
                true,
                null
        );
    }
}