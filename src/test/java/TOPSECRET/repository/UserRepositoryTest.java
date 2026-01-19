package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Enum.Country;
import TOPSECRET.domain.Profile;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;

import static TOPSECRET.domain.Enum.UserRole.NORMAL_USER;
import static org.junit.jupiter.api.Assertions.*;

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
                            LocalDate.of(2000, 5, 10)),
                    NORMAL_USER
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
                    ),
                    NORMAL_USER
            );

            repository.register(user);

            assertThrows(IllegalArgumentException.class, () -> {
                repository.register(user);
            });
        }

    @Test
    void shouldReturnFalseWhenUserDoesNotExist() {
        UserRepository repository = new UserRepository();

        boolean exists = repository.exists("nonExistingUser");

        assertFalse(exists);
    }
    }