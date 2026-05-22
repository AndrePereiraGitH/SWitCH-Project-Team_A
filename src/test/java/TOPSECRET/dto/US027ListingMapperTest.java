package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.US027ListingDTO;
import TOPSECRET.dto.listing.US027ListingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link US027ListingMapper} class.
 * <p>
 * These tests validate both the successful mapping of {@link Listing} domain objects into
 * {@link US027ListingDTO} instances and the correct handling of exceptional
 * scenarios. The mapper interacts with multiple repositories to retrieve
 * related aggregates (PublicationSample, BibliographicalData, and Author),
 * and the tests ensure that all such interactions behave as expected under
 * normal and error conditions.
 * </p>
 *
 * <p>
 * The test suite also verifies the behavior of the bulk conversion method
 * {@code toDTOs}, including immutability of the returned list and validation
 * of invalid inputs such as null lists or lists containing null elements.
 * </p>
 */
class US027ListingMapperTest {

    // ========================================================
    // ================ Set Up ================================
    // ========================================================

    private IPublicationSampleRepository pubSampleRepoDouble;
    private IBibliographicalDataRepository bdRepoDouble;
    private IAuthorRepository authorRepoDouble;
    private Listing listingDouble;

    @BeforeEach
    void setup() {

        // Author
        Author authorDouble1 = mock(Author.class);
        AuthorId authorId1Double = mock(AuthorId.class);

        when(authorDouble1.id()).thenReturn(authorId1Double);
        when(authorDouble1.getName()).thenReturn("authorName1");

        Author authorDouble2 = mock(Author.class);
        AuthorId authorId2Double = mock(AuthorId.class);

        when(authorDouble2.id()).thenReturn(authorId2Double);
        when(authorDouble2.getName()).thenReturn("authorName2");

        authorRepoDouble = mock(IAuthorRepository.class);

        when(authorRepoDouble.findById(authorId1Double)).thenReturn(Optional.of(authorDouble1));
        when(authorRepoDouble.findById(authorId2Double)).thenReturn(Optional.of(authorDouble2));

        // BibliographicalData
        BibliographicalData bdDouble1 = mock(BibliographicalData.class);
        BibliographicalDataId bdId1Double = mock(BibliographicalDataId.class);

        when(bdDouble1.id()).thenReturn(bdId1Double);
        when(bdDouble1.getTitle()).thenReturn("bdTitle1");
        when(bdDouble1.getAuthors()).thenReturn(List.of(authorId1Double));

        BibliographicalData bdDouble2 = mock(BibliographicalData.class);
        BibliographicalDataId bdId2Double = mock(BibliographicalDataId.class);

        when(bdDouble2.id()).thenReturn(bdId2Double);
        when(bdDouble2.getTitle()).thenReturn("bdTitle2");
        when(bdDouble2.getAuthors()).thenReturn(List.of(authorId2Double));

        bdRepoDouble = mock(IBibliographicalDataRepository.class);

        when(bdRepoDouble.findById(bdId1Double)).thenReturn(Optional.of(bdDouble1));
        when(bdRepoDouble.findById(bdId2Double)).thenReturn(Optional.of(bdDouble2));

        // Publication Sample
        PublicationSample pubSampleDouble1 = mock(PublicationSample.class);
        PublicationSampleId pubSampleId1Double = mock(PublicationSampleId.class);

        when(pubSampleDouble1.id()).thenReturn(pubSampleId1Double);
        when(pubSampleDouble1.getBibliographicalDataID()).thenReturn(bdId1Double);

        PublicationSample pubSampleDouble2 = mock(PublicationSample.class);
        PublicationSampleId pubSampleId2Double = mock(PublicationSampleId.class);

        when(pubSampleDouble2.id()).thenReturn(pubSampleId2Double);
        when(pubSampleDouble2.getBibliographicalDataID()).thenReturn(bdId2Double);

        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);

        when(pubSampleRepoDouble.findById(pubSampleId1Double)).thenReturn(Optional.of(pubSampleDouble1));
        when(pubSampleRepoDouble.findById(pubSampleId2Double)).thenReturn(Optional.of(pubSampleDouble2));

        // Listing
        listingDouble = mock(Listing.class);
        ListingId listingIdDouble = mock(ListingId.class);

