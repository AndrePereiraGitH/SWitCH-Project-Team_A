package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US026AuctionDTO;
import TOPSECRET.dto.US026AuctionMapper;
import TOPSECRET.dto.US026AuthorDTO;
import TOPSECRET.dto.US026AuthorMapper;
import TOPSECRET.services.AuctionService;
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

class ToGetTheListOfItemsInAuctionOfAGivenAuthorControllerTest {

    private IAuthorRepository authorRepository;
    private IBibliographicalDataRepository biblioRepository;
    private IPublicationSampleRepository pubRepo;
    private IListingRepository listingRepo;
    private AuctionService auctionService;
    private US026AuctionMapper auctionMapper;

    @BeforeEach
    void setUp() {
        authorRepository = mock(IAuthorRepository.class);
        biblioRepository = mock(IBibliographicalDataRepository.class);
        pubRepo = mock(IPublicationSampleRepository.class);
        listingRepo = mock(IListingRepository.class);
        auctionService = mock(AuctionService.class);
        auctionMapper = mock(US026AuctionMapper.class);
    }

    @ParameterizedTest
    @MethodSource("invalidConstructorArguments")
    void constructor_shouldThrowIllegalArgumentException_whenAnyDependencyIsNull(
            IAuthorRepository authorRepository,
            IBibliographicalDataRepository biblioRepository,
            IPublicationSampleRepository pubRepo,
            IListingRepository listingRepo,
            AuctionService auctionService,
            US026AuctionMapper mapper,
            String expected
    ) {
        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new ToGetTheListOfItemsInAuctionOfAGivenAuthorController(
                        authorRepository,
                        biblioRepository,
                        pubRepo,
                        listingRepo,
                        auctionService,
                        mapper
                )
        );

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void getAllAuthors_shouldReturnAuthorDTOList_whenRepositoryReturnsAuthors() {
        // Arrange
        ToGetTheListOfItemsInAuctionOfAGivenAuthorController controller = createController();

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
        ToGetTheListOfItemsInAuctionOfAGivenAuthorController controller = createController();

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
        ToGetTheListOfItemsInAuctionOfAGivenAuthorController controller = createController();

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
        ToGetTheListOfItemsInAuctionOfAGivenAuthorController controller = createController();

        AuthorId authorId = mock(AuthorId.class);

        BibliographicalDataId bdId1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId2 = mock(BibliographicalDataId.class);
        List<BibliographicalDataId> bibliographicalDataIds = List.of(bdId1, bdId2);

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
        when(pubRepo.findIdsByBibliographicalDataId(bdId1))
                .thenReturn(List.of(sampleId1));

        when(pubRepo.findIdsByBibliographicalDataId(bdId2))
                .thenReturn(List.of(sampleId2));
        when(listingRepo.findIdsByPublicationSampleIdIn(publicationSampleIds)).thenReturn(listingIds);
        when(auctionService.findByListingIdIn(listingIds)).thenReturn(auctions);
        when(auctionMapper.fromDomainList(auctions)).thenReturn(expected);

        // Act
        List<US026AuctionDTO> result = controller.getAuctionsByAuthorId(authorId);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        assertEquals(2, result.size());
    }

    @Test
    void getAuctionsByAuthorId_shouldReturnEmptyList_whenAuthorHasNoBibliographicalData()
            throws InstanceNotFoundException {

        // Arrange
        ToGetTheListOfItemsInAuctionOfAGivenAuthorController controller = createController();

        AuthorId authorId = mock(AuthorId.class);

        List<BibliographicalDataId> bibliographicalDataIds = List.of();
        List<PublicationSampleId> publicationSampleIds = List.of();
        List<ListingId> listingIds = List.of();
        List<Auction> auctions = List.of();
        List<US026AuctionDTO> expected = List.of();

        when(biblioRepository.findIdByAuthorId(authorId))
                .thenReturn(bibliographicalDataIds);

        when(listingRepo.findIdsByPublicationSampleIdIn(publicationSampleIds))
                .thenReturn(listingIds);

        when(auctionService.findByListingIdIn(listingIds))
                .thenReturn(auctions);

        when(auctionMapper.fromDomainList(auctions))
                .thenReturn(expected);

        // Act
        List<US026AuctionDTO> result = controller.getAuctionsByAuthorId(authorId);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
        assertTrue(result.isEmpty());

    }

