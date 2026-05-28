package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer that populates the database with sample {@link Genre} entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Genres are built via {@link GenreFactory} to respect
 * domain invariants, and persisted through {@link IGenreRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
public class GenreDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(GenreDataInitializer.class);

    private final IGenreRepository genreRepository;
    private final GenreFactory genreFactory;

    public GenreDataInitializer(IGenreRepository genreRepository,
                                GenreFactory genreFactory) {
        this.genreRepository = genreRepository;
        this.genreFactory = genreFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) {
        log.info("[GenreDataInitializer] Seeding genre data...");

        genreRepository.save(genreFactory.create("Drama"));
        genreRepository.save(genreFactory.create("Science Fiction"));
        genreRepository.save(genreFactory.create("Mystery"));
        genreRepository.save(genreFactory.create("Fantasy"));
        genreRepository.save(genreFactory.create("Biography"));
        genreRepository.save(genreFactory.create("Horror"));
        genreRepository.save(genreFactory.create("Romance"));
        genreRepository.save(genreFactory.create("Historical Fiction"));
        genreRepository.save(genreFactory.create("Thriller"));
        genreRepository.save(genreFactory.create("Non-Fiction"));

        log.info("[GenreDataInitializer] Done — {} genres seeded.", 10);
    }
}