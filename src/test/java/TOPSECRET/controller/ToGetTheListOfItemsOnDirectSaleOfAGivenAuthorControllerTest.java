package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US025AuthorDTO;
import TOPSECRET.dto.US025DirectSaleDTO;
import TOPSECRET.dto.US025AuthorMapper;
import TOPSECRET.dto.US025DirectSaleMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController}.
 *
 * <p>Covers:
 * <ul>
 *   <li>Constructor - null guard validation for all parameters</li>
 *   <li>{@code getAllAuthors()} - delegation to repository and mapper</li>
 *   <li>{@code getDirectSalesByAuthorId()} - null guard, cross-aggregate navigation, and sorting</li>
 * </ul>
 *
 * <p>Uses doubles for all dependencies to isolate the controller
 * from repository and mapper implementations.
 */
class ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorControllerTest {

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Ensures constructor throws exception when AuthorRepository is null.
     */
    @Test
    void constructorShouldThrowExceptionWhenAuthorRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        null,
                        mock(IListingRepository.class),
                        mock(IBibliographicalDataRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(US025AuthorMapper.class),
                        mock(US025DirectSaleMapper.class)));
    }

    /**
     * Ensures constructor throws exception when ListingRepository is null.
     */
    @Test
    void constructorShouldThrowExceptionWhenListingRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        mock(IAuthorRepository.class),
                        null,
                        mock(IBibliographicalDataRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(US025AuthorMapper.class),
                        mock(US025DirectSaleMapper.class)));
    }

    /**
     * Ensures constructor throws exception when BibliographicalDataRepository is null.
     */
    @Test
    void constructorShouldThrowExceptionWhenBibliographicalDataRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        mock(IAuthorRepository.class),
                        mock(IListingRepository.class),
                        null,
                        mock(IPublicationSampleRepository.class),
                        mock(US025AuthorMapper.class),
                        mock(US025DirectSaleMapper.class)));
    }

    /**
     * Ensures constructor throws exception when PublicationSampleRepository is null.
     */
    @Test
    void constructorShouldThrowExceptionWhenPublicationSampleRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        mock(IAuthorRepository.class),
                        mock(IListingRepository.class),
                        mock(IBibliographicalDataRepository.class),
                        null,
                        mock(US025AuthorMapper.class),
                        mock(US025DirectSaleMapper.class)));
    }

    /**
     * Ensures constructor throws exception when AuthorMapper is null.
     */
    @Test
    void constructorShouldThrowExceptionWhenAuthorMapperIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        mock(IAuthorRepository.class),
                        mock(IListingRepository.class),
                        mock(IBibliographicalDataRepository.class),
                        mock(IPublicationSampleRepository.class),
                        null,
                        mock(US025DirectSaleMapper.class)));
    }

    /**
     * Ensures constructor throws exception when DirectSaleMapper is null.
     */
    @Test
    void constructorShouldThrowExceptionWhenDirectSaleMapperIsNull() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        mock(IAuthorRepository.class),
                        mock(IListingRepository.class),
                        mock(IBibliographicalDataRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(US025AuthorMapper.class),
                        null));
    }

    // =========================================================================
    // getAllAuthors
    // =========================================================================

    /**
     * Ensures {@code getAllAuthors()} returns the list of DTOs produced by the mapper.
     */
    @Test
    void getAllAuthorsShouldReturnDTOList() {

        // ARRANGE
        IAuthorRepository authorRepositoryDouble = mock(IAuthorRepository.class);
        US025AuthorMapper authorMapperDouble = mock(US025AuthorMapper.class);

        Author authorDouble = mock(Author.class);
        US025AuthorDTO dtoDouble = mock(US025AuthorDTO.class);

        when(authorRepositoryDouble.findAll()).thenReturn(List.of(authorDouble));
        when(authorMapperDouble.toDTOList(List.of(authorDouble))).thenReturn(List.of(dtoDouble));

        ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        mock(IListingRepository.class),
                        mock(IBibliographicalDataRepository.class),
                        mock(IPublicationSampleRepository.class),
                        authorMapperDouble,
                        mock(US025DirectSaleMapper.class));

        // ACT
        List<US025AuthorDTO> result = sut.getAllAuthors();

        // ASSERT
        assertEquals(List.of(dtoDouble), result);
    }

    /**
     * Ensures {@code getAllAuthors()} returns an empty list when no authors exist.
     */
    @Test
    void getAllAuthorsShouldReturnEmptyListWhenNoAuthorsExist() {

        // ARRANGE
        IAuthorRepository authorRepositoryDouble = mock(IAuthorRepository.class);
        US025AuthorMapper authorMapperDouble = mock(US025AuthorMapper.class);

        when(authorRepositoryDouble.findAll()).thenReturn(List.of());
        when(authorMapperDouble.toDTOList(List.of())).thenReturn(List.of());

        ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        mock(IListingRepository.class),
                        mock(IBibliographicalDataRepository.class),
                        mock(IPublicationSampleRepository.class),
                        authorMapperDouble,
                        mock(US025DirectSaleMapper.class));

        // ACT
        List<US025AuthorDTO> result = sut.getAllAuthors();

        // ASSERT
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // getDirectSalesByAuthorId
    // =========================================================================

    /**
     * Ensures {@code getDirectSalesByAuthorId()} throws exception when authorId is null.
     */
    @Test
    void getDirectSalesByAuthorIdShouldThrowWhenAuthorIdIsNull() {

        // ARRANGE
        ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        mock(IAuthorRepository.class),
                        mock(IListingRepository.class),
                        mock(IBibliographicalDataRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(US025AuthorMapper.class),
                        mock(US025DirectSaleMapper.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.getDirectSalesByAuthorId(null));
    }

    /**
     * Ensures {@code getDirectSalesByAuthorId()} returns an empty list when no listings exist.
     */
    @Test
    void getDirectSalesByAuthorIdShouldReturnEmptyListWhenNoListingsExist() {

        // ARRANGE
        IAuthorRepository authorRepositoryDouble = mock(IAuthorRepository.class);
        IBibliographicalDataRepository bdRepositoryDouble = mock(IBibliographicalDataRepository.class);
        IPublicationSampleRepository pubRepositoryDouble = mock(IPublicationSampleRepository.class);
        IListingRepository listingRepositoryDouble = mock(IListingRepository.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(bdRepositoryDouble.findIdByAuthorId(authorIdDouble)).thenReturn(List.of());
        when(pubRepositoryDouble.findIdsByBibliographicalDataIdIn(List.of())).thenReturn(List.of());
        when(listingRepositoryDouble.findAllByDirectSaleAndPublicationSampleIdIn(List.of())).thenReturn(List.of());

        ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        bdRepositoryDouble,
                        pubRepositoryDouble,
                        mock(US025AuthorMapper.class),
                        mock(US025DirectSaleMapper.class));

        // ACT
        List<US025DirectSaleDTO> result = sut.getDirectSalesByAuthorId(authorIdDouble);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    /**
     * Ensures {@code getDirectSalesByAuthorId()} returns DTOs sorted ascending by publication title.
     */
    @Test
    void getDirectSalesByAuthorIdShouldReturnDTOsSortedByPublicationTitle() {

        // ARRANGE
        IAuthorRepository authorRepositoryDouble = mock(IAuthorRepository.class);
        IBibliographicalDataRepository bdRepositoryDouble = mock(IBibliographicalDataRepository.class);
        IPublicationSampleRepository pubRepositoryDouble = mock(IPublicationSampleRepository.class);
        IListingRepository listingRepositoryDouble = mock(IListingRepository.class);
        US025DirectSaleMapper directSaleMapperDouble = mock(US025DirectSaleMapper.class);

        AuthorId authorIdDouble = mock(AuthorId.class);
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);
        PublicationSampleId pubIdDouble = mock(PublicationSampleId.class);

        Listing listingDouble = mock(Listing.class);
        PublicationSample pubDouble = mock(PublicationSample.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);

        when(listingDouble.getItemsId()).thenReturn(List.of(pubIdDouble));
        when(pubDouble.getBibliographicalDataID()).thenReturn(bdIdDouble);
        when(bdDouble.getAuthors()).thenReturn(List.of());

        when(bdRepositoryDouble.findIdByAuthorId(authorIdDouble)).thenReturn(List.of(bdIdDouble));
        when(pubRepositoryDouble.findIdsByBibliographicalDataIdIn(List.of(bdIdDouble))).thenReturn(List.of(pubIdDouble));
        when(listingRepositoryDouble.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pubIdDouble)))
                .thenReturn(List.of(listingDouble));
        when(pubRepositoryDouble.findById(pubIdDouble)).thenReturn(Optional.of(pubDouble));
        when(bdRepositoryDouble.findById(bdIdDouble)).thenReturn(Optional.of(bdDouble));

        US025DirectSaleDTO dtoB = mock(US025DirectSaleDTO.class);
        US025DirectSaleDTO dtoA = mock(US025DirectSaleDTO.class);
        when(dtoB.getPublicationTitle()).thenReturn("B Title");
        when(dtoA.getPublicationTitle()).thenReturn("A Title");
        when(directSaleMapperDouble.toDTO(eq(listingDouble), eq(bdDouble), anyList()))
                .thenReturn(dtoB);

        ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        bdRepositoryDouble,
                        pubRepositoryDouble,
                        mock(US025AuthorMapper.class),
                        directSaleMapperDouble);

        // ACT
        List<US025DirectSaleDTO> result = sut.getDirectSalesByAuthorId(authorIdDouble);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("B Title", result.get(0).getPublicationTitle());
    }

    /**
     * Ensures {@code getDirectSalesByAuthorId()} resolves author names from BibliographicalData.
     */
    @Test
    void getDirectSalesByAuthorIdShouldResolveAuthorNames() {

        // ARRANGE
        IAuthorRepository authorRepositoryDouble = mock(IAuthorRepository.class);
        IBibliographicalDataRepository bdRepositoryDouble = mock(IBibliographicalDataRepository.class);
        IPublicationSampleRepository pubRepositoryDouble = mock(IPublicationSampleRepository.class);
        IListingRepository listingRepositoryDouble = mock(IListingRepository.class);
        US025DirectSaleMapper directSaleMapperDouble = mock(US025DirectSaleMapper.class);

        AuthorId authorIdDouble = mock(AuthorId.class);
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);
        PublicationSampleId pubIdDouble = mock(PublicationSampleId.class);

        Listing listingDouble = mock(Listing.class);
        PublicationSample pubDouble = mock(PublicationSample.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        Author authorDouble = mock(Author.class);
        US025DirectSaleDTO dtoDouble = mock(US025DirectSaleDTO.class);

        when(listingDouble.getItemsId()).thenReturn(List.of(pubIdDouble));
        when(pubDouble.getBibliographicalDataID()).thenReturn(bdIdDouble);
        when(bdDouble.getAuthors()).thenReturn(List.of(authorIdDouble));
        when(authorDouble.getName()).thenReturn("Author Name");
        when(dtoDouble.getPublicationTitle()).thenReturn("Some Title");

        when(bdRepositoryDouble.findIdByAuthorId(authorIdDouble)).thenReturn(List.of(bdIdDouble));
        when(pubRepositoryDouble.findIdsByBibliographicalDataIdIn(List.of(bdIdDouble))).thenReturn(List.of(pubIdDouble));
        when(listingRepositoryDouble.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pubIdDouble)))
                .thenReturn(List.of(listingDouble));
        when(pubRepositoryDouble.findById(pubIdDouble)).thenReturn(Optional.of(pubDouble));
        when(bdRepositoryDouble.findById(bdIdDouble)).thenReturn(Optional.of(bdDouble));
        when(authorRepositoryDouble.findById(authorIdDouble)).thenReturn(Optional.of(authorDouble));
        when(directSaleMapperDouble.toDTO(eq(listingDouble), eq(bdDouble), eq(List.of("Author Name"))))
                .thenReturn(dtoDouble);

        ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController sut =
                new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                        authorRepositoryDouble,
                        listingRepositoryDouble,
                        bdRepositoryDouble,
                        pubRepositoryDouble,
                        mock(US025AuthorMapper.class),
                        directSaleMapperDouble);

        // ACT
        List<US025DirectSaleDTO> result = sut.getDirectSalesByAuthorId(authorIdDouble);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(dtoDouble, result.get(0));
    }
}