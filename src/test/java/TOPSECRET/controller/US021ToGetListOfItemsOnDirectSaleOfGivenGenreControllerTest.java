package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US021DirSaleListingDTO;
import TOPSECRET.dto.US021DirSaleListingMapper;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class US021ToGetListOfItemsOnDirectSaleOfGivenGenreControllerTest {

    // ============================================================
    // Constructor Tests
    // ============================================================

    @Test
    void ensureControllerIsCreatedSuccessfully() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        // Act
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                );

        // Assert - if controller is null, the test will fail with NullPointerException
        controller.getAllGenres();
    }


    @Test
    void ensureThrowsExceptionWhenGenreRepositoryIsNull() {
        // Arrange
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        null,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                ));
    }

    @Test
    void ensureThrowsExceptionWhenListingRepositoryIsNull() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        null,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                ));
    }

    @Test
    void ensureThrowsExceptionWhenPublicationSampleRepositoryIsNull() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        null,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                ));
    }

    @Test
    void ensureThrowsExceptionWhenBibliographicalDataRepositoryIsNull() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        null,
                        mapperMock
                ));
    }

    @Test
    void ensureThrowsExceptionWhenMapperIsNull() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        null
                ));
    }

    @Test
    void ensureThrowsExceptionWhenAllDependenciesAreNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, null, null, null, null));
    }


    // ============================================================
    // getAllGenres() Tests
    // ============================================================

    @Test
    void ensureGetAllGenresReturnsEmptyListWhenRepositoryHasNoGenres() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        when(genreRepositoryMock.getAllGenres()).thenReturn(new ArrayList<>());

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                );

        // Act
        List<Genre> result = controller.getAllGenres();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureGetAllGenresReturnsCorrectListWhenRepositoryHasGenres() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);
        List<Genre> expectedGenres = List.of(genre1, genre2);
        when(genreRepositoryMock.getAllGenres()).thenReturn(expectedGenres);

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                );

        // Act
        List<Genre> result = controller.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedGenres, result);
    }


    // ============================================================
    // getListOfItemsOnDirectSale() Tests
    // ============================================================

    @Test
    void ensureThrowsExceptionWhenSelectedGenreIsNull() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.getListOfItemsOnDirectSale(null));
    }

    @Test
    void ensureReturnsEmptyListWhenNoDirectSaleListingsExist() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");
        when(listingRepositoryMock.findAllDirectSaleListings()).thenReturn(new ArrayList<>());
        when(mapperMock.toDTOs(anyList())).thenReturn(new ArrayList<>());

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                );

        // Act
        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureReturnsEmptyListWhenNoListingsContainMatchingGenre() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");
        PublicationSampleId psId = new PublicationSampleId("ps-1");
        Listing listingMock = mock(Listing.class);
        when(listingMock.getItemsId()).thenReturn(List.of(psId));
        when(listingRepositoryMock.findAllDirectSaleListings()).thenReturn(List.of(listingMock));
        when(publicationSampleRepositoryMock.findPublicationSampleById(psId))
                .thenReturn(Optional.empty());

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                );

        // Act
        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureReturnsEmptyListWhenBibliographicalDataNotFound() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");
        PublicationSampleId psId = new PublicationSampleId("ps-1");
        Listing listingMock = mock(Listing.class);
        when(listingMock.getItemsId()).thenReturn(List.of(psId));

        PublicationSample publicationSampleMock = mock(PublicationSample.class);
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);

        when(listingRepositoryMock.findAllDirectSaleListings()).thenReturn(List.of(listingMock));
        when(publicationSampleRepositoryMock.findPublicationSampleById(psId))
                .thenReturn(Optional.of(publicationSampleMock));
        when(publicationSampleMock.getBibliographicalDataID()).thenReturn(bdId);
        when(bibliographicalDataRepositoryMock.findBibliographicalDataById(bdId))
                .thenReturn(Optional.empty());

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                );

        // Act
        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureReturnsListWithOneListingWhenGenreMatches() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");
        PublicationSampleId psId = new PublicationSampleId("ps-1");
        Listing listingMock = mock(Listing.class);
        when(listingMock.getItemsId()).thenReturn(List.of(psId));

        PublicationSample publicationSampleMock = mock(PublicationSample.class);
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataMock = mock(BibliographicalData.class);

        US021DirSaleListingDTO expectedDTO = mock(US021DirSaleListingDTO.class);

        when(listingRepositoryMock.findAllDirectSaleListings()).thenReturn(List.of(listingMock));
        when(publicationSampleRepositoryMock.findPublicationSampleById(psId))
                .thenReturn(Optional.of(publicationSampleMock));
        when(publicationSampleMock.getBibliographicalDataID()).thenReturn(bdId);
        when(bibliographicalDataRepositoryMock.findBibliographicalDataById(bdId))
                .thenReturn(Optional.of(bibliographicalDataMock));
        when(bibliographicalDataMock.hasGenre(selectedGenre)).thenReturn(true);
        when(mapperMock.toDTOs(List.of(listingMock))).thenReturn(List.of(expectedDTO));

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                );

        // Act
        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedDTO, result.get(0));
    }

    @Test
    void ensureDoesNotAddDuplicateListingsWhenMultipleItemsMatchGenre() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");
        PublicationSampleId psId1 = new PublicationSampleId("ps-1");
        PublicationSampleId psId2 = new PublicationSampleId("ps-2");

        Listing listingMock = mock(Listing.class);
        when(listingMock.getItemsId()).thenReturn(List.of(psId1, psId2));

        PublicationSample publicationSampleMock1 = mock(PublicationSample.class);
        BibliographicalDataID bdId1 = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataMock1 = mock(BibliographicalData.class);

        when(listingRepositoryMock.findAllDirectSaleListings()).thenReturn(List.of(listingMock));

        when(publicationSampleRepositoryMock.findPublicationSampleById(psId1))
                .thenReturn(Optional.of(publicationSampleMock1));
        when(publicationSampleMock1.getBibliographicalDataID()).thenReturn(bdId1);
        when(bibliographicalDataRepositoryMock.findBibliographicalDataById(bdId1))
                .thenReturn(Optional.of(bibliographicalDataMock1));
        when(bibliographicalDataMock1.hasGenre(selectedGenre)).thenReturn(true);

        US021DirSaleListingDTO expectedDTO = mock(US021DirSaleListingDTO.class);
        when(mapperMock.toDTOs(List.of(listingMock))).thenReturn(List.of(expectedDTO));

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                );

        // Act
        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void ensureFiltersOutListingsWithNoMatchingGenre() {
        // Arrange
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");
        PublicationSampleId psId1 = new PublicationSampleId("ps-1");
        PublicationSampleId psId2 = new PublicationSampleId("ps-2");

        Listing listingWithMatch = mock(Listing.class);
        when(listingWithMatch.getItemsId()).thenReturn(List.of(psId1));

        Listing listingWithoutMatch = mock(Listing.class);
        when(listingWithoutMatch.getItemsId()).thenReturn(List.of(psId2));

        PublicationSample publicationSampleMock1 = mock(PublicationSample.class);
        PublicationSample publicationSampleMock2 = mock(PublicationSample.class);
        BibliographicalDataID bdId1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId2 = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataMock1 = mock(BibliographicalData.class);
        BibliographicalData bibliographicalDataMock2 = mock(BibliographicalData.class);

        when(listingRepositoryMock.findAllDirectSaleListings())
                .thenReturn(List.of(listingWithMatch, listingWithoutMatch));

        // Listing with match setup
        when(publicationSampleRepositoryMock.findPublicationSampleById(psId1))
                .thenReturn(Optional.of(publicationSampleMock1));
        when(publicationSampleMock1.getBibliographicalDataID()).thenReturn(bdId1);
        when(bibliographicalDataRepositoryMock.findBibliographicalDataById(bdId1))
                .thenReturn(Optional.of(bibliographicalDataMock1));
        when(bibliographicalDataMock1.hasGenre(selectedGenre)).thenReturn(true);

        // Listing without match setup
        when(publicationSampleRepositoryMock.findPublicationSampleById(psId2))
                .thenReturn(Optional.of(publicationSampleMock2));
        when(publicationSampleMock2.getBibliographicalDataID()).thenReturn(bdId2);
        when(bibliographicalDataRepositoryMock.findBibliographicalDataById(bdId2))
                .thenReturn(Optional.of(bibliographicalDataMock2));
        when(bibliographicalDataMock2.hasGenre(selectedGenre)).thenReturn(false);

        US021DirSaleListingDTO expectedDTO = mock(US021DirSaleListingDTO.class);
        when(mapperMock.toDTOs(List.of(listingWithMatch))).thenReturn(List.of(expectedDTO));

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock,
                        listingRepositoryMock,
                        publicationSampleRepositoryMock,
                        bibliographicalDataRepositoryMock,
                        mapperMock
                );

        // Act
        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        // Assert
        assertEquals(1, result.size());
    }


}


