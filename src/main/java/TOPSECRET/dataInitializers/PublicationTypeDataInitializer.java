package TOPSECRET.dataInitializers;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer that populates the database with sample {@link PublicationType} entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Publication types are built via {@link PublicationTypeFactory} to respect
 * domain invariants, and persisted through {@link IPublicationTypeRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
public class PublicationTypeDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PublicationTypeDataInitializer.class);

    private final IPublicationTypeRepository publicationTypeRepository;
    private final PublicationTypeFactory publicationTypeFactory;

    public PublicationTypeDataInitializer(IPublicationTypeRepository publicationTypeRepository,
                                          PublicationTypeFactory publicationTypeFactory) {
        this.publicationTypeRepository = publicationTypeRepository;
        this.publicationTypeFactory = publicationTypeFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) {
        log.info("[PublicationTypeDataInitializer] Seeding publication type data...");

        publicationTypeRepository.save(publicationTypeFactory.create("Book"));
        publicationTypeRepository.save(publicationTypeFactory.create("Magazine"));
        publicationTypeRepository.save(publicationTypeFactory.create("AudioBook"));
        publicationTypeRepository.save(publicationTypeFactory.create("eBook"));
        publicationTypeRepository.save(publicationTypeFactory.create("Comic"));

        log.info("[PublicationTypeDataInitializer] Done — {} publication types seeded.", 5);
    }
}