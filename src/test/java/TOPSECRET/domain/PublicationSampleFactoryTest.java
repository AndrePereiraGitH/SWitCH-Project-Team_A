package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationSampleFactoryTest {

    @Test
    void create_shouldReturnValidPublicationSample() {

        // Arrange
        PublicationSampleFactory factory = new PublicationSampleFactory();

        BibliographicalData bd = mock(BibliographicalData.class);
        User user = mock(User.class);
        Edition edition = mock(Edition.class);

        PublicationStatus status = PublicationStatus.READ;
        Condition condition = Condition.NEW;

        // Act
        PublicationSample result = factory.create(
                bd,
                user,
                status,
                edition,
                condition
        );

        // Assert
        assertNotNull(result);
        assertEquals(bd, result.getBibliographicalData());
        assertEquals(user, result.getOwner());
        assertEquals(status, result.getPublicationStatus());
        assertEquals(edition, result.getEdition());
        assertEquals(condition, result.getCondition());
    }
}