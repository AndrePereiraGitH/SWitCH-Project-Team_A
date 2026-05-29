package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.US022AuctionDTO;
import TOPSECRET.dto.US022AuctionMapper;
import TOPSECRET.dto.US022GenreDTO;

import TOPSECRET.dto.US022GenreMapper;
import TOPSECRET.services.AuctionService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToGetListOfItemsOnAuctionOfGivenGenreControllerTest {

    private IGenreRepository genreRepository = mock(IGenreRepository.class);
    private AuctionService auctionService = mock(AuctionService.class);
    private IBibliographicalDataRepository bibliographicalDataRepository = mock(IBibliographicalDataRepository.class);
    private IPublicationSampleRepository publicationSampleRepository = mock(IPublicationSampleRepository.class);
    private IListingRepository listingRepository = mock(IListingRepository.class);
    private US022GenreMapper genreMapper = mock(US022GenreMapper.class);
    private US022AuctionMapper auctionMapper = mock(US022AuctionMapper.class);

    private ToGetListOfItemsOnAuctionOfGivenGenreController sut;


    @Test
    void shouldThrowExceptionWhenAuctionServiceIsNull() {

        //Act
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        null,
                        genreRepository,
                        listingRepository,
                        bibliographicalDataRepository,
                        publicationSampleRepository,
                        genreMapper,
                        auctionMapper
                )
        );

        // Assert
        assertEquals(
                "AuctionService cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenGenreRepositoryIsNull() {

        // Act
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionService,
                        null,
                        listingRepository,
                        bibliographicalDataRepository,
                        publicationSampleRepository,
                        genreMapper,
                        auctionMapper
                )
        );

        // Assert
        assertEquals(
                "GenreRepository cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenListingRepositoryIsNull() {


        // Act
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionService,
                        genreRepository,
                        null,
                        bibliographicalDataRepository,
                        publicationSampleRepository,
                        genreMapper,
                        auctionMapper
                )
        );

        // Assert
        assertEquals(
                "ListingRepository cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBDRepositoryIsNull() {

        // Act
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionService,
                        genreRepository,
                        listingRepository,
                        null,
                        publicationSampleRepository,
                        genreMapper,
                        auctionMapper
                )
        );

        // Assert
        assertEquals(
                "BibliographicalDataRepository cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPubSampleRepositoryIsNull() {

        // Act
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionService,
                        genreRepository,
                        listingRepository,
                        bibliographicalDataRepository,
                        null,
                        genreMapper,
                        auctionMapper
                )
        );

        // Assert
        assertEquals(
                "PublicationSampleRepository cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenGenreMapperIsNull() {

        // Act
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionService,
                        genreRepository,
                        listingRepository,
                        bibliographicalDataRepository,
                        publicationSampleRepository,
                        null,
                        auctionMapper
                )
        );

        // Assert
        assertEquals(
                "GenreMapper cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAuctionMapperIsNull() {

        // Act
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionService,
                        genreRepository,
                        listingRepository,
                        bibliographicalDataRepository,
                        publicationSampleRepository,
                        genreMapper,
                        null
                )
        );

        // Assert
        assertEquals(
                "AuctionMapper cannot be null", ex.getMessage());
    }

    @Test
    void shouldReturnAuctionsForGivenGenre() {

        // Arrange
        GenreId genreId = mock(GenreId.class);
        Genre genre = mock(Genre.class);

        when(genre.id()).thenReturn(genreId);

        US022GenreDTO genreDto = mock(US022GenreDTO.class);
        when(genreDto.genreId()).thenReturn(genreId);

        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        PublicationSampleId itemId = mock(PublicationSampleId.class);
        ListingId listingId = mock(ListingId.class);
        AuctionId auctionId = mock(AuctionId.class);

        List<BibliographicalDataId> bdIds = List.of(bdId);
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
        when(auctionService.findByListingIdIn(anyList())).thenReturn(auctions);

        US022AuctionDTO dtoDouble = mock(US022AuctionDTO.class);

        when(auctionMapper.toDTOList(auctions)).thenReturn(List.of(dtoDouble));
        when(dtoDouble.auctionId()).thenReturn("auction-1");
        when(dtoDouble.listingId()).thenReturn("listing-1");

        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );

        // Act
        List<US022AuctionDTO> result =
                sut.findListOfItemsOnAuctionByGenreId(genreDto);

        US022AuctionDTO dto = result.get(0);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
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
        when(auctionService.findByListingIdIn(List.of())).thenReturn(List.of());

        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );

        // Act
        List<US022AuctionDTO> result =
                sut.findListOfItemsOnAuctionByGenreId(genreDtoDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDTOisNull() {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.findListOfItemsOnAuctionByGenreId(null));
    }

    @Test
    void shouldThrowExceptionWithCorrectMessage() {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );
         // Act
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> sut.findListOfItemsOnAuctionByGenreId(null)
        );

        // Assert
        assertEquals("genre cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {

        // Arrange
        US022GenreDTO genreDtoDouble = mock(US022GenreDTO.class);
        when(genreDtoDouble.genreId()).thenReturn(null);

        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.findListOfItemsOnAuctionByGenreId(genreDtoDouble));
    }

    @Test
    void shouldReturnAllGenresMappedCorrectly() {

        // Arrange
        Genre genreDouble1 = mock(Genre.class);
        Genre genreDouble2 = mock(Genre.class);

        US022GenreDTO dto1Double = mock(US022GenreDTO.class);
        US022GenreDTO dto2Double = mock(US022GenreDTO.class);

        when(dto1Double.genreType()).thenReturn("Fantasy");
        when(dto2Double.genreType()).thenReturn("Drama");

        List<Genre> genres = List.of(genreDouble1, genreDouble2);

        List<US022GenreDTO> genreDtoList = List.of(dto1Double, dto2Double);

        when(genreRepository.findAll()).thenReturn(genres);
        when(genreMapper.toDTOList(genres)).thenReturn(genreDtoList);

        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );
        // Act
        List<US022GenreDTO> result = sut.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).genreType());
        assertEquals("Drama", result.get(1).genreType());
    }

    @Test
    void shouldThrowExceptionWhenGenreIdIsNull() {

        // Arrange
        US022GenreDTO genreDto = mock(US022GenreDTO.class);
        when(genreDto.genreId()).thenReturn(null);

        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );

        // Act & Assert
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> sut.findListOfItemsOnAuctionByGenreId(genreDto)
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

        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );

        // Act
        List<US022AuctionDTO> result = sut.findListOfItemsOnAuctionByGenreId(genreDTO);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoPublicationSamplesExist() {

        // Arrange
        GenreId genreId = mock(GenreId.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        US022GenreDTO genreDTO = mock(US022GenreDTO.class);
        when(genreDTO.genreId()).thenReturn(genreId);
        when(bibliographicalDataRepository.findAllByGenreId(genreId)).thenReturn(List.of(bdId));
        when(publicationSampleRepository.findIdsByBibliographicalDataIdIn(anyList())).thenReturn(List.of());

        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );

        // Act
        List<US022AuctionDTO> result =
                sut.findListOfItemsOnAuctionByGenreId(genreDTO);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void shouldReturnEmptyListWhenNoListingsExist() {

        // Arrange
        GenreId genreId = mock(GenreId.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        PublicationSampleId sampleId = mock(PublicationSampleId.class);
        US022GenreDTO genreDTO = mock(US022GenreDTO.class);

        when(genreDTO.genreId()).thenReturn(genreId);
        when(bibliographicalDataRepository.findAllByGenreId(genreId)).thenReturn(List.of(bdId));
        when(publicationSampleRepository.findIdsByBibliographicalDataIdIn(anyList())).thenReturn(List.of(sampleId));
        when(listingRepository.findIdsByPublicationSampleIdIn(anyList())).thenReturn(List.of());

        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );

        // Act
        List<US022AuctionDTO> result =
                sut.findListOfItemsOnAuctionByGenreId(genreDTO);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void shouldReturnAuctionsSortedByDeadline() {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );

        US022GenreDTO genreDto = mock(US022GenreDTO.class);
        GenreId genreId = mock(GenreId.class);

        when(genreDto.genreId()).thenReturn(genreId);

        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        PublicationSampleId itemId = mock(PublicationSampleId.class);
        ListingId listingId = mock(ListingId.class);

        List<Auction> auctions = List.of(mock(Auction.class));
        when(bibliographicalDataRepository.findAllByGenreId(genreId)).thenReturn(List.of(bdId));
        when(publicationSampleRepository.findIdsByBibliographicalDataIdIn(anyList())).thenReturn(List.of(itemId));
        when(listingRepository.findIdsByPublicationSampleIdIn(anyList())).thenReturn(List.of(listingId));
        when(auctionService.findByListingIdInOrderByAuctionDeadlineAsc(anyList())).thenReturn(auctions);

        List<US022AuctionDTO> dtoList = List.of(mock(US022AuctionDTO.class));
        when(auctionMapper.toDTOList(auctions)).thenReturn(dtoList);

        // Act
        List<US022AuctionDTO> result = sut.findListOfItemsOnAuctionByGenreIdSortedByDeadlineAsc(genreDto);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowExceptionGenreDTONull() {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );
        // Act
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> sut.findListOfItemsOnAuctionByGenreIdSortedByDeadlineAsc(null)
        );

        // Assert
        assertEquals("genre cannot be null", ex.getMessage());
    }

    @Test
    void shouldReturnAuctionsSortedByDeadlineAsc() {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfGivenGenreController(
                auctionService,
                genreRepository,
                listingRepository,
                bibliographicalDataRepository,
                publicationSampleRepository,
                genreMapper,
                auctionMapper
        );

        GenreId genreId = mock(GenreId.class);

        US022GenreDTO genreDTO = mock(US022GenreDTO.class);

        when(genreDTO.genreId()).thenReturn(genreId);

        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        PublicationSampleId itemId = mock(PublicationSampleId.class);
        ListingId listingId = mock(ListingId.class);

        List<BibliographicalDataId> bdIds = List.of(bdId);
        List<PublicationSampleId> itemIds = List.of(itemId);
        List<ListingId> listingIds = List.of(listingId);

        Auction auction1 = mock(Auction.class);
        Auction auction2 = mock(Auction.class);

        List<Auction> auctions = List.of(auction1, auction2);

        US022AuctionDTO dto1 = mock(US022AuctionDTO.class);
        US022AuctionDTO dto2 = mock(US022AuctionDTO.class);

        List<US022AuctionDTO> dtoList = List.of(dto1, dto2);

        when(bibliographicalDataRepository.findAllByGenreId(genreId)).thenReturn(bdIds);
        when(publicationSampleRepository.findIdsByBibliographicalDataIdIn(bdIds)).thenReturn(itemIds);

        when(listingRepository.findIdsByPublicationSampleIdIn(itemIds)).thenReturn(listingIds);
        when(auctionService.findByListingIdInOrderByAuctionDeadlineAsc(listingIds)).thenReturn(auctions);
        when(auctionMapper.toDTOList(auctions)).thenReturn(dtoList);

        // Act
        List<US022AuctionDTO> result =
                sut.findListOfItemsOnAuctionByGenreIdSortedByDeadlineAsc(genreDTO);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

}