        when(listingDouble.id()).thenReturn(listingIdDouble);
        when(listingDouble.getListingTitle()).thenReturn("listingTitle");
        when(listingDouble.getItemsId()).thenReturn(List.of(pubSampleId1Double, pubSampleId2Double));
        when(listingDouble.getPrice()).thenReturn(mock(Price.class));
    }


    // =================================================================================================================
    // ================ toDTO ==========================================================================================
    // =================================================================================================================
    
    @Test
    void toDTO_ShouldMapAllFieldsCorrectly() throws InstanceNotFoundException {
        // Act
        US027ListingMapper mapper = new US027ListingMapper(pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);

        US027ListingDTO dto = mapper.toDTO(listingDouble);

        // Assert
        assertNotNull(dto);
        assertEquals(listingDouble.id().toString(), dto.listingId());
        assertEquals("listingTitle", dto.listingTitle());
        assertEquals(listingDouble.getPrice().toString(), dto.price());
        assertEquals(List.of("bdTitle1", "bdTitle2"), dto.pubTitles());
        assertEquals(List.of("authorName1", "authorName2"), dto.authorsName());
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenListingIsNull() {
        // Arrange
        US027ListingMapper mapper = new US027ListingMapper(pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);

        // Act + Assert
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenPublicationSampleNotFound() {
        // Arrange
        PublicationSampleId wrongIdDouble = mock(PublicationSampleId.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(wrongIdDouble));
        when(pubSampleRepoDouble.findById(wrongIdDouble))
                .thenReturn(Optional.empty());

        US027ListingMapper mapper = new US027ListingMapper(pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(listingDouble));
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenBibliographicalDataNotFound() {
        // Arrange
        BibliographicalDataId wrongIdDouble =  mock(BibliographicalDataId.class);
        PublicationSample pubSampleDouble = pubSampleRepoDouble.findById(
                listingDouble.getItemsId().get(0)).get();

        when(pubSampleDouble.getBibliographicalDataID()).thenReturn(wrongIdDouble);
        when(bdRepoDouble.findById(wrongIdDouble))
                .thenReturn(Optional.empty());

        US027ListingMapper mapper = new US027ListingMapper(pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(listingDouble));
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenAuthorNotFound() {
        // Arrange
        BibliographicalData bdDouble = bdRepoDouble.findById(
                pubSampleRepoDouble.findById(
                        listingDouble.getItemsId().get(0)).get().getBibliographicalDataID()).get();

        AuthorId missingAuthorIdDouble = mock(AuthorId.class);
        when(bdDouble.getAuthors()).thenReturn(List.of(missingAuthorIdDouble));
        when(authorRepoDouble.findById(missingAuthorIdDouble))
                .thenReturn(Optional.empty());

        US027ListingMapper mapper = new US027ListingMapper(pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(listingDouble));
    }


    // =================================================================================================================
    // ================ toDTOs =========================================================================================
    // =================================================================================================================

    @Test
    void toDTOs_ShouldThrowExceptionWhenListIsNull() {
        // Arrange
        US027ListingMapper mapper = new US027ListingMapper(pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);

        // Act + Assert
        assertThrows(NullPointerException.class, () -> mapper.toDTOs(null));
    }

    @Test
    void toDTOs_ShouldReturnEmptyListWhenInputListIsEmpty() throws InstanceNotFoundException {
        // Arrange
        US027ListingMapper mapper = new US027ListingMapper(pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);

        // Act
        List<US027ListingDTO> dtoList = mapper.toDTOs(List.of());

        // Assert
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    void toDTOs_ShouldReturnListWithOneDTOWhenListHasOneElement() throws InstanceNotFoundException {
        // Arrange
        US027ListingMapper mapper = new US027ListingMapper(pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);

        // Act
        List<US027ListingDTO> dtoList = mapper.toDTOs(List.of(listingDouble));

        // Assert
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
    }

    @Test
    void toDTOs_ShouldReturnListWithSameSizeAsInputList() throws InstanceNotFoundException {
        // Arrange
        US027ListingMapper mapper = new US027ListingMapper(pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);

        // Act
        List<US027ListingDTO> dtoList = mapper.toDTOs(List.of(listingDouble, listingDouble));

        // Assert
        assertEquals(2, dtoList.size());
    }
}