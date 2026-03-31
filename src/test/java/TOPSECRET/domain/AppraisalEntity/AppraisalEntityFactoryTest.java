package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationType.PublicationType;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityFactory}.
 *
 * <p>
 * Validates that the factory correctly creates AppraisalEntity
 * instances by invoking the constructor.
 * </p>
 *
 * <p>
 * Uses {@code mockConstruction} to intercept object creation
 * and verify that a new instance is created.
 * </p>
 */
class AppraisalEntityFactoryTest {

    @Test
    void shouldCallAppraisalEntityConstructor() {

        // ARRANGE
        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        Contact contact = mock(Contact.class);
        Genre genre = mock(Genre.class);
        PublicationType type = mock(PublicationType.class);

        try (MockedConstruction<AppraisalEntity> mocked =
                mockConstruction(AppraisalEntity.class,
                    (mock, context) -> {
                        when(mock.getName()).thenReturn("apEn");
            })) {

            // ACT
            AppraisalEntity entity = factory.create(
                    "apEn",
                    contact,
                    List.of(type),
                    List.of(genre)
            );

            // ASSERT
            assertEquals(1, mocked.constructed().size());
            assertEquals("apEn", entity.getName());
            assertSame(mocked.constructed().get(0), entity);
        }

    }
}