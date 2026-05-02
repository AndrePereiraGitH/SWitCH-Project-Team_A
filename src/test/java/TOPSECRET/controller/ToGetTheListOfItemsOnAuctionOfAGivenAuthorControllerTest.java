package TOPSECRET.controller;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US026AuctionDTO;
import TOPSECRET.dto.US026AuctionMapper;
import TOPSECRET.dto.US026AuthorDTO;
import TOPSECRET.dto.US026AuthorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class ToGetTheListOfItemsOnAuctionOfAGivenAuthorControllerTest {

    private IAuthorRepository authorRepository;
    private IBibliographicalDataRepository biblioRepository;
    private IPublicationSampleRepository pubRepo;
    private IListingRepository listingRepo;
    private IAuctionRepository auctionRepository;
    private US026AuctionMapper auctionMapper;

    @BeforeEach
    void setUp() {
        authorRepository = mock(IAuthorRepository.class);
        biblioRepository = mock(IBibliographicalDataRepository.class);
        pubRepo = mock(IPublicationSampleRepository.class);
        listingRepo = mock(IListingRepository.class);
        auctionRepository = mock(IAuctionRepository.class);
        auctionMapper = mock(US026AuctionMapper.class);
    }

    @ParameterizedTest
    @MethodSource("invalidConstructorArguments")
    void constructor_shouldThrowIllegalArgumentException_whenAnyDependencyIsNull(
            IAuthorRepository authorRepository,
            IBibliographicalDataRepository biblioRepository,
            IPublicationSampleRepository pubRepo,
            IListingRepository listingRepo,
            IAuctionRepository auctionRepository,
            US026AuctionMapper mapper
    ) {
        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new ToGetTheListOfItemsOnAuctionOfAGivenAuthorController(
                        authorRepository,
                        biblioRepository,
                        pubRepo,
                        listingRepo,
                        auctionRepository,
                        mapper
                )
        );

        // Assert
        assertEquals("Controller dependencies cannot be null", exception.getMessage());
    }

    @Test
    void getAllAuthors_shouldReturnAuthorDTOList_whenRepositoryReturnsAuthors() {
        // Arrange
        ToGetTheListOfItemsOnAuctionOfAGivenAuthorController controller = createController();

        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);
        List<Author> authors = List.of(author1, author2);

        US026AuthorDTO dto1 = mock(US026AuthorDTO.class);
        US026AuthorDTO dto2 = mock(US026AuthorDTO.class);
        List<US026AuthorDTO> expected = List.of(dto1, dto2);

        when(authorRepository.findAll()).thenReturn(authors);

        try (MockedStatic<US026AuthorMapper> mockedMapper = mockStatic(US026AuthorMapper.class)) {
            mockedMapper.when(() -> US026AuthorMapper.fromDomainList(authors)).thenReturn(expected);

            // Act
            List<US026AuthorDTO> result = controller.getAllAuthors();

            // Assert
            assertNotNull(result);
            assertEquals(expected, result);
            assertEquals(2, result.size());
        }
    }

    @Test
    void getAllAuthors_shouldReturnEmptyList_whenRepositoryReturnsEmptyList() {
        // Arrange
        ToGetTheListOfItemsOnAuctionOfAGivenAuthorController controller = createController();

        List<Author> authors = List.of();
        List<US026AuthorDTO> expected = List.of();

        when(authorRepository.findAll()).thenReturn(authors);

        try (MockedStatic<US026AuthorMapper> mockedMapper = mockStatic(US026AuthorMapper.class)) {
            mockedMapper.when(() -> US026AuthorMapper.fromDomainList(authors)).thenReturn(expected);

            // Act
            List<US026AuthorDTO> result = controller.getAllAuthors();

            // Assert
            assertNotNull(result);
            assertEquals(expected, result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void getAuctionsByAuthorId_shouldThrowIllegalArgumentException_whenAuthorIdIsNull() {
        // Arrange
        ToGetTheListOfItemsOnAuctionOfAGivenAuthorController controller = createController();

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                controller.getAuctionsByAuthorId(null)
        );

        // Assert
        assertEquals("authorId cannot be null.", exception.getMessage());
    }

    @Test
    void getAuctionsByAuthorId_shouldReturnAuctionDTOList_whenAuthorHasAuctions() throws InstanceNotFoundException {
        // Arrange
        ToGetTheListOfItemsOnAuctionOfAGivenAuthorController controller = createController();

        AuthorId authorId = mock(AuthorId.class);

        BibliographicalDataID bdId1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId2 = mock(BibliographicalDataID.class);
        List<BibliographicalDataID> bibliographicalDataIds = List.of(bdId1, bdId2);

        PublicationSampleId sampleId1 = mock(PublicationSampleId.class);
        PublicationSampleId sampleId2 = mock(PublicationSampleId.class);
        List<PublicationSampleId> publicationSampleIds = List.of(sampleId1, sampleId2);

        ListingId listingId1 = mock(ListingId.class);
        ListingId listingId2 = mock(ListingId.class);
        List<ListingId> listingIds = List.of(listingId1, listingId2);

        Auction auction1 = mock(Auction.class);
        Auction auction2 = mock(Auction.class);
        List<Auction> auctions = List.of(auction1, auction2);

        US026AuctionDTO dto1 = mock(US026AuctionDTO.class);
        US026AuctionDTO dto2 = mock(US026AuctionDTO.class);
        List<US026AuctionDTO> expected = List.of(dto1, dto2);

        when(biblioRepository.findIdByAuthorId(authorId)).thenReturn(bibliographicalDataIds);
        when(pubRepo.findPublicationSamplesIdsByBiblioDataIds(bibliographicalDataIds)).thenReturn(publicationSampleIds);
        when(listingRepo.findIdsByPublicationSampleIdIn(publicationSampleIds)).thenReturn(listingIds);
        when(auctionRepository.findAuctionsByListingId(listingIds)).thenReturn(auctions);
        when(auctionMapper.fromDomainList(auctions)).thenReturn(expected);

        // Act
        List<US026AuctionDTO> result = controller.getAuctionsByAuthorId(authorId);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        assertEquals(2, result.size());
    }

    @Test
    void getAuctionsByAuthorId_shouldReturnEmptyList_whenAuthorHasNoBibliographicalData() throws InstanceNotFoundException {
        // Arrange
        ToGetTheListOfItemsOnAuctionOfAGivenAuthorController controller = createController();

        AuthorId authorId = mock(AuthorId.class);

        List<BibliographicalDataID> bibliographicalDataIds = List.of();
        List<PublicationSampleId> publicationSampleIds = List.of();
        List<ListingId> listingIds = List.of();
        List<Auction> auctions = List.of();
        List<US026AuctionDTO> expected = List.of();

        when(biblioRepository.findIdByAuthorId(authorId)).thenReturn(bibliographicalDataIds);
        when(pubRepo.findPublicationSamplesIdsByBiblioDataIds(bibliographicalDataIds)).thenReturn(publicationSampleIds);
        when(listingRepo.findIdsByPublicationSampleIdIn(publicationSampleIds)).thenReturn(listingIds);
        when(auctionRepository.findAuctionsByListingId(listingIds)).thenReturn(auctions);
        when(auctionMapper.fromDomainList(auctions)).thenReturn(expected);

        // Act
        List<US026AuctionDTO> result = controller.getAuctionsByAuthorId(authorId);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAuctionsByAuthorId_shouldPropagateInstanceNotFoundException_whenMapperThrowsException() throws InstanceNotFoundException {
        // Arrange
        ToGetTheListOfItemsOnAuctionOfAGivenAuthorController controller = createController();

        AuthorId authorId = mock(AuthorId.class);

        List<BibliographicalDataID> bibliographicalDataIds = List.of(mock(BibliographicalDataID.class));
        List<PublicationSampleId> publicationSampleIds = List.of(mock(PublicationSampleId.class));
        List<ListingId> listingIds = List.of(mock(ListingId.class));
        List<Auction> auctions = List.of(mock(Auction.class));

        when(biblioRepository.findIdByAuthorId(authorId)).thenReturn(bibliographicalDataIds);
        when(pubRepo.findPublicationSamplesIdsByBiblioDataIds(bibliographicalDataIds)).thenReturn(publicationSampleIds);
        when(listingRepo.findIdsByPublicationSampleIdIn(publicationSampleIds)).thenReturn(listingIds);
        when(auctionRepository.findAuctionsByListingId(listingIds)).thenReturn(auctions);
        when(auctionMapper.fromDomainList(auctions)).thenThrow(new InstanceNotFoundException("Listing not found"));

        // Act + Assert
        InstanceNotFoundException exception = assertThrows(InstanceNotFoundException.class, () ->
                controller.getAuctionsByAuthorId(authorId)
        );

        assertEquals("Listing not found", exception.getMessage());
    }

    private ToGetTheListOfItemsOnAuctionOfAGivenAuthorController createController() {
        return new ToGetTheListOfItemsOnAuctionOfAGivenAuthorController(
                authorRepository,
                biblioRepository,
                pubRepo,
                listingRepo,
                auctionRepository,
                auctionMapper
        );
    }

    private static Stream<Arguments> invalidConstructorArguments() {
        return Stream.of(
                of(null, mock(IBibliographicalDataRepository.class), mock(IPublicationSampleRepository.class), mock(IListingRepository.class), mock(IAuctionRepository.class), mock(US026AuctionMapper.class)),
                of(mock(IAuthorRepository.class), null, mock(IPublicationSampleRepository.class), mock(IListingRepository.class), mock(IAuctionRepository.class), mock(US026AuctionMapper.class)),
                of(mock(IAuthorRepository.class), mock(IBibliographicalDataRepository.class), null, mock(IListingRepository.class), mock(IAuctionRepository.class), mock(US026AuctionMapper.class)),
                of(mock(IAuthorRepository.class), mock(IBibliographicalDataRepository.class), mock(IPublicationSampleRepository.class), null, mock(IAuctionRepository.class), mock(US026AuctionMapper.class)),
                of(mock(IAuthorRepository.class), mock(IBibliographicalDataRepository.class), mock(IPublicationSampleRepository.class), mock(IListingRepository.class), null, mock(US026AuctionMapper.class)),
                of(mock(IAuthorRepository.class), mock(IBibliographicalDataRepository.class), mock(IPublicationSampleRepository.class), mock(IListingRepository.class), mock(IAuctionRepository.class), null)
        );
    }
}