package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppraisalEntityRepositoryTest {

    // Helper to create a valid Contact
    private Contact createValidContact() {
        return new Contact("912345678", "351", null, null);
    }

    // Helper to create a valid PublicationType
    private PublicationType createValidPublicationType() {
        return new PublicationType(
                TypeOfBinding.HARDBACK,
                "Standard publication"
        );
    }

    // Helper to create a valid Genre
    private Genre createValidGenre() {
        return new Genre("Fiction");
    }

    @Test
    void shouldCreateAndAddAppraisalEntitySuccessfully() {

        // ARRANGE
        AppraisalEntityRepository repository = new AppraisalEntityRepository();

        String name = "Entity One";
        Contact contact = createValidContact();
        PublicationType publicationType = createValidPublicationType();
        Genre genre = createValidGenre();

        AppraisalEntity expected =
                new AppraisalEntity(name, contact, publicationType, genre);

        // ACT
        AppraisalEntity result =
                repository.createAndAddToAppraisalEntityList(
                        name, contact, publicationType, genre
                );

        // ASSERT
        assertEquals(expected, result);   // Compares entity (name + contact)
    }

    @Test
    void shouldThrowExceptionWhenAppraisalEntityAlreadyExists() {

        // ARRANGE
        AppraisalEntityRepository repository = new AppraisalEntityRepository();

        String name = "Entity One";
        Contact contact = createValidContact();
        PublicationType publicationType = createValidPublicationType();
        Genre genre = createValidGenre();

        repository.createAndAddToAppraisalEntityList(name, contact, publicationType, genre);

        // ACT AND ASSERT
        assertThrows(IllegalArgumentException.class, () -> {   // Duplicate creation must throw exception
            repository.createAndAddToAppraisalEntityList(name, contact, publicationType, genre);
        });
    }
}
