package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Profile;
import TOPSECRET.dto.PublicationSampleDTO_US016;
import TOPSECRET.dto.PublicationSampleDTO_US018;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import javax.management.InstanceAlreadyExistsException;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PublicationSampleRepositoryMemTest {

    // ===========================================================
    // ======================== Set Up ===========================
    // ===========================================================

    Listing listing1Double;
    Listing listing2Double;
    Listing listing3Double;
    PublicationSample pubSample1Double;
    PublicationSample pubSample2Double;
    PublicationSample pubSample3Double;
    PublicationSample pubSample4Double;
    BibliographicalData bd1Double;
    BibliographicalData bd2Double;
    BibliographicalData bd3Double;
    Publisher publisher1Double;
    Publisher publisher2Double;
    PublicationSampleFactory factory;

    @BeforeEach
    void setUp() {

        listing1Double = mock(Listing.class);                                           // contains pubSample1
        listing2Double = mock(Listing.class);                                           // contains pubSample2
        listing3Double = mock(Listing.class);                                           // contains pubSample3 e 4

        pubSample1Double = mock(PublicationSample.class);                               // contains bd1
        pubSample2Double = mock(PublicationSample.class);                               // contains bd2
        pubSample3Double = mock(PublicationSample.class);                               // contains bd3
        pubSample4Double = mock(PublicationSample.class);                               // contains bd1

        bd1Double = mock(BibliographicalData.class);
        bd2Double = mock(BibliographicalData.class);
        bd3Double = mock(BibliographicalData.class);

        Edition edition1Double = mock(Edition.class);
        Edition edition2Double = mock(Edition.class);

        // listings
        when(listing1Double.getItems()).thenReturn(List.of(pubSample1Double));
        when(listing2Double.getItems()).thenReturn(List.of(pubSample2Double));
        when(listing3Double.getItems()).thenReturn(List.of(pubSample3Double, pubSample4Double));

        when(listing1Double.getItems()).thenReturn(List.of(pubSample1Double));
        when(listing2Double.getItems()).thenReturn(List.of(pubSample2Double));
        when(listing3Double.getItems()).thenReturn(List.of(pubSample3Double, pubSample4Double));

        // pubSample1
        when(pubSample1Double.getBibliographicalData()).thenReturn(bd1Double);

        // pubSample2
        when(pubSample2Double.getBibliographicalData()).thenReturn(bd2Double);

        // pubSample3
        when(pubSample3Double.getBibliographicalData()).thenReturn(bd3Double);

        // pubSample4
        when(pubSample4Double.getBibliographicalData()).thenReturn(bd1Double);

        // Publisher
        publisher1Double = mock(Publisher.class);
        publisher2Double = mock(Publisher.class);

        when(pubSample1Double.getEdition()).thenReturn(edition1Double);
        when(pubSample2Double.getEdition()).thenReturn(edition2Double);
        when(pubSample3Double.getEdition()).thenReturn(edition1Double);
        when(pubSample4Double.getEdition()).thenReturn(edition2Double);

        when(edition1Double.getPublisher()).thenReturn(publisher1Double);
        when(edition2Double.getPublisher()).thenReturn(publisher2Double);

        //Factory
        factory = mock(PublicationSampleFactory.class);
    }


    // ===========================================================
    // ===== Tests for  findItemsByListingAndPublication =========
    // ===========================================================

    @Test
    void findItemsByListingAndPublication_ShouldReturnSamplesMatchingPublication() {
        // Arrange
        List<Listing> listOfListings = List.of(listing1Double, listing2Double, listing3Double);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        // Assert
        assertEquals(2, itemsList.size());
        assertEquals(pubSample1Double, itemsList.get(0));
        assertEquals(pubSample4Double, itemsList.get(1));
    }

    @Test
    void findItemsByListingAndPublication_ShouldReturnEmptyListWhenNoMatches() {
        // Arrange
        List<Listing> listOfListings = List.of(listing1Double, listing3Double);

        // Act
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd2Double);

        assertTrue(itemsList.isEmpty());
    }

    @Test
    void findItemsByListingAndPublication_ShouldReturnEmptyListWhenListingsEmpty() {
        // Arrange
        List<Listing> listOfListings = List.of();

        // Act
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        // Assert
        assertTrue(itemsList.isEmpty());
    }

    @Test
    void findItemsByListingAndPublication_ShouldReturnImmutableList() {
        // Arrange
        List<Listing> listOfListings = List.of(listing1Double);

        // Act
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> itemsList.add(pubSample1Double));
    }

    // Test for listings with more than 1 item (listing3)
    @Test
    void findItemsByListingAndPublication_ShouldHandleMultipleSamplesInListing() {
        // Arrange
        List<Listing> listOfListings = List.of(listing3Double);

        // Act
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        assertEquals(1, itemsList.size());
        assertTrue(itemsList.contains(pubSample4Double));
    }

    // ===========================================================
    // ===== Tests for  findItemsByListingAndPublisher ===========
    // ===========================================================

    @Test
    void findItemsByListingAndPublisher_ShouldReturnOnlySamplesFromGivenPublisher() {
        // Arrange
        List<Listing> listings = List.of(listing1Double, listing2Double, listing3Double);

        // Act
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSample> result = repo.findItemsByListingAndPublisher(listings, publisher1Double);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pubSample1Double));
        assertTrue(result.contains(pubSample3Double));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldReturnEmptyListWhenNoSamplesMatchPublisher() {
        // Arrange
        Publisher publisherDouble = mock(Publisher.class);                  // new Publisher

        List<Listing> listings = List.of(listing1Double, listing2Double, listing3Double);

        // Act
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSample> result = repo.findItemsByListingAndPublisher(listings, publisherDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findItemsByListingAndPublisher_ShouldHandleEmptyListingsList() {
        // Arrange
        List<Listing> listings = List.of();

        // Act
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSample> result =  repo.findItemsByListingAndPublisher(List.of(), publisher1Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findItemsByListingAndPublisher_ShouldReturnImmutableList() {
        // Arrange
        List<Listing> listings = List.of(listing1Double);

        // Act
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSample> result = repo.findItemsByListingAndPublisher(listings, publisher1Double);

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> result.add(pubSample2Double));
    }


    @Test
    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingsIsNull() {
        //Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findItemsByListingAndPublisher(null, publisher1Double));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenPublisherIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        when(listing1Double.getItems()).thenReturn(List.of());
        when(listing2Double.getItems()).thenReturn(List.of());

        List<Listing> listings = List.of(listing1Double, listing2Double);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findItemsByListingAndPublisher(listings, null));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingsContainNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<Listing> listings = new ArrayList<>();
        listings.add(listing1Double);
        listings.add(null);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findItemsByListingAndPublisher(listings, publisher1Double));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingItemsIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        when(listing1Double.getItems()).thenReturn(null);

        List<Listing> listings = List.of(listing1Double);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repo.findItemsByListingAndPublisher(listings, publisher1Double));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingContainsNullItem() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSample> items = new ArrayList<>();
        items.add(pubSample1Double);
        items.add(null);

        when(listing1Double.getItems()).thenReturn(items);

        List<Listing> listings = List.of(listing1Double);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findItemsByListingAndPublisher(listings, publisher1Double));
    }

    // US018 STARTS MOCK TESTS -------------------------------------------------------

    /**
     * Ensures exception is thrown when library is null.
     */
    @Test
    void shouldThrowExceptionWhenLibraryIsNull() {

        // ARRANGE
        IPublicationSampleRepository sut = new PublicationSampleRepositoryMem(factory);
        PublicationSampleDTO_US018 dto = mock(PublicationSampleDTO_US018.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.PublicationSampleDtoToDomain(null, dto));
    }

    /**
     * Ensures exception is thrown when DTO is null.
     */
    @Test
    void shouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        IPublicationSampleRepository sut = new PublicationSampleRepositoryMem(factory);
        Library library = mock(Library.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.PublicationSampleDtoToDomain(library, null));
    }

    /**
     * Ensures correct PublicationSample is returned
     * when DTO matches publication in user's library.
     */
    @Test
    void shouldReturnMatchingPublicationSample() {

        // ARRANGE
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        Library library = mock(Library.class);
        PublicationSampleDTO_US018 dto = mock(PublicationSampleDTO_US018.class);

        when(library.getAllPublications()).thenReturn(List.of(pubSample1Double));

        when(pubSample1Double.getBibliographicalData()).thenReturn(bd1Double);

        when(bd1Double.getTitle()).thenReturn("Clean Code");

        Edition editionDouble = mock(Edition.class);
        when(pubSample1Double.getEdition()).thenReturn(editionDouble);
        when(editionDouble.getEditionNumber()).thenReturn("1");

        User ownerDouble = mock(User.class);
        when(pubSample1Double.getOwner()).thenReturn(ownerDouble);
        when(ownerDouble.getUserName()).thenReturn("john");

        when(dto.getTitle()).thenReturn("Clean Code");
        when(dto.getEditionNumber()).thenReturn("1");
        when(dto.getOwnerUsername()).thenReturn("john");

        // ACT
        PublicationSample result =
                repo.PublicationSampleDtoToDomain(library, dto);

        // ASSERT
        assertEquals(pubSample1Double, result);
    }

    /**
     * Ensures exception is thrown when
     * publication does not exist in library.
     */
    @Test
    void shouldThrowExceptionWhenPublicationNotFound() {

        // ARRANGE
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        Library library = mock(Library.class);
        PublicationSampleDTO_US018 dto = mock(PublicationSampleDTO_US018.class);

        when(library.getAllPublications()).thenReturn(List.of(pubSample1Double));

        when(bd1Double.getTitle()).thenReturn("Different");
        when(pubSample1Double.getEdition().getEditionNumber()).thenReturn("2");

        User ownerDouble = mock(User.class);
        when(pubSample1Double.getOwner()).thenReturn(ownerDouble);
        when(ownerDouble.getUserName()).thenReturn("mary");

        when(dto.getTitle()).thenReturn("Clean Code");
        when(dto.getEditionNumber()).thenReturn("1");
        when(dto.getOwnerUsername()).thenReturn("john");

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.PublicationSampleDtoToDomain(library, dto));
    }

    // US018 ENDS --------------------------------------------------------------------------

    //============================================================
    // TESTING CREATE AND ADD WITH FACTORY
    @Test
    void shouldReturnValidNewPublicationSampleWithFactory () throws InstanceAlreadyExistsException {

        // Arrange
        PublicationSample publicationSampleDouble = mock(PublicationSample.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        User ownerDouble = mock(User.class);
        PublicationStatus status = PublicationStatus.READ;
        Edition eDouble = mock(Edition.class);
        Condition condition = Condition.LIKE_NEW;

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);
        IPublicationSampleRepository publicationSampleRepositoryMem = new PublicationSampleRepositoryMem(factory);


        when(factory.create(bdDouble, ownerDouble, status, eDouble, condition)).thenReturn(publicationSampleDouble);

        // Act
        PublicationSample result = publicationSampleRepositoryMem.createAndAdd(bdDouble, ownerDouble, status, eDouble, condition);

        // Assert
        verify(factory).create(bdDouble, ownerDouble, status, eDouble, condition);
        assertEquals(publicationSampleDouble, result);
        assertTrue(publicationSampleRepositoryMem.getAllPublications().contains(publicationSampleDouble));
        assertEquals(1, publicationSampleRepositoryMem.getAllPublications().size());
    }

    @Test
    void createAndAdd_shouldThrowException_whenBibliographicalDataIsNull() {
        // Arrange

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);

        User user = mock(User.class);
        Edition edition = mock(Edition.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        null,
                        user,
                        PublicationStatus.READ,
                        edition,
                        Condition.NEW
                )
        );

        verify(factory, never()).create(any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenUserIsNull() {
        // Arrange

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);

        BibliographicalData bd = mock(BibliographicalData.class);
        Edition edition = mock(Edition.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        bd,
                        null,
                        PublicationStatus.READ,
                        edition,
                        Condition.NEW
                )
        );

        verify(factory, never()).create(any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenStatusIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);

        BibliographicalData bd = mock(BibliographicalData.class);
        User user = mock(User.class);
        Edition edition = mock(Edition.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        bd,
                        user,
                        null,
                        edition,
                        Condition.NEW
                )
        );

        verify(factory, never()).create(any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenEditionIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);

        BibliographicalData bd = mock(BibliographicalData.class);
        User user = mock(User.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        bd,
                        user,
                        PublicationStatus.READ,
                        null,
                        Condition.NEW
                )
        );

        verify(factory, never()).create(any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenConditionIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);

        BibliographicalData bd = mock(BibliographicalData.class);
        User user = mock(User.class);
        Edition edition = mock(Edition.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        bd,
                        user,
                        PublicationStatus.READ,
                        edition,
                        null
                )
        );

        verify(factory, never()).create(any(), any(), any(), any(), any());
    }

    @Test
    void getPublicationByDTO_shouldThrow_whenDTOList_Empty() {

        // arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSampleDTO_US016> pubsDTOList = new ArrayList<>();

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.getPublicationsByDTO(pubsDTOList));

        assertEquals("list of PublicationSampleDTO cannot be null or empty", exception.getMessage());
    }

    @Test
    void getPublicationByDTO_shouldThrow_whenDTOList_Null() {

        //Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.getPublicationsByDTO(null));

        assertEquals("list of PublicationSampleDTO cannot be null or empty", exception.getMessage());
    }

    @Test
    void getPublicationByDTO_shouldReturn_EmptyList_whenNoPublicationSampleMatchesDTO() {

        // arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        List<PublicationSample> matchedList = List.of();


        PublicationSampleDTO_US016 dtoDouble1 = mock(PublicationSampleDTO_US016.class);
        PublicationSampleDTO_US016 dtoDouble2 = mock(PublicationSampleDTO_US016.class);
        List<PublicationSampleDTO_US016> dtoList = List.of(dtoDouble1, dtoDouble2);

        // act
        List<PublicationSample> result = repo.getPublicationsByDTO(dtoList);

        // assert
        assertEquals(0, result.size());

    }


    @Test
    void getPublicationsByDTO_allFieldsMatch_returnsPublication() {
        //Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        PublicationSample ps = mockPublicationSample(
                "Title",
                List.of("Author1"),
                Locale.ENGLISH,
                List.of("Genre1"),
                "owner",
                "New"
        );
        repo.add(ps);

        PublicationSampleDTO_US016 dto = mockDto(
                "Title",
                List.of("Author1"),
                Locale.ENGLISH,
                List.of("Genre1"),
                "owner",
                "New"
        );

        //Act
        List<PublicationSample> result = repo.getPublicationsByDTO(List.of(dto));

        //Assert
        assertEquals(1, result.size());
        assertSame(ps, result.get(0));
    }


    @Test
    void getPublicationsByDTO_authorOrderDifferent_returnsEmpty_becauseListEqualsIsOrderSensitive() {
        //Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        PublicationSample ps = mockPublicationSample(
                "Title",
                List.of("Author1", "Author2"),
                Locale.ENGLISH,
                List.of("Genre1"),
                "owner",
                "New"
        );
        repo.add(ps);

        PublicationSampleDTO_US016 dto = mockDto(
                "Title",
                List.of("Author2", "Author1"),
                Locale.ENGLISH,
                List.of("Genre1"),
                "owner",
                "New"
        );

        //Act
        List<PublicationSample> result = repo.getPublicationsByDTO(List.of(dto));

        //Arrange
        assertTrue(result.isEmpty());
    }

    @Test
    void isExistent_returnsTrue_WhenRepoContainsSamePublicationSample() {

        // arrange
        PublicationSample psDouble1 = mock(PublicationSample.class);
        PublicationSample psDouble2 = mock(PublicationSample.class);

        PublicationSampleFactory psFactory = mock(PublicationSampleFactory.class);
        IPublicationSampleRepository publicationSampleRepositoryMem = new PublicationSampleRepositoryMem(psFactory);

        publicationSampleRepositoryMem.add(psDouble1);
        publicationSampleRepositoryMem.add(psDouble2);

        // act + assert

        assertTrue(publicationSampleRepositoryMem.isExistent(psDouble1));

    }

    @Test
    void isExistent_returnsFalse_WhenRepoDoeNotContainPublicationSample() {

        // arrange
        PublicationSample psDouble1 = mock(PublicationSample.class);
        PublicationSample psDouble2 = mock(PublicationSample.class);

        PublicationSampleFactory psFactory = mock(PublicationSampleFactory.class);
        IPublicationSampleRepository publicationSampleRepositoryMem = new PublicationSampleRepositoryMem(psFactory);

        publicationSampleRepositoryMem.add(psDouble1);

        // act + assert
        assertFalse(publicationSampleRepositoryMem.isExistent(psDouble2));

    }

    @Test
    void isExistent_returnsFalse_whenEmptyRepo() {

        // arrange
        PublicationSample psDouble = mock(PublicationSample.class);

        PublicationSampleFactory psFactory = mock(PublicationSampleFactory.class);
        IPublicationSampleRepository publicationSampleRepositoryMem = new PublicationSampleRepositoryMem(psFactory);

        // act + assert
        assertFalse(publicationSampleRepositoryMem.isExistent(psDouble));

    }

    @Test
    void getListOfDifferentPublications_whenLibraryContainsSome_returnsOnlyMissingOnes() {
        PublicationSample p1 = mock(PublicationSample.class);
        PublicationSample p2 = mock(PublicationSample.class);
        PublicationSample p3 = mock(PublicationSample.class);

        // repo: p1, p2, p3
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.add(p1);
        repo.add(p2);
        repo.add(p3);

        // library tem p2
        List<PublicationSample> library = List.of(p2);

        List<PublicationSample> result = repo.getListOfDifferentPublications(library);

        // deve devolver p1 e p3
        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p3));
        assertFalse(result.contains(p2));
    }

    @Test
    void getListOfDifferentPublications_whenLibraryContainsAll_returnsEmpty() {
        PublicationSample p1 = mock(PublicationSample.class);
        PublicationSample p2 = mock(PublicationSample.class);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.add(p1);
        repo.add(p2);

        List<PublicationSample> result = repo.getListOfDifferentPublications(List.of(p1, p2));

        assertTrue(result.isEmpty());
    }

    @Test
    void getListOfDifferentPublications_whenLibraryContainsNone_returnsAllRepoPublications() {
        PublicationSample p1 = mock(PublicationSample.class);
        PublicationSample p2 = mock(PublicationSample.class);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.add(p1);
        repo.add(p2);

        List<PublicationSample> result = repo.getListOfDifferentPublications(List.of());

        assertEquals(2, result.size());
        assertEquals(List.of(p1, p2), result); // mesma ordem do repo
    }

    @Test
    void getListOfDifferentPublications_returnsImmutableCopy() {
        PublicationSample p1 = mock(PublicationSample.class);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.add(p1);

        List<PublicationSample> result = repo.getListOfDifferentPublications(List.of());

        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(PublicationSample.class)));
    }


    private static PublicationSample mockPublicationSample(
            String title,
            List<String> authorNames,
            Locale language,
            List<String> genreTypes,
            String ownerName,
            String conditionDescription
    ) {
        PublicationSample ps = mock(PublicationSample.class);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(ps.getBibliographicalData()).thenReturn(bdDouble);

        when(bdDouble.getTitle()).thenReturn(title);
        when(bdDouble.getOriginalLanguage()).thenReturn(language);

        List<Author> authors = authorNames.stream().map(n -> {
            Author authorDouble = mock(Author.class);
            when(authorDouble.getName()).thenReturn(n);
            return authorDouble;
        }).toList();
        when(bdDouble.getAuthors()).thenReturn(authors);

        // Genres -> genreType
        List<Genre> genres = genreTypes.stream().map(gt -> {
            Genre g = mock(Genre.class);
            when(g.getGenreType()).thenReturn(gt);
            return g;
        }).toList();
        when(bdDouble.getGenres()).thenReturn(genres);

        // owner username
        User ownerDouble = mock(User.class);
        when(ownerDouble.getUserName()).thenReturn(ownerName);
        when(ps.getOwner()).thenReturn(ownerDouble);

        // condition description
        Condition conditionDouble = mock(Condition.class);
        when(conditionDouble.getDescription()).thenReturn(conditionDescription);
        when(ps.getCondition()).thenReturn(conditionDouble);

        return ps;
    }

    private static PublicationSampleDTO_US016 mockDto(
            String title,
            List<String> authors,
            Locale language,
            List<String> genreTypes,
            String ownerName,
            String condition
    ) {
        PublicationSampleDTO_US016 dtoDouble = mock(PublicationSampleDTO_US016.class);
        when(dtoDouble.getTitle()).thenReturn(title);
        when(dtoDouble.getAuthorName()).thenReturn(authors);
        when(dtoDouble.getLanguage()).thenReturn(language);          // Locale
        when(dtoDouble.getGenreType()).thenReturn(genreTypes);
        when(dtoDouble.getOwnerName()).thenReturn(ownerName);
        when(dtoDouble.getCondition()).thenReturn(condition);
        return dtoDouble;
    }
}