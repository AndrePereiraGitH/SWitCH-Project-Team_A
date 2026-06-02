package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;

/**
 * Data initializer that populates the database with sample Author
 * entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Authors are built via {@link AuthorFactory}
 * to respect domain invariants, and persisted through
 * {@link IAuthorRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
@Order(1)
public class AuthorDataInitializer implements ApplicationRunner {

    private static final Logger log =
            LoggerFactory.getLogger(AuthorDataInitializer.class);

    private final IAuthorRepository authorRepository;
    private final AuthorFactory authorFactory;

    public AuthorDataInitializer(
            IAuthorRepository authorRepository,
            AuthorFactory authorFactory) {

        this.authorRepository = authorRepository;
        this.authorFactory = authorFactory;
    }

    @Override
    public void run(ApplicationArguments args) throws InstanceAlreadyExistsException {
        log.info("[AuthorDataInitializer] Seeding authors...");

        authorRepository.save(buildFantasyAuthor());
        authorRepository.save(buildScienceAuthor());
        authorRepository.save(buildHistoricalAuthor());

        log.info("[AuthorDataInitializer] Done — {} authors seeded.", 3);
    }

    private Author buildFantasyAuthor() {
        return authorFactory.create(
                new AuthorId("AUTH1"),
                "J. R. R. Tolkien",
                new CountryId("UK"),
                LocalDate.of(1892, 1, 3),
                "English writer, poet, philologist, and academic, best known for The Lord of the Rings."
        );
    }

    private Author buildScienceAuthor() {
        return authorFactory.create(
                new AuthorId("AUTH2"),
                "Isaac Asimov",
                new CountryId("USA"),
                LocalDate.of(1920, 1, 2),
                "American writer and professor of biochemistry, famous for his works of hard science fiction."
        );
    }

    private Author buildHistoricalAuthor() {
        return authorFactory.create(
                new AuthorId("AUTH3"),
                "Ken Follett",
                new CountryId("UK"),
                LocalDate.of(1949, 6, 5),
                "Welsh author known for thrillers and historical novels, including The Pillars of the Earth."
        );
    }
}