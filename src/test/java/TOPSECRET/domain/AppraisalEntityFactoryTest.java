package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the AppraisalEntityFactory.
 *
 * <p>This test uses Mockito's {@code MockedConstruction} to intercept
 * the instantiation of {@code AppraisalEntity}.
 *
 * <p>The constructor call is replaced by a mock, allowing the test to:
 * <ul>
 *     <li>Verify that the constructor was invoked</li>
 *     <li>Define behavior using {@code when(...).thenReturn(...)}</li>
 * </ul>
 */
class AppraisalEntityFactoryTest {

    /**
     * Ensures that the factory method calls the AppraisalEntity constructor
     * and returns the mocked instance with the defined behavior.
     */
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

            AppraisalEntity entity = factory.create(
                    "apEn",
                    contact,
                    List.of(genre),
                    List.of(type)
            );

            // Verify if the constructor was called
            assertEquals(1, mocked.constructed().size());

            assertEquals("apEn", entity.getName());
        }

    }
}