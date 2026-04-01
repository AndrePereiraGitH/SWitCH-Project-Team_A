package TOPSECRET.dto;

import TOPSECRET.domain.PublicationSample.PublicationSample;

/**
 * Unit tests for {@link PublicationSampleDTO_US016}.
 * <p>
 * Verifies that the DTO can be instantiated and that the {@link PublicationSampleDTO_US016#toDTO(PublicationSample)}
 * mapping correctly copies the expected fields from the {@link PublicationSample} domain object.
 */
class PublicationSampleDTO_US016DependenciesTest {

    /**
     * Ensures the DTO constructor creates a non-null instance.
     */
//    @Test
//    void constructorTest() {
//        PublicationSampleDTO_US016 sampleDTOUs016 = createPublicationSampleDTO();
//
//        assertNotNull(sampleDTOUs016);
//    }
//
//    /**
//     * Given a valid {@link PublicationSample}, verifies that {@link PublicationSampleDTO_US016#toDTO(PublicationSample)}
//     * returns a DTO with all fields mapped as expected:
//     * title, author names, original language, genre types, owner username, and condition description.
//     */
//    @Test
//    void toDTO_shouldReturn_validPublicationSampleDTO_forValidPublicationSample() {
//        // arrange
//        PublicationSample publicationSample = createPublicationSample();
//        List<String> expectedAuthorsNames = publicationSample.getBibliographicalData().getAuthors().stream().map(a -> a.getProfile().getName()).toList();
//
//        List<String> expectedGenres = publicationSample.getBibliographicalData().getGenres().stream().map(g -> g.getGenreType()).toList();
//
//        // act
//        PublicationSampleDTO_US016 result = PublicationSampleDTO_US016.toDTO(publicationSample);
//
//        // assert
//        assertEquals(publicationSample.getBibliographicalData().getTitle(), result.getTitle());
//        assertEquals(expectedAuthorsNames, result.getAuthorName());
//        assertEquals(publicationSample.getBibliographicalData().getOriginalLanguage(), result.getLanguage());
//        assertEquals(expectedGenres, result.getGenreType());
//        assertEquals(publicationSample.getOwnerId().getUserName(), result.getOwnerName());
//        assertEquals(publicationSample.getCondition().getDescription(), result.getCondition());
//    }
//
//    /**
//     * Tests for equals and hash code
//     */
//
//    @Test
//    void equals_shouldReturnTrue_forSameReference() {
//        PublicationSampleDTO_US016 dto = createPublicationSampleDTO();
//
//        assertEquals(dto, dto);
//    }
//
//    @Test
//    void equals_shouldReturnFalse_whenComparedWithNull() {
//        PublicationSampleDTO_US016 dto = createPublicationSampleDTO();
//
//        assertNotEquals(dto, null);
//    }
//
//    @Test
//    void equals_shouldReturnFalse_whenComparedWithDifferentClass() {
//        PublicationSampleDTO_US016 dto = createPublicationSampleDTO();
//
//        assertNotEquals(dto, "not a dto");
//    }
//
//    @Test
//    void equals_shouldReturnTrue_forDifferentInstancesWithSameValues() {
//        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
//        PublicationSampleDTO_US016 dto2 = createPublicationSampleDTO();
//
//        assertEquals(dto1, dto2);
//        assertEquals(dto2, dto1);
//    }
//
//    @Test
//    void hashCode_shouldBeEqual_whenObjectsAreEqual() {
//        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
//        PublicationSampleDTO_US016 dto2 = createPublicationSampleDTO();
//
//        assertEquals(dto1, dto2);
//        assertEquals(dto1.hashCode(), dto2.hashCode());
//    }
//
//    @Test
//    void hashCode_shouldBeConsistent() {
//        PublicationSampleDTO_US016 dto = createPublicationSampleDTO();
//
//        int first = dto.hashCode();
//        int second = dto.hashCode();
//
//        assertEquals(first, second);
//    }
//
//    @Test
//    void equals_shouldReturnFalse_whenTitleIsDifferent() {
//        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
//        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
//                "Different title",
//                dto1.getAuthorName(),
//                dto1.getLanguage(),
//                dto1.getGenreType(),
//                dto1.getOwnerName(),
//                dto1.getCondition()
//        );
//
//        assertNotEquals(dto1, dto2);
//    }
//
//    @Test
//    void equals_shouldReturnFalse_whenAuthorNameIsDifferent() {
//        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
//        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
//                dto1.getTitle(),
//                new ArrayList<>(List.of("another author")),
//                dto1.getLanguage(),
//                dto1.getGenreType(),
//                dto1.getOwnerName(),
//                dto1.getCondition()
//        );
//
//        assertNotEquals(dto1, dto2);
//    }
//
//    @Test
//    void equals_shouldReturnFalse_whenLanguageIsDifferent() {
//        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
//        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
//                dto1.getTitle(),
//                dto1.getAuthorName(),
//                Locale.FRENCH,
//                dto1.getGenreType(),
//                dto1.getOwnerName(),
//                dto1.getCondition()
//        );
//
//        assertNotEquals(dto1, dto2);
//    }
//
//    @Test
//    void equals_shouldReturnFalse_whenGenreTypeIsDifferent() {
//        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
//        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
//                dto1.getTitle(),
//                dto1.getAuthorName(),
//                dto1.getLanguage(),
//                new ArrayList<>(List.of("Non-fiction")),
//                dto1.getOwnerName(),
//                dto1.getCondition()
//        );
//
//        assertNotEquals(dto1, dto2);
//    }
//
//    @Test
//    void equals_shouldReturnFalse_whenOwnerNameIsDifferent() {
//        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
//        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
//                dto1.getTitle(),
//                dto1.getAuthorName(),
//                dto1.getLanguage(),
//                dto1.getGenreType(),
//                "different owner",
//                dto1.getCondition()
//        );
//
//        assertNotEquals(dto1, dto2);
//    }
//
//    @Test
//    void equals_shouldReturnFalse_whenConditionIsDifferent() {
//        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
//        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
//                dto1.getTitle(),
//                dto1.getAuthorName(),
//                dto1.getLanguage(),
//                dto1.getGenreType(),
//                dto1.getOwnerName(),
//                "POOR"
//        );
//
//        assertNotEquals(dto1, dto2);
//    }
//
//
//
//    /**
//     * Creates a minimal {@link PublicationSampleDTO_US016} instance to test the constructor.
//     *
//     * @return a DTO instance with sample values
//     */
//    private PublicationSampleDTO_US016 createPublicationSampleDTO() {
//        return new PublicationSampleDTO_US016(
//                "Lord of the Rings",
//                new ArrayList<>(List.of("author name")),
//                Locale.ENGLISH,
//                new ArrayList<>(List.of("Fiction")),
//                "seller name",
//                "LIKE_NEW"
//        );
//    }
//
//    /**
//     * Builds a valid {@link PublicationSample} with a complete object graph (bibliographical data, authors, genres,
//     * owner user, edition and condition) to be used by mapping tests.
//     *
//     * @return a fully initialized {@link PublicationSample}
//     */
//    private PublicationSample createPublicationSample() {
//        // ===========
//        // code provided by PublicationSampleTest
//        // ============
//        String title = "A Book";
//
//        Country country = new Country("UNITED_STATES");
//        Profile profile2 = new Profile("Henry A Kissinger", country, LocalDate.of(1923, 05, 27));
//        Profile profile3 = new Profile("Eric Schmidt III", country, LocalDate.of(1955, 04, 27));
//        Profile profile4 = new Profile("Daniel Huttenlocher", country, LocalDate.of(1959, 01, 01));
//        AuthorFactory authorFactory = new AuthorFactory();
//        Author author2 = authorFactory.createAuthorWithExistingProfile(profile2, "valid bio2");
//        Author author3 = authorFactory.createAuthorWithExistingProfile(profile3, "valid bio3");
//        Author author4 = authorFactory.createAuthorWithExistingProfile(profile4, "valid bio4");
//
//
//        List<Author> authors = List.of(author2, author3, author4);
//
//
//        Locale language = ENGLISH;
//        LocalDate publishDate = LocalDate.of(2022, 8, 4);
//        Genre genre1 = new Genre("Fiction");
//        List<Genre> genres = List.of(genre1);
//        String chunksOfText = "The sweet spot is in partnership—AI handles scale, I bring empathy, wisdom, and context.";
//
//        BibliographicalData anyBd = new BibliographicalData(title, authors, language, publishDate, genres,
//                chunksOfText);
//
//
//        //Arrange User
//
//        Address address1 = new Address("Main Street", "10", "12345", new Country("PORTUGAL"));
//        Email email1 = new Email("test.user", "example.com");
//        Contact contact1 = new Contact("987654321", "351", email1, address1);
//
//        Profile profile5 = new Profile(
//                "Test Profile",
//                new Country("PORTUGAL"),
//                LocalDate.of(1997, 8, 15)
//        );
//
//        User anyOwner = new User("ValidName", contact1, profile5, UserRole.NORMAL_USER);
//
//
//        //Arrange PublicationStatus
//
//        PublicationStatus anyPubStatus = PublicationStatus.READING;
//
//
//        //Arrange Edition
//
//        String editionNumber1 = "1st edition";
//        Code code1 = Code.fromIsbn13("0123456789012");
//        Dimensions dimensions1 = new Dimensions(4.5, 5.4, 2.3);
//        PhysicalAttributes physicalAttributes1 = new PhysicalAttributes(dimensions1, 0.8, 250);
//        PublicationType pubType1 = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Cool cover");
//        Address address2 = new Address("Not Main Street", "11", "12346", new Country("SPAIN"));
//        Email email2 = new Email("test.user2", "example2.com");
//        Contact contact2 = new Contact("987438239", "351", email2, address2);
//        Publisher publisher1 = new Publisher("Cool publisher", "MT1234567", contact2);
//        Locale editionLanguage1 = Locale.JAPANESE;
//        LocalDate editionDate1 = LocalDate.of(2015, 1, 12);
//
//        EditionFactory editionFactory = new EditionFactory();
//        Edition anyEdition = editionFactory.create(editionNumber1, code1, physicalAttributes1, pubType1, publisher1,
//                editionLanguage1, editionDate1);
//
//        //Arrange Condition
//
//        Condition anyCondition = Condition.VERY_GOOD;
//
//        PublicationSample anySample = new PublicationSample(anyBd, anyOwner, anyPubStatus, anyEdition, anyCondition);
//
//        return anySample;
//    }

}