    @Test
    void getAuctionsByAuthorId_shouldPropagateInstanceNotFoundException_whenMapperThrowsException()
            throws InstanceNotFoundException {

        // Arrange
        ToGetTheListOfItemsInAuctionOfAGivenAuthorController controller = createController();

        AuthorId authorId = mock(AuthorId.class);

        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        PublicationSampleId sampleId = mock(PublicationSampleId.class);
        ListingId listingId = mock(ListingId.class);
        Auction auction = mock(Auction.class);

        List<BibliographicalDataId> bibliographicalDataIds = List.of(bdId);
        List<PublicationSampleId> publicationSampleIds = List.of(sampleId);
        List<ListingId> listingIds = List.of(listingId);
        List<Auction> auctions = List.of(auction);

        when(biblioRepository.findIdByAuthorId(authorId))
                .thenReturn(bibliographicalDataIds);

        when(pubRepo.findIdsByBibliographicalDataId(bdId))
                .thenReturn(publicationSampleIds);

        when(listingRepo.findIdsByPublicationSampleIdIn(publicationSampleIds))
                .thenReturn(listingIds);

        when(auctionService.findByListingIdIn(listingIds))
                .thenReturn(auctions);

        when(auctionMapper.fromDomainList(auctions))
                .thenThrow(new InstanceNotFoundException("Listing not found"));

        // Act + Assert
        InstanceNotFoundException exception = assertThrows(
                InstanceNotFoundException.class,
                () -> controller.getAuctionsByAuthorId(authorId)
        );

        assertEquals("Listing not found", exception.getMessage());
    }

    private ToGetTheListOfItemsInAuctionOfAGivenAuthorController createController() {
        return new ToGetTheListOfItemsInAuctionOfAGivenAuthorController(
                authorRepository,
                biblioRepository,
                pubRepo,
                listingRepo,
                auctionService,
                auctionMapper
        );
    }

    private static Stream<Arguments> invalidConstructorArguments() {
        return Stream.of(
                of(null, mock(IBibliographicalDataRepository.class), mock(IPublicationSampleRepository.class), mock(IListingRepository.class), mock(AuctionService.class), mock(US026AuctionMapper.class), "AuthorRepository cannot be null"),
                of(mock(IAuthorRepository.class), null, mock(IPublicationSampleRepository.class), mock(IListingRepository.class), mock(AuctionService.class), mock(US026AuctionMapper.class), "BibliographicalDataRepository cannot be null"),
                of(mock(IAuthorRepository.class), mock(IBibliographicalDataRepository.class), null, mock(IListingRepository.class), mock(AuctionService.class), mock(US026AuctionMapper.class), "PublicationSampleRepository cannot be null"),
                of(mock(IAuthorRepository.class), mock(IBibliographicalDataRepository.class), mock(IPublicationSampleRepository.class), null, mock(AuctionService.class), mock(US026AuctionMapper.class), "ListingRepository cannot be null"),
                of(mock(IAuthorRepository.class), mock(IBibliographicalDataRepository.class), mock(IPublicationSampleRepository.class), mock(IListingRepository.class), null, mock(US026AuctionMapper.class), "AuctionService cannot be null"),
                of(mock(IAuthorRepository.class), mock(IBibliographicalDataRepository.class), mock(IPublicationSampleRepository.class), mock(IListingRepository.class), mock(AuctionService.class), null, "AuctionMapper cannot be null")
        );
    }
}