package TOPSECRET.domain;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FactoryAppraisalEntity.
 *
 * Verifies that Factory correctly creates AppraisalEntity.
 */
class AppraisalEntityFactoryDependenciesTest {

    /**
     * Verifies that factory creates a valid AppraisalEntity.
     */
    @Test
    void shouldCreateAppraisalEntitySuccessfully() {

        // ARRANGE
        Contact contact = new Contact("912345678", "351", null, null);

        List<Genre> genres =
                List.of(new Genre("Fantasy"));

        List<PublicationType> types =
                List.of(new PublicationType(
                        MediaFormat.BOOK,
                        TypeOfBinding.HARDBACK,
                        "Standard"
                ));

        // ACT
        AppraisalEntity result =
                AppraisalEntityFactory.create(
                        "Entity",
                        contact,
                        genres,
                        types
                );

        // ASSERT
        assertEquals("Entity", result.getName());
        assertEquals(contact, result.getContact());
        assertEquals(1, result.getGenres().size());
        assertEquals(1, result.getPublicationTypes().size());
    }
}