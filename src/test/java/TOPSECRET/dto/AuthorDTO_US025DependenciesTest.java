package TOPSECRET.dto;

import static org.mockito.Mockito.mock;

/**
 * Unit tests for AuthorDTO_US025.
 */
//class AuthorDTO_US025DependenciesTest {
//
//    private AuthorRepository authorRepo;
//    private AuthorFactory authorFactory = new AuthorFactory();
//
//    @BeforeEach
//    void setup() {
//       AuthorRepository authorRepo = new  (authorFactory);
//    }
//
//    @Test
//    void fromAuthor_whenValidAuthor_shouldMapNameFromProfile() {
//        // arrange
//        Country portugal = mock(Country.class);
//        when(portugal.getCountryName()).thenReturn("Portugal");
//        Author author = authorRepo.createAndAddAuthor(
//                "Ken Follett",
//                portugal,
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
//        Country portugal = mock(Country.class);
//        when(portugal.getCountryName()).thenReturn("Portugal");
//        Author author = authorRepo.createAndAddAuthor(
//                "Dan Brown",
//                portugal,
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
//
//        Country portugal1 = mock(Country.class);
//        when(portugal1.getCountryName()).thenReturn("Portugal");
//        Author a1 = authorRepo.createAndAddAuthor(
//                "Author One",
//                portugal1,
//                LocalDate.of(1970, 1, 1),
//                "bio1"
//        );
//        Country portugal2 = mock(Country.class);
//        when(portugal2.getCountryName()).thenReturn("Portugal");
//
//        Author a2 = authorRepo.createAndAddAuthor(
//                "Author Two",
//                portugal2,
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