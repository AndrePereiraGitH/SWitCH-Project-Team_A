package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for US023PublicationSampleMapper.
 *
 * This suite verifies that the mapper correctly converts PublicationSample
 * domain objects into US023PublicationSampleDTO instances, and that it
 * properly handles missing data by throwing the expected exceptions.
 *
 * It also checks constructor validation and the behavior of mapping
 * collections of PublicationSample objects.
 */
class US023ListingMapperTest {

    // ========================================================
    // ================ Set Up ================================
    // ========================================================

    private IListingRepository listingRepoDouble;
    private IPublicationSampleRepository pubSampleRepoDouble;
    private IBibliographicalDataRepository bdRepoDouble;
    private IAuthorRepository authorRepoDouble;
    private US023ListingMapper mapper;
    private Listing listingDouble;
    private ListingId listingIdDouble;

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
        BibliographicalDataID bdId1Double = mock(BibliographicalDataID.class);

        when(bdDouble1.id()).thenReturn(bdId1Double);
        when(bdDouble1.getTitle()).thenReturn("bdTitle1");
        when(bdDouble1.getAuthors()).thenReturn(List.of(authorId1Double));

        BibliographicalData bdDouble2 = mock(BibliographicalData.class);
        BibliographicalDataID bdId2Double = mock(BibliographicalDataID.class);

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
        listingIdDouble = mock(ListingId.class);

        when(listingDouble.id()).thenReturn(listingIdDouble);
        when(listingDouble.getListingTitle()).thenReturn("listingTitle");
        when(listingDouble.getItemsId()).thenReturn(List.of(pubSampleId1Double, pubSampleId2Double));
        when(listingDouble.getPrice()).thenReturn(mock(Price.class));

        listingRepoDouble = mock(IListingRepository.class);
        when(listingRepoDouble.findById(listingIdDouble)).thenReturn(Optional.of(listingDouble));

        // Mapper
        mapper = new US023ListingMapper(listingRepoDouble, pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);
    }


    // =================================================================================================================
    // ================ toDTO ==========================================================================================
    // =================================================================================================================

    @Test
    void toDTO_ShouldMapAllFieldsCorrectly() throws InstanceNotFoundException {
        // Act
        US023ListingDTO dto = mapper.toDTO(listingIdDouble);

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
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> mapper.toDTO(null));
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenPublicationSampleNotFound() {
        // Arrange
        PublicationSampleId wrongIdDouble = mock(PublicationSampleId.class);
        when(listingDouble.getItemsId()).thenReturn(List.of(wrongIdDouble));
        when(pubSampleRepoDouble.findById(wrongIdDouble))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(listingIdDouble));
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenBibliographicalDataNotFound() {
        // Arrange
        BibliographicalDataID wrongIdDouble =  mock(BibliographicalDataID.class);
        PublicationSample pubSampleDouble = pubSampleRepoDouble.findById(
                listingDouble.getItemsId().get(0)).get();

        when(pubSampleDouble.getBibliographicalDataID()).thenReturn(wrongIdDouble);
        when(bdRepoDouble.findById(wrongIdDouble))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(listingIdDouble));
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

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(listingIdDouble));
    }

    @Test
    void toDTO_ShouldThrowInstanceNotFoundExceptionWhenListingDoesNotExist() throws Exception {
        // Arrange
        when(listingRepoDouble.findById(listingIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> mapper.toDTO(listingIdDouble));
    }



    // =================================================================================================================
    // ================ toDTOs =========================================================================================
    // =================================================================================================================

    @Test
    void toDTOs_ShouldThrowExceptionWhenListIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> mapper.toDTOs(null));
    }

    @Test
    void toDTOs_ShouldThrowExceptionWhenListContainsNullElement() {
        // Arrange
        List<ListingId> listWithNull = new ArrayList<>();
        listWithNull.add(listingIdDouble);
        listWithNull.add(null);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> mapper.toDTOs(listWithNull));
    }

    @Test
    void toDTOs_ShouldReturnEmptyListWhenInputListIsEmpty() throws InstanceNotFoundException {
        // Act
        List<US023ListingDTO> dtoList = mapper.toDTOs(List.of());

        // Assert
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    void toDTOs_ShouldReturnListWithOneDTOWhenListHasOneElement() throws InstanceNotFoundException {
        // Act
        List<US023ListingDTO> dtoList = mapper.toDTOs(List.of(listingIdDouble));

        // Assert
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
    }

    @Test
    void toDTOs_ShouldReturnListWithSameSizeAsInputList() throws InstanceNotFoundException {
        // Act
        List<US023ListingDTO> dtoList = mapper.toDTOs(List.of(listingIdDouble, listingIdDouble));

        // Assert
        assertEquals(2, dtoList.size());
    }

    @Test
    void toDTOs_ShouldBeImmutableList() throws InstanceNotFoundException {
        // Act
        List<US023ListingDTO> dtoList = mapper.toDTOs(List.of(listingIdDouble));

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> dtoList.add(mock(US023ListingDTO.class)));
    }

    @Test
    void toDTOs_ShouldThrowInstanceNotFoundExceptionWhenToDTORaisesException() throws Exception {
        // Arrange
        US023ListingMapper mapper =
                new US023ListingMapper(listingRepoDouble, pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);

        when(listingRepoDouble.findById(listingIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> mapper.toDTOs(List.of(listingIdDouble)));
    }

}