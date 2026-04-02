package TOPSECRET.controller;

import TOPSECRET.dto.AuthorDTO_US026;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.repository.AuctionRepositoryMem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorControllerTest {

    AuctionRepositoryMem auctionRepository;
    IAuthorRepository IAuthorRepository;

    US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController controller;

    @BeforeEach
    void setup() {
        IAuthorRepository = mock(IAuthorRepository.class);
        controller = new US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController(IAuthorRepository, auctionRepository);
        auctionRepository = mock(AuctionRepositoryMem.class);
        controller = new US026ToGetTheListOfItemsOnAuctionOfAGivenAuthorController(IAuthorRepository, auctionRepository);
    }

    @Test
    void getAllAuthors_shouldReturnEmptyList_whenAuthorRepoIsEmpty() {

        // act
        List<AuthorDTO_US026> result = controller.getAllAuthors();

        // assert
        assertEquals(0, result.size());
    }

//    @Test
//    void getAllAuthors_shouldReturnListOfAuthorsDtoInRepo() {
//
//        // arrange
//        String name = "author1";
//        String country = "Portugal";
//        LocalDate birthday = LocalDate.of(1999, 6,1);
//        String biography = "this is a biography";
//
//        String name2 = "author2";
//        LocalDate birthday2 = LocalDate.of(1980, 6,1);
//        String biography2 = "this is a biography2";
//
//        Author author1 = createMockAuthor(name, country, birthday, biography);
//        Author author2 = createMockAuthor(name2, country, birthday2, biography2);
//
//        when(IAuthorRepository.getAllAuthors()).thenReturn(List.of(author1, author2));
//
//        // act
//        List<AuthorDTO_US026> result = controller.getAllAuthors();
//
//        // assert
//        assertEquals(2, result.size());
//    }

//    @Test
//    void getListOfItemsOnAuctionByAuthor_whenRepoReturnsEmpty_returnsEmpty() {
//        AuthorDTO_US026 authorDto = mock(AuthorDTO_US026.class);
//
//        when(auctionRepository.getListOfItemsOnAuctionGivenAuthor(authorDto)).thenReturn(List.of());
//
//        List<AuctionDTO_US026> result = controller.getListOfItemsOnAuctionByAuthor(authorDto);
//
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void getListOfItemsOnAuctionByAuthor_whenRepoReturnsAuctions_returnsSameCountOfDTOs() {
//        AuthorDTO_US026 authorDto = mock(AuthorDTO_US026.class);
//
//        Auction auc1 = mock(Auction.class);
//        Auction auc2 = mock(Auction.class);
//        Auction auc3 = mock(Auction.class);
//
//        when(auctionRepository.getListOfItemsOnAuctionGivenAuthor(authorDto))
//                .thenReturn(List.of(auc1, auc2, auc3));
//
//        List<AuctionDTO_US026> result = controller.getListOfItemsOnAuctionByAuthor(authorDto);
//
//        assertNotNull(result);
//        assertEquals(3, result.size());
//
//    }

//    private Author createMockAuthor(String name, String country, LocalDate dateOfBirth, String biography) {
//        Country countryDouble = mock(Country.class);
//        when(countryDouble.getCountryName()).thenReturn(country);
//
//        Author authorDouble = mock(Author.class);
//
//        when(authorDouble.getName()).thenReturn(name);
//        when(authorDouble.getDateOfBirth()).thenReturn(dateOfBirth);
//        when(authorDouble.getCountry()).thenReturn(countryDouble);
//        when(authorDouble.getBiography()).thenReturn(biography);
//
//        return authorDouble;
//    }

}