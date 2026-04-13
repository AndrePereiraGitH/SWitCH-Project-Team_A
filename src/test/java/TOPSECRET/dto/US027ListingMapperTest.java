package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
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
    private US027ListingMapper mapper;
    private Listing listingDouble;

    @BeforeEach
    void setup() {

        // Author
        Author authorDouble1 = mock(Author.class);
        AuthorId authorId1 = new AuthorId("authorId1");

        when(authorDouble1.id()).thenReturn(authorId1);
        when(authorDouble1.getName()).thenReturn("authorName1");

        Author authorDouble2 = mock(Author.class);
        AuthorId authorId2 = new AuthorId("authorId2");

        when(authorDouble2.id()).thenReturn(authorId2);
        when(authorDouble2.getName()).thenReturn("authorName2");

        authorRepoDouble = mock(IAuthorRepository.class);

        when(authorRepoDouble.findAuthorById(authorId1)).thenReturn(Optional.of(authorDouble1));
        when(authorRepoDouble.findAuthorById(authorId2)).thenReturn(Optional.of(authorDouble2));

        // BibliographicalData
        BibliographicalData bdDouble1 = mock(BibliographicalData.class);
        BibliographicalDataID bdId1 = new BibliographicalDataID("bdId1");

        when(bdDouble1.id()).thenReturn(bdId1);
        when(bdDouble1.getTitle()).thenReturn("bdTitle1");
        when(bdDouble1.getAuthors()).thenReturn(List.of(authorId1));

        BibliographicalData bdDouble2 = mock(BibliographicalData.class);
        BibliographicalDataID bdId2 = new BibliographicalDataID("bdId2");

        when(bdDouble2.id()).thenReturn(bdId2);
        when(bdDouble2.getTitle()).thenReturn("bdTitle2");
        when(bdDouble2.getAuthors()).thenReturn(List.of(authorId2));

        bdRepoDouble = mock(IBibliographicalDataRepository.class);

        when(bdRepoDouble.findBibliographicalDataById(bdId1)).thenReturn(Optional.of(bdDouble1));
        when(bdRepoDouble.findBibliographicalDataById(bdId2)).thenReturn(Optional.of(bdDouble2));

        // Publication Sample
        PublicationSample pubSampleDouble1 = mock(PublicationSample.class);
        PublicationSampleId pubSampleId1 = new PublicationSampleId("pubSampleId1");

        when(pubSampleDouble1.id()).thenReturn(pubSampleId1);
        when(pubSampleDouble1.getBibliographicalDataID()).thenReturn(bdId1);

        PublicationSample pubSampleDouble2 = mock(PublicationSample.class);
        PublicationSampleId pubSampleId2 = new PublicationSampleId("pubSampleId2");

        when(pubSampleDouble2.id()).thenReturn(pubSampleId2);
        when(pubSampleDouble2.getBibliographicalDataID()).thenReturn(bdId2);

        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);

        when(pubSampleRepoDouble.findPublicationSampleById(pubSampleId1)).thenReturn(Optional.of(pubSampleDouble1));
        when(pubSampleRepoDouble.findPublicationSampleById(pubSampleId2)).thenReturn(Optional.of(pubSampleDouble2));

        // Listing
        listingDouble = mock(Listing.class);
        ListingId listingId = new ListingId("listingId");

        when(listingDouble.id()).thenReturn(listingId);
        when(listingDouble.getListingTitle()).thenReturn("listingTitle");
        when(listingDouble.getItemsId()).thenReturn(List.of(pubSampleId1, pubSampleId2));
        when(listingDouble.getPrice()).thenReturn(mock(Price.class));


        // Mapper
        mapper = new US027ListingMapper(pubSampleRepoDouble, bdRepoDouble, authorRepoDouble);
    }


    // =================================================================================================================
    // ================ toDTO ==========================================================================================
    // =================================================================================================================
    
    @Test
    void toDTO_ShouldMapAllFieldsCorrectly() throws InstanceNotFoundException {
        // Act
        US027ListingDTO dto = mapper.toDTO(listingDouble);

        // Assert
        assertNotNull(dto);
        assertEquals(listingDouble.id(), dto.listingId());
        assertEquals("listingTitle", dto.listingTitle());
        assertEquals(listingDouble.getPrice().toString(), dto.price());
        assertEquals(List.of("bdTitle1", "bdTitle2"), dto.pubTitles());
        assertEquals(List.of("authorName1", "authorName2"), dto.authorsName());
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenListingIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenPublicationSampleNotFound() {
        // Arrange
        PublicationSampleId wrongId = new PublicationSampleId("wrong");
        when(listingDouble.getItemsId()).thenReturn(List.of(wrongId));
        when(pubSampleRepoDouble.findPublicationSampleById(wrongId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(listingDouble));
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenBibliographicalDataNotFound() {
        // Arrange
        BibliographicalDataID wrongId = new BibliographicalDataID("wrongBD");
        PublicationSample pubSampleDouble = pubSampleRepoDouble.findPublicationSampleById(
                listingDouble.getItemsId().get(0)).get();

        when(pubSampleDouble.getBibliographicalDataID()).thenReturn(wrongId);
        when(bdRepoDouble.findBibliographicalDataById(wrongId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(listingDouble));
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenAuthorNotFound() {
        // Arrange
        BibliographicalData bdDouble = bdRepoDouble.findBibliographicalDataById(
                pubSampleRepoDouble.findPublicationSampleById(
                        listingDouble.getItemsId().get(0)).get().getBibliographicalDataID()).get();

        AuthorId missingAuthorId = new AuthorId("missing");
        when(bdDouble.getAuthors()).thenReturn(List.of(missingAuthorId));
        when(authorRepoDouble.findAuthorById(missingAuthorId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> mapper.toDTO(listingDouble));
    }


    // =================================================================================================================
    // ================ toDTOs =========================================================================================
    // =================================================================================================================

    @Test
    void toDTOs_ShouldThrowExceptionWhenListIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> mapper.toDTOs(null));
    }

    @Test
    void toDTOs_ShouldThrowExceptionWhenListContainsNullElement() {
        // Arrange
        List<Listing> listWithNull = new ArrayList<>();
        listWithNull.add(listingDouble);
        listWithNull.add(null);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> mapper.toDTOs(listWithNull));
    }

    @Test
    void toDTOs_ShouldReturnEmptyListWhenInputListIsEmpty() throws InstanceNotFoundException {
        // Act
        List<US027ListingDTO> dtoList = mapper.toDTOs(List.of());

        // Assert
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    void toDTOs_ShouldReturnListWithOneDTOWhenListHasOneElement() throws InstanceNotFoundException {
        // Act
        List<US027ListingDTO> dtoList = mapper.toDTOs(List.of(listingDouble));

        // Assert
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
    }

    @Test
    void toDTOs_ShouldReturnListWithSameSizeAsInputList() throws InstanceNotFoundException {
        // Act
        List<US027ListingDTO> dtoList = mapper.toDTOs(List.of(listingDouble, listingDouble));

        // Assert
        assertEquals(2, dtoList.size());
    }

    @Test
    void toDTOs_ShouldBeImmutableList() throws InstanceNotFoundException {
        // Act
        List<US027ListingDTO> dtoList = mapper.toDTOs(List.of(listingDouble));

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> dtoList.add(mock(US027ListingDTO.class)));
    }
}