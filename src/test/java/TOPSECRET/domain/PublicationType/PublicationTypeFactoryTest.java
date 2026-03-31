package TOPSECRET.domain.PublicationType;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link PublicationTypeFactory}.
 *
 * <p>
 * Validates that the factory correctly creates PublicationType
 * instances by invoking the constructor.
 * </p>
 *
 * <p>
 * Uses {@code mockConstruction} to ensure that a new instance
 * is created and returned.
 * </p>
 */
class PublicationTypeFactoryTest {

    @Test
    void mockConstructsPublicationType() {

        // ARRANGE
        String mediaFormat = "Book";

        try (MockedConstruction<PublicationType> mocked =
                     mockConstruction(PublicationType.class,
                             (mock, context) -> {
                                 when(mock.getMediaFormat()).thenReturn(mediaFormat);
                             })) {

            PublicationTypeFactory sut = new PublicationTypeFactory();

            // ACT
            PublicationType result = sut.create(mediaFormat);

            // ASSERT
            assertEquals(1, mocked.constructed().size());
            assertSame(mocked.constructed().get(0), result);
            assertEquals(mediaFormat, result.getMediaFormat());
        }
    }

}