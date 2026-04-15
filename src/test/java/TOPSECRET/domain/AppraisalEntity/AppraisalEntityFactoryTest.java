package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppraisalEntityFactoryTest {

    @Test
    void shouldCallAppraisalEntityConstructor() {

        // ARRANGE
        AppraisalEntityFactory factory = new AppraisalEntityFactory();

        Contact contact = mock(Contact.class);
        GenreID genre = mock(GenreID.class);
        PublicationTypeId type = mock(PublicationTypeId.class);

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