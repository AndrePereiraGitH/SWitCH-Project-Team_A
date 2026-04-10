package TOPSECRET.controller;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.US022AuctionDTO;
import TOPSECRET.dto.US022AuctionMapper;
import TOPSECRET.dto.US022GenreDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US022ToGetListOfItemsOnAuctionOfGivenGenreControllerTest {

    private IGenreRepository genreRepository = mock(IGenreRepository.class);
    private IAuctionRepository auctionRepository = mock(IAuctionRepository.class);
    private IBibliographicalDataRepository bibliographicalDataRepository = mock(IBibliographicalDataRepository.class);
    private IPublicationSampleRepository publicationSampleRepository = mock(IPublicationSampleRepository.class);
    private IListingRepository listingRepository = mock(IListingRepository.class);
    private US022AuctionMapper auctionMapper = mock(US022AuctionMapper.class);

    private US022ToGetListOfItemsOnAuctionOfGivenGenreController controller;

    @BeforeEach
    void setUp() {
        controller = new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionRepository,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                auctionMapper
        );
    }

    @Test
    void shouldReturnAuctionsForGivenGenre() {

        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);
        Genre genreDouble = mock(Genre.class);

        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);
        when(genreDtoDouble.genreId()).thenReturn(genreIdDouble);
        when(genreDtoDouble.name()).thenReturn("drama");

        List<BibliographicalDataID> bdIds = List.of(mock(BibliographicalDataID.class));
        List<PublicationSampleId> itemIds = List.of(mock(PublicationSampleId.class));
        List<ListingId> listingIds = List.of(mock(ListingId.class));
        List<Auction> auctions = List.of(mock(Auction.class));

        US022AuctionDTO auctionDtoDouble = mock(US022AuctionDTO.class);
        when(auctionDtoDouble.auctionId()).thenReturn("A1");
        when(auctionDtoDouble.listingId()).thenReturn("L1");

        List<US022AuctionDTO> expectedDTO = List.of(auctionDtoDouble);

        when(genreRepository.findById(genreIdDouble)).thenReturn(genreDouble);
        when(genreDouble.id()).thenReturn(genreIdDouble);

        when(bibliographicalDataRepository.findAllBibliographicalDataByGenreId(genreIdDouble)).thenReturn(bdIds);
        when(publicationSampleRepository.findAllItemsIdByBibliographicalDataId(bdIds)).thenReturn(itemIds);
        when(listingRepository.findAllListingId(itemIds)).thenReturn(listingIds);
        when(auctionRepository.getListOfItemsOnAuctionGivenGenre(listingIds)).thenReturn(auctions);

        when(auctionMapper.toDTOList(auctions)).thenReturn(expectedDTO);

        // Act
        List<US022AuctionDTO> result =
                controller.getListOfItemsOnAuctionGivenGenre(genreDtoDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).auctionId());
        assertEquals("L1", result.get(0).listingId());
    }


    @Test
    void shouldReturnEmptyListWhenNoAuctionsFound() {

        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);
        Genre genreDouble = mock(Genre.class);

        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);
        when(genreDtoDouble.genreId()).thenReturn(genreIdDouble);

        when(genreRepository.findById(genreIdDouble)).thenReturn(genreDouble);
        when(genreDouble.id()).thenReturn(genreIdDouble);

        when(bibliographicalDataRepository.findAllBibliographicalDataByGenreId(genreIdDouble)).thenReturn(List.of());
        when(publicationSampleRepository.findAllItemsIdByBibliographicalDataId(List.of())).thenReturn(List.of());
        when(listingRepository.findAllListingId(List.of())).thenReturn(List.of());
        when(auctionRepository.getListOfItemsOnAuctionGivenGenre(List.of())).thenReturn(List.of());

        when(auctionMapper.toDTOList(List.of())).thenReturn(List.of());

        // Act
        List<US022AuctionDTO> result =
                controller.getListOfItemsOnAuctionGivenGenre(genreDtoDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDTOisNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getListOfItemsOnAuctionGivenGenre(null));
    }

    @Test
    void shouldThrowExceptionWithCorrectMessage() {

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.getListOfItemsOnAuctionGivenGenre(null)
        );

        assertEquals("genre cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() {

        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);

        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);
        when(genreDtoDouble.genreId()).thenReturn(genreIdDouble);

        when(genreRepository.findById(genreIdDouble))
                .thenThrow(new IllegalArgumentException("Genre not found"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getListOfItemsOnAuctionGivenGenre(genreDtoDouble));
    }

    @Test
    void shouldThrowExceptionWhenGenreIdIsNull() {

        // Arrange
        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);
        when(genreDtoDouble.genreId()).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getListOfItemsOnAuctionGivenGenre(genreDtoDouble));
    }

    @Test
    void shouldThrowExceptionWhenGenreReturnedIsNull() {

        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);
        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);

        when(genreDtoDouble.genreId()).thenReturn(genreIdDouble);
        when(genreRepository.findById(genreIdDouble)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getListOfItemsOnAuctionGivenGenre(genreDtoDouble));
    }

    @Test
    void shouldThrowExceptionWhenBibliographicalDataIsNull() {

        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);
        Genre genreDouble = mock(Genre.class);
        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);

        when(genreDtoDouble.genreId()).thenReturn(genreIdDouble);
        when(genreRepository.findById(genreIdDouble)).thenReturn(genreDouble);
        when(genreDouble.id()).thenReturn(genreIdDouble);

        when(bibliographicalDataRepository
                .findAllBibliographicalDataByGenreId(genreIdDouble))
                .thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getListOfItemsOnAuctionGivenGenre(genreDtoDouble));
    }

    @Test
    void shouldThrowExceptionWhenItemsIsNull() {

        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);
        Genre genreDouble = mock(Genre.class);
        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);

        when(genreDtoDouble.genreId()).thenReturn(genreIdDouble);
        when(genreRepository.findById(genreIdDouble)).thenReturn(genreDouble);
        when(genreDouble.id()).thenReturn(genreIdDouble);

        when(bibliographicalDataRepository
                .findAllBibliographicalDataByGenreId(genreIdDouble))
                .thenReturn(List.of());

        when(publicationSampleRepository
                .findAllItemsIdByBibliographicalDataId(any()))
                .thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getListOfItemsOnAuctionGivenGenre(genreDtoDouble));
    }

    @Test
    void shouldThrowExceptionWhenListingIdsIsNull() {

        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);
        Genre genreDouble = mock(Genre.class);
        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);

        when(genreDtoDouble.genreId()).thenReturn(genreIdDouble);
        when(genreRepository.findById(genreIdDouble)).thenReturn(genreDouble);
        when(genreDouble.id()).thenReturn(genreIdDouble);

        when(bibliographicalDataRepository.findAllBibliographicalDataByGenreId(any())).thenReturn(List.of());
        when(publicationSampleRepository.findAllItemsIdByBibliographicalDataId(any())).thenReturn(List.of());
        when(listingRepository.findAllListingId(any())).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getListOfItemsOnAuctionGivenGenre(genreDtoDouble));
    }

    @Test
    void shouldThrowExceptionWhenAuctionsIsNull() {

        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);
        Genre genreDouble = mock(Genre.class);
        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);

        when(genreDtoDouble.genreId()).thenReturn(genreIdDouble);
        when(genreRepository.findById(genreIdDouble)).thenReturn(genreDouble);
        when(genreDouble.id()).thenReturn(genreIdDouble);

        when(bibliographicalDataRepository.findAllBibliographicalDataByGenreId(any())).thenReturn(List.of());
        when(publicationSampleRepository.findAllItemsIdByBibliographicalDataId(any())).thenReturn(List.of());
        when(listingRepository.findAllListingId(any())).thenReturn(List.of());
        when(auctionRepository.getListOfItemsOnAuctionGivenGenre(any())).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getListOfItemsOnAuctionGivenGenre(genreDtoDouble));
    }

    @Test
    void shouldReturnNullWhenMapperReturnsNull() {

        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);
        Genre genreDouble = mock(Genre.class);
        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);

        when(genreDtoDouble.genreId()).thenReturn(genreIdDouble);
        when(genreRepository.findById(genreIdDouble)).thenReturn(genreDouble);
        when(genreDouble.id()).thenReturn(genreIdDouble);

        when(bibliographicalDataRepository.findAllBibliographicalDataByGenreId(any())).thenReturn(List.of());
        when(publicationSampleRepository.findAllItemsIdByBibliographicalDataId(any())).thenReturn(List.of());
        when(listingRepository.findAllListingId(any())).thenReturn(List.of());
        when(auctionRepository.getListOfItemsOnAuctionGivenGenre(any())).thenReturn(List.of());
        when(auctionMapper.toDTOList(any())).thenReturn(null);

        // Act
        List<US022AuctionDTO> result =
                controller.getListOfItemsOnAuctionGivenGenre(genreDtoDouble);

        // Assert
        assertNull(result);
    }

    @Test
    void shouldReturnAllGenresMappedCorrectly() {

        // Arrange
        Genre genreDouble1 = mock(Genre.class);
        Genre genreDouble2 = mock(Genre.class);

        GenreID idDouble1 = mock(GenreID.class);
        GenreID idDouble2 = mock(GenreID.class);

        when(genreDouble1.getGenreType()).thenReturn("Fantasy");
        when(genreDouble2.getGenreType()).thenReturn("Drama");

        when(genreDouble1.id()).thenReturn(idDouble1);
        when(genreDouble2.id()).thenReturn(idDouble2);

        List<Genre> genres = List.of(genreDouble1, genreDouble2);

        when(genreRepository.getAllGenres()).thenReturn(genres);

        // Act
        List<US022GenreDTO> result = controller.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).name());
        assertEquals("Drama", result.get(1).name());
    }
}