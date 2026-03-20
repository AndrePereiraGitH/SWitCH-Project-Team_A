package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.repository.GenreRepositoryMem;
import TOPSECRET.repository.ListingRepositoryMem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US021ToGetListOfItemsOnDirectSaleOfGivenGenreController}.
 *
 * These tests verify the controller's behaviour in isolation using mock doubles
 * for {@link GenreRepositoryMem} and {@link ListingRepositoryMem}, ensuring the controller
 * correctly delegates to its dependencies without testing their internal logic.
 */

class US021ToGetListOfItemsOnDirectSaleOfGivenGenreControllerTest {

    @Test
    void ensureControllerIsCreatedSuccessfully() {
        //Arrange
        GenreRepositoryMem genreRepositoryDouble = mock(GenreRepositoryMem.class);
        ListingRepositoryMem listingRepositoryDouble = mock(ListingRepositoryMem.class);

        // Act
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryDouble, listingRepositoryDouble);

        //Assert
        assertNotNull(controller);
    }

    @Test
    void ensureThrowsExceptionWhenGenreRepositoryIsNull() {
        // Arrange
        ListingRepositoryMem listingRepositoryDouble = mock(ListingRepositoryMem.class);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, listingRepositoryDouble));
    }

    @Test
    void ensureThrowsExceptionWhenListingRepositoryIsNull() {
        //Arrange
        GenreRepositoryMem genreRepositoryDouble = mock(GenreRepositoryMem.class);

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(genreRepositoryDouble, null));
    }

    @Test
    void ensureThrowsExceptionWhenBothRepositoriesAreNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(null, null));
    }


    /**
     * Verifies that getAllGenres() delegates to the genre repository
     * and returns an empty list when the repository contains no genres.
     * The repository mock is configured to return an empty list.
     */
    @Test
    void ensureGetAllGenresReturnsEmptyList() {
        //Arrange
        GenreRepositoryMem genreRepositoryDouble = mock(GenreRepositoryMem.class);
        ListingRepositoryMem listingRepositoryDouble = mock(ListingRepositoryMem.class);
        when(genreRepositoryDouble.getAllGenres()).thenReturn(new ArrayList<>());

        //Act
        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepositoryDouble, listingRepositoryDouble);
        List<Genre> result = controller.getAllGenres();


        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Verifies that getAllGenres() delegates to the genre repository
     * and returns the correct list when the repository contains genres.
     * The repository mock is configured to return two genre doubles.
     */
    @Test
    void ensureGetAllGenresReturnsCorrectList() {
        //Arrange
        GenreRepositoryMem genreRepositoryDouble = mock(GenreRepositoryMem.class);
        ListingRepositoryMem listingRepositoryDouble = mock(ListingRepositoryMem.class);

        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);
        when(genreRepositoryDouble.getAllGenres()).thenReturn(List.of(genre1Double, genre2Double));

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller = new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                genreRepositoryDouble, listingRepositoryDouble);

        // Act
        List<Genre> result = controller.getAllGenres();

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void ensureThrowsExceptionWhenGenreIsNull() {
        //Arrange
        GenreRepositoryMem genreRepositoryDouble = mock(GenreRepositoryMem.class);
        ListingRepositoryMem listingRepositoryDouble = mock(ListingRepositoryMem.class);

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryDouble, listingRepositoryDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(null));
    }


    /**
     * Verifies that the controller returns an empty list when the listing
     * repository contains no items matching the given genre.
     * The listing repository mock is configured to return an empty list
     * for the given genre double.
     */
    @Test
    void ensureReturnsEmptyListWhenNoItemsMatchGenre() {
        //Arrange
        GenreRepositoryMem genreRepositoryDouble = mock(GenreRepositoryMem.class);
        ListingRepositoryMem listingRepositoryDouble = mock(ListingRepositoryMem.class);

        Genre genreDouble = mock(Genre.class);
        when(listingRepositoryDouble.getListOfItemsOnDirectSaleOfAGivenGenre(genreDouble))
                .thenReturn(new ArrayList<>());

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryDouble, listingRepositoryDouble);

        //Act
        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(genreDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Verifies that the controller returns a list with exactly one item
     * when the listing repository contains one matching direct sale item.
     * The listing repository mock is configured to return a list with one
     * publication sample double for the given genre.
     */
    @Test
    void ensureReturnsListWithOneItemWhenOneMatchesGenre() throws Exception {
        // Arrange
        GenreRepositoryMem genreRepositoryDouble = mock(GenreRepositoryMem.class);
        ListingRepositoryMem listingRepositoryDouble = mock(ListingRepositoryMem.class);

        Genre genreDouble = mock(Genre.class);
        PublicationSample sampleDouble = mock(PublicationSample.class);

        when(listingRepositoryDouble.getListOfItemsOnDirectSaleOfAGivenGenre(genreDouble))
                .thenReturn(List.of(sampleDouble));

        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryDouble, listingRepositoryDouble);

        // Act
        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(genreDouble);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }


    /**
     * Verifies that the controller returns an empty list when no listings
     * belong to the queried genre.
     * The listing repository mock returns an empty list for the science genre double,
     * simulating a scenario where all existing listings belong to a different genre.
     */
    @Test
    void ensureReturnsEmptyListWhenNoListingsMatchGenre() {
        // Arrange
        GenreRepositoryMem genreRepositoryDouble = mock(GenreRepositoryMem.class);
        ListingRepositoryMem listingRepositoryDouble = mock(ListingRepositoryMem.class);

        Genre scienceDouble = mock(Genre.class);
        when(genreRepositoryDouble.createAndAdd("Science")).thenReturn(scienceDouble);


        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryDouble, listingRepositoryDouble);

        // Act
        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(scienceDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Verifies that the controller returns an empty list when the only
     * existing listing is not of type DIRECT_SALE.
     * The listing repository mock returns an empty list for the genre double,
     * simulating a scenario where the matching listing is an auction instead.
     */
    @Test
    void ensureReturnsEmptyListWhenListingIsNotDirectSale() {
        // Arrange
        GenreRepositoryMem genreRepositoryDouble = mock(GenreRepositoryMem.class);
        ListingRepositoryMem listingRepositoryDouble = mock(ListingRepositoryMem.class);

        Genre fictionDouble = mock(Genre.class);
        when(listingRepositoryDouble.getListOfItemsOnDirectSaleOfAGivenGenre(fictionDouble))
                .thenReturn(new ArrayList<>());


        US021ToGetListOfItemsOnDirectSaleOfGivenGenreController controller =
                new US021ToGetListOfItemsOnDirectSaleOfGivenGenreController(
                        genreRepositoryDouble, listingRepositoryDouble);

        // Act
        List<PublicationSample> result =
                controller.getListOfItemsOnDirectSaleOfAGivenGenre(fictionDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}


