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
import TOPSECRET.dto.US022GenreDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToGetListOfItemsOnAuctionOfGivenGenreControllerTest {

    private IGenreRepository genreRepository = mock(IGenreRepository.class);
    private IAuctionRepository auctionRepository = mock(IAuctionRepository.class);
    private IBibliographicalDataRepository bibliographicalDataRepository = mock(IBibliographicalDataRepository.class);
    private IPublicationSampleRepository publicationSampleRepository = mock(IPublicationSampleRepository.class);
    private IListingRepository listingRepository = mock(IListingRepository.class);

    private ToGetListOfItemsOnAuctionOfGivenGenreController controller;

    @BeforeEach
    void setUp() {
        controller = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionRepository,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository
        );
    }

    @Test
    void shouldThrowExceptionWhenAuctionRepositoryIsNull() {

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        null,
                        genreRepository,
                        listingRepository,
                        bibliographicalDataRepository,
                        publicationSampleRepository
                )
        );

        assertEquals(
                "AuctionRepository cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenGenreRepositoryIsNull() {

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        null,
                        listingRepository,
                        bibliographicalDataRepository,
                        publicationSampleRepository
                )
        );

        assertEquals(
                "GenreRepository cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenListingRepositoryIsNull() {

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        genreRepository,
                        null,
                        bibliographicalDataRepository,
                        publicationSampleRepository
                )
        );

        assertEquals(
                "ListingRepository cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBDRepositoryIsNull() {

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        genreRepository,
                        listingRepository,
                        null,
                        publicationSampleRepository
                )
        );

        assertEquals(
                "BibliographicalDataRepository cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPubSampleRepositoryIsNull() {

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepository,
                        genreRepository,
                        listingRepository,
                        bibliographicalDataRepository,
                        null
                )
        );

        assertEquals(
                "PublicationSampleRepository cannot be null", ex.getMessage());
    }

    @Test
    void shouldReturnAuctionsForGivenGenre() {

        // Arrange
        GenreId genreId = mock(GenreId.class);
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(genreId);

        US022GenreDTO genreDto = mock(US022GenreDTO.class);
        when(genreDto.genreId()).thenReturn(genreId);

        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        PublicationSampleId itemId = mock(PublicationSampleId.class);
        ListingId listingId = mock(ListingId.class);
        AuctionId auctionId = mock(AuctionId.class);

        List<BibliographicalDataID> bdIds = List.of(bdId);
        List<PublicationSampleId> itemIds = List.of(itemId);
        List<ListingId> listingIds = List.of(listingId);


        Auction auction = mock(Auction.class);
        List<Auction> auctions = List.of(auction);

        when(auction.id()).thenReturn(auctionId);
        when(auction.getListingId()).thenReturn(listingId);

        when(auctionId.getId()).thenReturn("auction-1");
        when(listingId.getId()).thenReturn("listing-1");

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(bibliographicalDataRepository.findAllByGenreId(genreId)).thenReturn(bdIds);
        when(publicationSampleRepository.findIdsByBibliographicalDataIdIn(anyList())).thenReturn(itemIds);
        when(listingRepository.findIdsByPublicationSampleIdIn(anyList())).thenReturn(listingIds);
        when(auctionRepository.findByListingIdIn(anyList())).thenReturn(auctions);

        // Act
        List<US022AuctionDTO> result =
                controller.findListOfItemsOnAuctionByGenreId(genreDto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        US022AuctionDTO dto = result.get(0);
        assertEquals("auction-1", dto.auctionId());
        assertEquals("listing-1", dto.listingId());
    }


    @Test
    void shouldReturnEmptyListWhenNoAuctionsFound() {

        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        Genre genreDouble = mock(Genre.class);

        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);
        when(genreDtoDouble.genreId()).thenReturn(genreIdDouble);

        when(genreRepository.findById(genreIdDouble)).thenReturn(Optional.of(genreDouble));
        when(genreDouble.id()).thenReturn(genreIdDouble);

        when(bibliographicalDataRepository.findAllByGenreId(genreIdDouble)).thenReturn(List.of());
        when(publicationSampleRepository.findIdsByBibliographicalDataIdIn(List.of())).thenReturn(List.of());
        when(listingRepository.findIdsByPublicationSampleIdIn(List.of())).thenReturn(List.of());
        when(auctionRepository.findByListingIdIn(List.of())).thenReturn(List.of());

        // Act
        List<US022AuctionDTO> result =
                controller.findListOfItemsOnAuctionByGenreId(genreDtoDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDTOisNull() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.findListOfItemsOnAuctionByGenreId(null));
    }

    @Test
    void shouldThrowExceptionWithCorrectMessage() {

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.findListOfItemsOnAuctionByGenreId(null)
        );

        assertEquals("genre cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {

        // Arrange
        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);
        when(genreDtoDouble.genreId()).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.findListOfItemsOnAuctionByGenreId(genreDtoDouble));
    }

    @Test
    void shouldReturnAllGenresMappedCorrectly() {

        // Arrange
        Genre genreDouble1 = mock(Genre.class);
        Genre genreDouble2 = mock(Genre.class);

        GenreId idDouble1 = mock(GenreId.class);
        GenreId idDouble2 = mock(GenreId.class);

        when(genreDouble1.getGenreType()).thenReturn("Fantasy");
        when(genreDouble2.getGenreType()).thenReturn("Drama");

        when(genreDouble1.id()).thenReturn(idDouble1);
        when(genreDouble2.id()).thenReturn(idDouble2);

        List<Genre> genres = List.of(genreDouble1, genreDouble2);

        when(genreRepository.findAll()).thenReturn(genres);

        // Act
        List<US022GenreDTO> result = controller.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).name());
        assertEquals("Drama", result.get(1).name());
    }

    @Test
    void shouldThrowExceptionWhenGenreIdIsNull() {

        // Arrange
        US022GenreDTO genreDto = mock(US022GenreDTO.class);
        when(genreDto.genreId()).thenReturn(null);

        // Act & Assert
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.findListOfItemsOnAuctionByGenreId(genreDto)
        );
        assertEquals("genreId cannot be null", ex.getMessage());
    }

    @Test
    void shouldReturnEmptyListWhenNoBibliographicalDataExists() {

        // Arrange
        GenreId genreId = mock(GenreId.class);

        US022GenreDTO genreDTO = mock(US022GenreDTO.class);
        when(genreDTO.genreId()).thenReturn(genreId);
        when(bibliographicalDataRepository.findAllByGenreId(genreId)).thenReturn(List.of());

        // Act
        List<US022AuctionDTO> result = controller.findListOfItemsOnAuctionByGenreId(genreDTO);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoPublicationSamplesExist() {

        // Arrange
        GenreId genreId = mock(GenreId.class);
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);

        US022GenreDTO genreDTO = mock(US022GenreDTO.class);
        when(genreDTO.genreId()).thenReturn(genreId);
        when(bibliographicalDataRepository.findAllByGenreId(genreId)).thenReturn(List.of(bdId));
        when(publicationSampleRepository.findIdsByBibliographicalDataIdIn(anyList())).thenReturn(List.of());

        // Act
        List<US022AuctionDTO> result =
                controller.findListOfItemsOnAuctionByGenreId(genreDTO);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void shouldReturnEmptyListWhenNoListingsExist() {

        // Arrange
        GenreId genreId = mock(GenreId.class);
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        PublicationSampleId sampleId = mock(PublicationSampleId.class);
        US022GenreDTO genreDTO = mock(US022GenreDTO.class);

        when(genreDTO.genreId()).thenReturn(genreId);
        when(bibliographicalDataRepository.findAllByGenreId(genreId)).thenReturn(List.of(bdId));
        when(publicationSampleRepository.findIdsByBibliographicalDataIdIn(anyList())).thenReturn(List.of(sampleId));
        when(listingRepository.findIdsByPublicationSampleIdIn(anyList())).thenReturn(List.of());

        // Act
        List<US022AuctionDTO> result =
                controller.findListOfItemsOnAuctionByGenreId(genreDTO);

        // Assert
        assertTrue(result.isEmpty());

    }

}

