package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockConstruction;

/**
 * Verifies that the {@code create} method constructs a {@link PublicationType}
 * instance using Mockito's constructor mocking.
 *
 * <p>This test ensures that:
 * <ul>
 *   <li>A {@link PublicationType} object is created when the {@code create} method is called.</li>
 *   <li>The returned object is the same instance that Mockito intercepts and constructs.</li>
 *   <li>Exactly one {@link PublicationType} instance is constructed during the execution.</li>
 * </ul>
 *
 * <p>The test uses {@link org.mockito.MockedConstruction} to intercept calls to the
 * {@link PublicationType} constructor and verify that object creation occurs as expected.
 */

class PublicationTypeFactoryTest {

    @Test
    void mockConstructsPublicationType() {
        String mediaFormat = "Book";

        try (MockedConstruction<PublicationType> mocked =
                     mockConstruction(PublicationType.class)) {

            PublicationTypeFactory sut = new PublicationTypeFactory();
            PublicationType result = sut.create(mediaFormat);

            // Verify a single instance was constructed
            assertEquals(1, mocked.constructed().size());

            // Verify returned object is the constructed mock
            assertSame(mocked.constructed().get(0), result);
        }
    }

}