//package TOPSECRET.dto;
//
//import TOPSECRET.domain.Author;
//import TOPSECRET.domain.AuthorFactory;
//import TOPSECRET.domain.Country;
//import TOPSECRET.repository.AuthorRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for AuthorDTO_US025.
// */
//class AuthorDTO_US025Test {
//
//    private AuthorRepository authorRepo;
//    private AuthorFactory authorFactory = new AuthorFactory();
//
//    @BeforeEach
//    void setup() {
//        authorRepo = new AuthorRepository(new ArrayList<>(), authorFactory);
//    }
//
//    @Test
//    void fromAuthor_whenValidAuthor_shouldMapNameFromProfile() {
//        // arrange
//        Author author = authorRepo.createAndAddAuthor(
//                "Ken Follett",
//                new Country("United_kingdom"),
//                LocalDate.of(1949, 6, 5),
//                "bio"
//        );
//
//        // act
//        AuthorDTO_US025 dto = AuthorDTO_US025.fromAuthor(author);
//
//        // assert
//        assertNotNull(dto);
//        assertEquals("Ken Follett", dto.getName());
//    }
//
//    @Test
//    void fromAuthor_whenAuthorHasDifferentName_shouldMapThatExactName() {
//        // arrange
//        Author author = authorRepo.createAndAddAuthor(
//                "Dan Brown",
//                new Country("United_kingdom"),
//                LocalDate.of(1964, 6, 5),
//                "bio"
//        );
//
//        // act
//        AuthorDTO_US025 dto = AuthorDTO_US025.fromAuthor(author);
//
//        // assert
//        assertNotNull(dto);
//        assertEquals("Dan Brown", dto.getName());
//    }
//
//    @Test
//    void fromAuthor_whenAuthorIsNull_shouldReturnNull() {
//        // act
//        AuthorDTO_US025 dto = AuthorDTO_US025.fromAuthor(null);
//
//        // assert
//        assertNull(dto);
//    }
//
//    @Test
//    void fromAuthors_listOfAuthors_shouldMapAllNames() {
//        // arrange
//        Author a1 = authorRepo.createAndAddAuthor(
//                "Author One",
//                new Country("Portugal"),
//                LocalDate.of(1970, 1, 1),
//                "bio1"
//        );
//        Author a2 = authorRepo.createAndAddAuthor(
//                "Author Two",
//                new Country("Portugal"),
//                LocalDate.of(1980, 2, 2),
//                "bio2"
//        );
//        var authors = new ArrayList<Author>();
//        authors.add(a1);
//        authors.add(a2);
//
//        // act
//        var dtos = AuthorDTO_US025.fromAuthors(authors);
//
//        // assert
//        assertEquals(2, dtos.size());
//        assertEquals("Author One", dtos.get(0).getName());
//        assertEquals("Author Two", dtos.get(1).getName());
//    }
//
//    @Test
//    void fromAuthors_nullList_shouldReturnMutableList() {
//        var dtos = AuthorDTO_US025.fromAuthors(null);
//        assertNotNull(dtos);
//        assertTrue(dtos.isEmpty());
//
//        dtos.add(new AuthorDTO_US025("Teste"));
//        assertEquals(1, dtos.size());
//    }
//}