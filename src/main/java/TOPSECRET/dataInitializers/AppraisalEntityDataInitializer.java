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

@Component
public class AppraisalEntityDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AppraisalEntityDataInitializer.class);

    private final IAppraisalEntityRepository appraisalEntityRepository;
    private final AppraisalEntityFactory appraisalEntityFactory;

    public AppraisalEntityDataInitializer(IAppraisalEntityRepository appraisalEntityRepository, AppraisalEntityFactory appraisalEntityFactory) {
        this.appraisalEntityRepository = appraisalEntityRepository;
        this.appraisalEntityFactory = appraisalEntityFactory;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("[AppraisalEntityDataInitializer] Seeding appraisal entities...");
        appraisalEntityRepository.save(buildBookReviewEntity());
        appraisalEntityRepository.save(buildMagazineReviewEntity());
        appraisalEntityRepository.save(buildMixedPublicationEntity());
        log.info("[AppraisalEntityDataInitializer] Done — {} appraisal entities seeded.", 3);
    }

    private AppraisalEntity buildBookReviewEntity() {
        Contact contact = new Contact("960293849", "351", new Email("antonia", "gmail.com"),
                new Address("Rua Firmeza", "147", "4500", new CityId("Porto"))); // Fixed CityId

        List<PublicationTypeId> publicationTypes = List.of(new PublicationTypeId("pub-type-001")); // Fixed PubType

        List<GenreId> genres = List.of(
                new GenreId("GENRE4"), // Fantasy
                new GenreId("GENRE2"), // Science Fiction
                new GenreId("GENRE1")  // Drama
        ); // Fixed Genre IDs

        return appraisalEntityFactory.create("Book Review Hub", contact, publicationTypes, genres);
    }

    private AppraisalEntity buildMagazineReviewEntity() {
        Contact contact = new Contact("910293849", "351", new Email("paulo", "gmail.com"),
                new Address("Rua Santa Catarina", "147", "4500", new CityId("Porto"))); // Fixed CityId

        List<PublicationTypeId> publicationTypes = List.of(new PublicationTypeId("pub-type-002")); // Fixed PubType

        List<GenreId> genres = List.of(
                new GenreId("GENRE8"), // Historical Fiction
                new GenreId("GENRE10") // Non-Fiction (replacing Science/Tech to map correctly)
        ); // Fixed Genre IDs

        return appraisalEntityFactory.create("Magazine Media Review", contact, publicationTypes, genres);
    }

    private AppraisalEntity buildMixedPublicationEntity() {
        Contact contact = new Contact("930293849", "351", new Email("judite", "gmail.com"),
                new Address("Rua Fernandes Tomás", "147", "4500", new CityId("Porto"))); // Fixed CityId

        List<PublicationTypeId> publicationTypes = List.of(
                new PublicationTypeId("pub-type-001"), // Book
                new PublicationTypeId("pub-type-002")  // Magazine
        ); // Fixed PubType IDs

        List<GenreId> genres = List.of(
                new GenreId("GENRE5"), // Biography
                new GenreId("GENRE10") // Non-fiction (replacing Politics/Economy)
        ); // Fixed Genre IDs

        return appraisalEntityFactory.create("Global Critics Editorial", contact, publicationTypes, genres);
    }
}