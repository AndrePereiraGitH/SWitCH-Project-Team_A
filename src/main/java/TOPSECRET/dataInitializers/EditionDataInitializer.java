package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Data initializer that populates the database with sample {@link Edition} entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Editions are built via {@link EditionFactory} to respect
 * domain invariants, and persisted through {@link IEditionRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
public class EditionDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(EditionDataInitializer.class);

    private final IEditionRepository editionRepository;
    private final EditionFactory editionFactory;

    public EditionDataInitializer(IEditionRepository editionRepository,
                                  EditionFactory editionFactory) {
        this.editionRepository = editionRepository;
        this.editionFactory = editionFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) {
        log.info("[EditionDataInitializer] Seeding edition data...");

        editionRepository.save(buildModernBookEdition());
        editionRepository.save(buildVintageBookEdition());
        editionRepository.save(buildMagazineEdition());

        log.info("[EditionDataInitializer] Done — {} editions seeded.", 3);
    }

    // =========================================================================
    // Sample Editions
    // =========================================================================

    /**
     * A modern book edition published after 2007, requiring an ISBN-13 code.
     */
    private Edition buildModernBookEdition() {
        Code code = new Code(null, "978-0-13-468599-1", null);

        Dimensions dimensions = new Dimensions(10,20,30);

        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 23.5,500);

        PublicationTypeId publicationTypeId = new PublicationTypeId("pub-type-001");
        PublisherID publisherID = new PublisherID("publisher-001");

        return editionFactory.create(
                "3rd Edition",
                code,
                physicalAttributes,
                publicationTypeId,
                publisherID,
                Edition.BOOK,
                Locale.ENGLISH,
                LocalDate.of(2020, 6, 15),
                TypeOfBinding.PAPERBACK
        );
    }

    /**
     * A vintage book edition published between 1970 and 2006, requiring an ISBN-10 code.
     */
    private Edition buildVintageBookEdition() {
        Code code = new Code("0-13-110362-8", null, null); // ISBN-10

        Dimensions dimensions = new Dimensions(70,20,30);

        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0,500);

        PublicationTypeId publicationTypeId = new PublicationTypeId("pub-type-002");
        PublisherID publisherID = new PublisherID("publisher-002");

        return editionFactory.create(
                "1st Edition",
                code,
                physicalAttributes,
                publicationTypeId,
                publisherID,
                Edition.BOOK,
                Locale.ENGLISH,
                LocalDate.of(1988, 3, 22),
                TypeOfBinding.PERFECT_BINDING
        );
    }

    /**
     * A magazine edition published after 1976, requiring an ISSN code.
     */
    private Edition buildMagazineEdition() {
        Code code = new Code(null, null, "0027-9358"); // ISSN

        Dimensions dimensions = new Dimensions(10,20,80);

        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 800.5,700);

        PublicationTypeId publicationTypeId = new PublicationTypeId("pub-type-003");
        PublisherID publisherID = new PublisherID("publisher-003");

        return editionFactory.create(
                "Vol. 241 No. 4",
                code,
                physicalAttributes,
                publicationTypeId,
                publisherID,
                Edition.MAGAZINE,
                Locale.ENGLISH,
                LocalDate.of(2022, 4, 1),
                TypeOfBinding.OTHER
        );
    }
}