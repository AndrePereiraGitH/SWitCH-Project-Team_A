package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

/**
 * Data initializer that populates the database with sample {@link User} entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Users are built via {@link UserFactory} to respect
 * domain invariants, and persisted through {@link IUserRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
@Order(1)
public class UserDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(UserDataInitializer.class);

    private final IUserRepository userRepository;
    private final UserFactory userFactory;

    public UserDataInitializer(IUserRepository userRepository,
                               UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) {
        log.info("[UserDataInitializer] Seeding user data...");

        userRepository.save(buildRegularUser());
        userRepository.save(buildSellerUser());
        userRepository.save(buildAdminUser());

        log.info("[UserDataInitializer] Done — {} users seeded.", 3);
    }

    // =========================================================================
    // Sample Users
    // =========================================================================

    /**
     * A regular buyer user — matches user-001 referenced across other initializers.
     */
    private User buildRegularUser() {
        Email email = new Email("alice.smith", "example.com");
        Address address = new Address("Main Street", "12", "10001", new CityId("New York"));
        Contact contact = new Contact("9171234567", "1", email, address);
        Profile profile = new Profile("Alice Smith", contact, LocalDate.of(1990, 4, 15));
        UserId userId = new UserId("user-001");
        return userFactory.create(userId,"alice_smith", profile, Set.of(UserRole.NORMAL_USER));
    }

    /**
     * A seller user — matches user-002 referenced across other initializers.
     */
    private User buildSellerUser() {
        Email email = new Email("bob.jones", "example.com");
        Address address = new Address("Oak Avenue", "45", "90001", new CityId("Los Angeles"));
        Contact contact = new Contact("3109876543", "1", email, address);
        Profile profile = new Profile("Bob Jones", contact, LocalDate.of(1985, 8, 22));
        UserId userId = new UserId("user-002");

        return userFactory.create(userId,"bob_jones", profile, Set.of(UserRole.NORMAL_USER));
    }

    /**
     * An admin user — matches user-003 referenced across other initializers.
     */
    private User buildAdminUser() {
        Email email = new Email("carol.admin", "example.com");
        Address address = new Address("Pine Road", "7", "60601", new CityId("Chicago"));
        Contact contact = new Contact("3125550199", "1", email, address);
        Profile profile = new Profile("Carol Admin", contact, LocalDate.of(1978, 1, 30));
        UserId userId = new UserId("user-003");

        return userFactory.create(userId,"carol_admin", profile, Set.of(UserRole.ADMIN));
    }
}