package TOPSECRET.dataInitializers;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Data initializer that populates the database with sample AppraisalEntity
 * entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Entities are built via {@link AppraisalEntityFactory}
 * to respect domain invariants, and persisted through
 * {@link IAppraisalEntityRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
public class AppraisalEntityDataInitializer implements ApplicationRunner {

    private static final Logger log =
            LoggerFactory.getLogger(AppraisalEntityDataInitializer.class);

    private final IAppraisalEntityRepository appraisalEntityRepository;
    private final AppraisalEntityFactory appraisalEntityFactory;

    public AppraisalEntityDataInitializer(
            IAppraisalEntityRepository appraisalEntityRepository,
            AppraisalEntityFactory appraisalEntityFactory) {

        this.appraisalEntityRepository = appraisalEntityRepository;
        this.appraisalEntityFactory = appraisalEntityFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) {

        log.info("[AppraisalEntityDataInitializer] Seeding appraisal entities...");

        appraisalEntityRepository.save(buildBookReviewEntity());

        appraisalEntityRepository.save(buildMagazineReviewEntity());

        appraisalEntityRepository.save(buildMixedPublicationEntity());

        log.info("[AppraisalEntityDataInitializer] Done — {} appraisal entities seeded.", 3);
    }

    // =========================================================================
    // Sample Appraisal Entities
    // =========================================================================

    /**
     * Book-focused appraisal entity.
     */
    private AppraisalEntity buildBookReviewEntity() {

        Contact contact = new Contact(
                "960293849",
                "351",
                new Email("antonia", "gmail.com"),
                new Address("Rua Firmeza", "147", "4500", new CityId("PORTO")));

        List<PublicationTypeId> publicationTypes = List.of(
                new PublicationTypeId("BOOK")
        );

        List<GenreId> genres = List.of(
                new GenreId("FANTASY"),
                new GenreId("SCIENCE_FICTION"),
                new GenreId("DRAMA")
        );

        return appraisalEntityFactory.create(
                "Book Review Hub",
                contact,
                publicationTypes,
                genres
        );
    }

    /**
     * Magazine-focused appraisal entity.
     */
    private AppraisalEntity buildMagazineReviewEntity() {

        Contact contact = new Contact(
                "910293849",
                "351",
                new Email("paulo", "gmail.com"),
                new Address("Rua Santa Catarina", "147", "4500", new CityId("PORTO")));

        List<PublicationTypeId> publicationTypes = List.of(
                new PublicationTypeId("MAGAZINE")
        );

        List<GenreId> genres = List.of(
                new GenreId("HISTORY"),
                new GenreId("SCIENCE"),
                new GenreId("TECHNOLOGY")
        );

        return appraisalEntityFactory.create(
                "Magazine Media Review",
                contact,
                publicationTypes,
                genres
        );
    }

    /**
     * Mixed publication appraisal entity.
     */
    private AppraisalEntity buildMixedPublicationEntity() {

        Contact contact = new Contact(
                "930293849",
                "351",
                new Email("judite", "gmail.com"),
                new Address("Rua Fernandes Tomás", "147", "4500", new CityId("PORTO")));

        List<PublicationTypeId> publicationTypes = List.of(
                new PublicationTypeId("BOOK"),
                new PublicationTypeId("MAGAZINE"),
                new PublicationTypeId("NEWSPAPER")
        );

        List<GenreId> genres = List.of(
                new GenreId("BIOGRAPHY"),
                new GenreId("POLITICS"),
                new GenreId("ECONOMY")
        );

        return appraisalEntityFactory.create(
                "Global Critics Editorial",
                contact,
                publicationTypes,
                genres
        );
    }
}