package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class US004ToRegisterAppraisalEntityControllerTest {

    // Helper to create a valid Contact
    private Contact createValidContact() {
        return new Contact("912345678", "351", null, null);
    }

    // Helper to create a valid PublicationType
    private PublicationType createValidPublicationType() {
        return new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard publication"
        );
    }

    // Helper to create a valid Genre
    private Genre createValidGenre() {
        return new Genre("Fiction");
    }

    @Test
    void shouldRegisterAppraisalEntitySuccessfully() {

        // ARRANGE
        US004ToRegisterAppraisalEntityController controller = new US004ToRegisterAppraisalEntityController();

        String name = "Entity One";
        Contact contact = createValidContact();
        PublicationType publicationType = createValidPublicationType();
        Genre genre = createValidGenre();

        AppraisalEntity expected =
                new AppraisalEntity(name, contact, publicationType, genre);

        // ACT
        AppraisalEntity result =
                controller.registerAppraisalEntity(
                        name, contact, publicationType, genre
                );

        // ASSERT
        assertEquals(expected, result);   // Compares entity using equals() (name + contact)
    }

    @Test
    void shouldThrowExceptionWhenRegisteringDuplicateAppraisalEntity() {

        // ARRANGE
        US004ToRegisterAppraisalEntityController controller = new US004ToRegisterAppraisalEntityController();

        String name = "Entity One";
        Contact contact = createValidContact();
        PublicationType publicationType = createValidPublicationType();
        Genre genre = createValidGenre();

        controller.registerAppraisalEntity(name, contact, publicationType, genre);

        // ACT AND ASSERT
        assertThrows(IllegalArgumentException.class, () -> {   // Duplicate registration must throw exception
            controller.registerAppraisalEntity(name, contact, publicationType, genre);
        });
    }
}

