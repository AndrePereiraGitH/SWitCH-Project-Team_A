package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US021DirSaleListingDTO;
import TOPSECRET.dto.US021DirSaleListingMapper;
import TOPSECRET.dto.US021GenreDTO;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class US021ToGetListOfItemsOnDirectSaleOfGivenGenreControllerTest {

    // ============================================================
    // Constructor Tests
    // ============================================================

    @Test
    void ensureControllerIsCreatedSuccessfully() {
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

        assertNotNull(controller);
    }

    @Test
    void ensureThrowsExceptionWhenGenreRepositoryIsNull() {
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

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
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

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
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

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
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

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
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);

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
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, null, null, null, null));
    }


    // ============================================================
    // getAllGenres() Tests
    // ============================================================

    @Test
    void ensureGetAllGenresReturnsEmptyListWhenRepositoryHasNoGenres() {
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        when(genreRepositoryMock.getAllGenres()).thenReturn(new ArrayList<>());

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock, listingRepositoryMock,
                        publicationSampleRepositoryMock, bibliographicalDataRepositoryMock, mapperMock
                );

        List<US021GenreDTO> result = controller.getAllGenres();

        assertTrue(result.isEmpty());
    }

    @Test
    void ensureGetAllGenresReturnsDTOsWhenRepositoryHasGenres() {
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);
        GenreID genreId1 = new GenreID("drama");
        GenreID genreId2 = new GenreID("fiction");

        when(genre1.id()).thenReturn(genreId1);
        when(genre1.getGenreType()).thenReturn("Drama");
        when(genre2.id()).thenReturn(genreId2);
        when(genre2.getGenreType()).thenReturn("Fiction");
        when(genreRepositoryMock.getAllGenres()).thenReturn(List.of(genre1, genre2));

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock, listingRepositoryMock,
                        publicationSampleRepositoryMock, bibliographicalDataRepositoryMock, mapperMock
                );

        List<US021GenreDTO> result = controller.getAllGenres();

        assertEquals(2, result.size());
        assertEquals("Drama", result.get(0).name());
        assertEquals("Fiction", result.get(1).name());
    }


    // ============================================================
    // getListOfItemsOnDirectSale() Tests
    // ============================================================

    @Test
    void ensureThrowsExceptionWhenSelectedGenreIsNull() {
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock, listingRepositoryMock,
                        publicationSampleRepositoryMock, bibliographicalDataRepositoryMock, mapperMock
                );

        assertThrows(IllegalArgumentException.class, () ->
                controller.getListOfItemsOnDirectSale(null));
    }

    @Test
    void ensureReturnsEmptyListWhenNoDirectSaleListingsExist() {
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");

        when(bibliographicalDataRepositoryMock.findAllBibliographicalDataByGenreId(selectedGenre))
                .thenReturn(new ArrayList<>());
        when(publicationSampleRepositoryMock.findAllItemsIdByBibliographicalDataId(new ArrayList<>()))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDTOs(new ArrayList<>())).thenReturn(new ArrayList<>());

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock, listingRepositoryMock,
                        publicationSampleRepositoryMock, bibliographicalDataRepositoryMock, mapperMock
                );

        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        assertTrue(result.isEmpty());
    }

    @Test
    void ensureReturnsEmptyListWhenGenreHasNoBibliographicalData() {
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");

        when(bibliographicalDataRepositoryMock.findAllBibliographicalDataByGenreId(selectedGenre))
                .thenReturn(new ArrayList<>());
        when(publicationSampleRepositoryMock.findAllItemsIdByBibliographicalDataId(new ArrayList<>()))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDTOs(new ArrayList<>())).thenReturn(new ArrayList<>());

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock, listingRepositoryMock,
                        publicationSampleRepositoryMock, bibliographicalDataRepositoryMock, mapperMock
                );

        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        assertTrue(result.isEmpty());
    }

    @Test
    void ensureReturnsListingsWhenGenreMatchesDirectSaleListings() {
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        PublicationSampleId psId = new PublicationSampleId("ps-1");
        Listing listingMock = mock(Listing.class);
        US021DirSaleListingDTO expectedDTO = mock(US021DirSaleListingDTO.class);

        List<BibliographicalDataID> bdIds = List.of(bdId);
        List<PublicationSampleId> pubSampleIds = List.of(psId);
        List<Listing> listings = List.of(listingMock);

        when(bibliographicalDataRepositoryMock.findAllBibliographicalDataByGenreId(selectedGenre))
                .thenReturn(bdIds);
        when(publicationSampleRepositoryMock.findAllItemsIdByBibliographicalDataId(bdIds))
                .thenReturn(pubSampleIds);
        when(listingRepositoryMock.findDirectSaleListingsContaining(pubSampleIds))
                .thenReturn(listings);
        when(mapperMock.toDTOs(listings)).thenReturn(List.of(expectedDTO));

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock, listingRepositoryMock,
                        publicationSampleRepositoryMock, bibliographicalDataRepositoryMock, mapperMock
                );

        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        assertEquals(1, result.size());
        assertEquals(expectedDTO, result.get(0));
    }

    @Test
    void ensureReturnsMultipleListingsWhenSeveralMatchGenre() {
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");
        BibliographicalDataID bdId1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId2 = mock(BibliographicalDataID.class);
        PublicationSampleId psId1 = new PublicationSampleId("ps-1");
        PublicationSampleId psId2 = new PublicationSampleId("ps-2");
        Listing listing1 = mock(Listing.class);
        Listing listing2 = mock(Listing.class);
        US021DirSaleListingDTO dto1 = mock(US021DirSaleListingDTO.class);
        US021DirSaleListingDTO dto2 = mock(US021DirSaleListingDTO.class);

        List<BibliographicalDataID> bdIds = List.of(bdId1, bdId2);
        List<PublicationSampleId> pubSampleIds = List.of(psId1, psId2);
        List<Listing> listings = List.of(listing1, listing2);

        when(bibliographicalDataRepositoryMock.findAllBibliographicalDataByGenreId(selectedGenre))
                .thenReturn(bdIds);
        when(publicationSampleRepositoryMock.findAllItemsIdByBibliographicalDataId(bdIds))
                .thenReturn(pubSampleIds);
        when(listingRepositoryMock.findDirectSaleListingsContaining(pubSampleIds))
                .thenReturn(listings);
        when(mapperMock.toDTOs(listings)).thenReturn(List.of(dto1, dto2));

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock, listingRepositoryMock,
                        publicationSampleRepositoryMock, bibliographicalDataRepositoryMock, mapperMock
                );

        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        assertEquals(2, result.size());
    }

    @Test
    void ensureReturnsEmptyListWhenNoPublicationSamplesFoundForBibliographicalData() {
        IGenreRepository genreRepositoryMock = mock(IGenreRepository.class);
        IListingRepository listingRepositoryMock = mock(IListingRepository.class);
        IPublicationSampleRepository publicationSampleRepositoryMock = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalDataRepositoryMock = mock(IBibliographicalDataRepository.class);
        US021DirSaleListingMapper mapperMock = mock(US021DirSaleListingMapper.class);

        GenreID selectedGenre = new GenreID("drama");
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        List<BibliographicalDataID> bdIds = List.of(bdId);

        when(bibliographicalDataRepositoryMock.findAllBibliographicalDataByGenreId(selectedGenre))
                .thenReturn(bdIds);
        when(publicationSampleRepositoryMock.findAllItemsIdByBibliographicalDataId(bdIds))
                .thenReturn(new ArrayList<>());
        when(mapperMock.toDTOs(new ArrayList<>())).thenReturn(new ArrayList<>());

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryMock, listingRepositoryMock,
                        publicationSampleRepositoryMock, bibliographicalDataRepositoryMock, mapperMock
                );

        List<US021DirSaleListingDTO> result = controller.getListOfItemsOnDirectSale(selectedGenre);

        assertTrue(result.isEmpty());
    }
}