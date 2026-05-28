package TOPSECRET;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Library.LibraryFactory;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.ValueObjects.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.management.InstanceAlreadyExistsException;

/**
 * Data initializer that populates the database with sample {@link Library} entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Libraries are built via {@link LibraryFactory} to respect
 * domain invariants, and persisted through {@link ILibraryRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
public class LibraryDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(LibraryDataInitializer.class);

    private final ILibraryRepository libraryRepository;
    private final LibraryFactory libraryFactory;

    public LibraryDataInitializer(ILibraryRepository libraryRepository,
                                  LibraryFactory libraryFactory) {
        this.libraryRepository = libraryRepository;
        this.libraryFactory = libraryFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) throws InstanceAlreadyExistsException {
        log.info("[LibraryDataInitializer] Seeding library data...");

        libraryRepository.save(buildPersonalLibrary());
        libraryRepository.save(buildWishlistLibrary());
        libraryRepository.save(buildResearchLibrary());

        log.info("[LibraryDataInitializer] Done — {} libraries seeded.", 3);
    }

    // =========================================================================
    // Sample Libraries
    // =========================================================================

    /**
     * A personal reading library owned by user-001.
     */
    private Library buildPersonalLibrary() {
        UserId userId = new UserId("user-001");

        return libraryFactory.create("Personal Collection", userId);
    }

    /**
     * A wishlist library for books the user wants to acquire, owned by user-002.
     */
    private Library buildWishlistLibrary() {
        UserId userId = new UserId("user-002");

        return libraryFactory.create("Wishlist", userId);
    }

    /**
     * A research-focused library owned by user-003.
     */
    private Library buildResearchLibrary() {
        UserId userId = new UserId("user-003");

        return libraryFactory.create("Research & Reference", userId);
    }
}