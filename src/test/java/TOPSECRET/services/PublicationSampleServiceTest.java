package TOPSECRET.services;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicationSampleServiceTest {

    // =========================================================================
    // Dependencies
    // =========================================================================

    @Mock
    IPublicationSampleRepository publicationSampleRepository;

    @Mock
    PublicationSampleFactory factory;

    @InjectMocks
    PublicationSampleService service;

    // =========================================================================
    // Test Doubles (Collaborators)
    // =========================================================================

    private BibliographicalDataID bdIdDouble;
    private UserId userIdDouble;
    private EditionID editionIdDouble;
    private PublicationStatus status;
    private Condition condition;

    @BeforeEach
    public void setUp() {
        bdIdDouble = mock(BibliographicalDataID.class);
        userIdDouble = mock(UserId.class);
        editionIdDouble = mock(EditionID.class);
        status = PublicationStatus.READ;
        condition = Condition.NEW;
    }

    // =========================================================================
    // CreateAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnAPbSample() {

        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);

        when(factory.create(bdIdDouble, userIdDouble, status, editionIdDouble, condition)).thenReturn(pbSample);
        when(publicationSampleRepository.save(pbSample)).thenReturn(pbSample);

        // Act
        PublicationSample result = service.createAndSave(bdIdDouble, userIdDouble, status, editionIdDouble, condition);

        // Assert
        assertEquals(pbSample, result);
    }

    @Test
    void createAndSave_shouldThrowWhenBibliographicalDataIdIsNull() {

        // Arrange
        when(factory.create(isNull(), eq(userIdDouble), eq(status), eq(editionIdDouble), eq(condition)))
                .thenThrow(NullPointerException.class);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(null, userIdDouble, status, editionIdDouble, condition));
    }

    @Test
    void createAndSave_shouldThrowWhenUserIdIsNull() {

        // Arrange
        when(factory.create(eq(bdIdDouble), isNull(), eq(status), eq(editionIdDouble), eq(condition)))
                .thenThrow(NullPointerException.class);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(bdIdDouble, null, status, editionIdDouble, condition));
    }

    @Test
    void createAndSave_shouldThrowWhenPublicationStatusIsNull() {

        // Arrange
        when(factory.create(eq(bdIdDouble), eq(userIdDouble), isNull(), eq(editionIdDouble), eq(condition)))
                .thenThrow(NullPointerException.class);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(bdIdDouble, userIdDouble, null, editionIdDouble, condition));
    }

    @Test
    void createAndSave_shouldThrowWhenEditionIdIsNull() {

        // Arrange
        when(factory.create(eq(bdIdDouble), eq(userIdDouble), eq(status), isNull(), eq(condition)))
                .thenThrow(NullPointerException.class);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(bdIdDouble, userIdDouble, status, null, condition));
    }

    @Test
    void createAndSave_shouldThrowWhenConditionIsNull() {

        // Arrange
        when(factory.create(eq(bdIdDouble), eq(userIdDouble), eq(status), eq(editionIdDouble), isNull()))
                .thenThrow(NullPointerException.class);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(bdIdDouble, userIdDouble, status, editionIdDouble, null));
    }
}