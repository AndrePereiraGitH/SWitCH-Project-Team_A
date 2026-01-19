package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Enum.Country;
import TOPSECRET.domain.Profile;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryTest {

        @Test
        void shouldRegisterNewUserSuccessfully() {
            UserRepository repository = new UserRepository();
            User user = new User(
                    "validUser",
                    new Contact(),
                    new Profile(
                            "Test User",
                            Country.PORTUGAL,
                            LocalDate.of(2000, 5, 10)
                    )
            );

            repository.register(user);

            assertTrue(repository.exists("validUser"));
        }

        @Test
        void shouldThrowExceptionWhenUserIsNull() {
            UserRepository repository = new UserRepository();

            assertThrows(IllegalArgumentException.class, () -> {
                repository.register(null);
            });
        }

        @Test
        void shouldNotAllowDuplicateUsers() {
            UserRepository repository = new UserRepository();
            User user = new User(
                    "duplicateUser",
                    new Contact(),
                    new Profile(
                            "Test User",
                            Country.PORTUGAL,
                            LocalDate.of(2000, 5, 10)
                    )
            );

            repository.register(user);

            assertThrows(IllegalArgumentException.class, () -> {
                repository.register(user);
            });
        }
    }