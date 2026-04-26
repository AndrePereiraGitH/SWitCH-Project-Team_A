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
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PublicationSampleService}.
 *
 * <p>Covers two areas:</p>
 * <ul>
 *   <li><b>createAndSave — happy path</b>: verifies that a {@link PublicationSample}
 *       is correctly created via {@link PublicationSampleFactory} and persisted via
 *       {@link IPublicationSampleRepository}, returning the saved instance.</li>
 *   <li><b>createAndSave — null rejection</b>: verifies that all mandatory parameters
 *       ({@code bibliographicalDataId}, {@code userId}, {@code publicationStatus},
 *       {@code editionId}, {@code condition}) throw {@link NullPointerException}
 *       when {@code null}.</li>
 * </ul>
 *
 * <h2>Test setup</h2>
 * <p>{@code @SpringBootTest} is used so that {@code @MockBean} can register doubles in the
 * Spring Application Context. {@code MockitoAnnotations.openMocks(this)} is called in
 * {@code @BeforeEach} to wire those doubles into the {@code @InjectMocks} target.</p>
 */
@SpringBootTest
class PublicationSampleServiceTest {

    // =========================================================================
    // Dependencies
    // =========================================================================

    @MockBean
    IPublicationSampleRepository publicationSampleRepository;

    @MockBean
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
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
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
    void createAndSave_shouldReturnAPbSample(){

        //Arrange
        PublicationSample pbSample = mock(PublicationSample.class);

        when(factory.create(bdIdDouble,userIdDouble,status,editionIdDouble,condition)).thenReturn(pbSample);
        when(publicationSampleRepository.save(pbSample)).thenReturn(pbSample);

        //Act
        PublicationSample result = service.createAndSave(bdIdDouble,userIdDouble,status,editionIdDouble,condition);

        //Assert
        assertEquals(pbSample,result);
    }

    @Test
    void createAndSave_shouldThrowWhenBibliographicalDataIdIsNull() {

        //Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(null, userIdDouble, status, editionIdDouble, condition));
    }

    @Test
    void createAndSave_shouldThrowWhenUserIdIsNull() {

        //Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(bdIdDouble, null, status, editionIdDouble, condition));
    }

    @Test
    void createAndSave_shouldThrowWhenPublicationStatusIsNull() {

        //Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(bdIdDouble, userIdDouble, null, editionIdDouble, condition));
    }

    @Test
    void createAndSave_shouldThrowWhenEditionIdIsNull() {

        //Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(bdIdDouble, userIdDouble, status, null, condition));
    }

    @Test
    void createAndSave_shouldThrowWhenConditionIsNull() {

        //Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(bdIdDouble, userIdDouble, status, editionIdDouble, null));
    }

}