package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AppraisalEntityRepository.
 * Validates creation, storage, and duplicate prevention using name + contact.
 */
class AppraisalEntityRepositoryTest {

    /**
     * Helper method to create a valid Contact.
     * @return valid Contact
     */
    private Contact createValidContact() {
        return new Contact("912345678", "351", null, null);
    }

    /**
     * Helper method to create a valid PublicationType.
     * @param details publication type details
     * @return valid PublicationType
     */
    private PublicationType createValidPublicationType(String details) {
        return new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, details);
    }

    /**
     * Helper method to create a valid Genre.
     * @param title genre title
     * @return valid Genre
     */
    private Genre createValidGenre(String title) {
        return new Genre(title);
    }

    /**
     * Helper method to create a valid list of Genres with variable size.
     * @param titles genre titles
     * @return List of Genre
     */
    private List<Genre> createGenreList(String... titles) {
        return java.util.Arrays.stream(titles).map(this::createValidGenre).toList();}

    /**
     * Helper method to create a valid list of PublicationTypes with variable size.
     * @param details list of details
     * @return List of PublicationType
     */
    private List<PublicationType> createPublicationTypeList(String... details) {
        return java.util.Arrays.stream(details).map(this::createValidPublicationType).toList();
    }

    /**
     * Verifies repository successfully creates and stores a new AppraisalEntity.
     */
    @Test
    void shouldCreateAndAddAppraisalEntitySuccessfully() {

        // ARRANGE
        AppraisalEntityRepository repository = new AppraisalEntityRepository();

        String name = "Entity One";
        Contact contact = createValidContact();

        List<Genre> genres = createGenreList("Fiction");
        List<PublicationType> publicationTypes = createPublicationTypeList("Standard publication");

        AppraisalEntity expected =
                new AppraisalEntity(name, contact, publicationTypes, genres);

        // ACT
        AppraisalEntity result =
                repository.createAndAddAppraisalEntity(name, contact, publicationTypes, genres);

        // ASSERT
        assertEquals(expected, result);
    }

    /**
     * Verifies repository throws exception when trying to create duplicate AppraisalEntity
     * using the same name + contact.
     */
    @Test
    void shouldThrowExceptionWhenAppraisalEntityAlreadyExists() {

        // ARRANGE
        AppraisalEntityRepository repository = new AppraisalEntityRepository();

        String name = "Entity One";
        Contact contact = createValidContact();

        List<Genre> genres = createGenreList("Fiction", "History");
        List<PublicationType> publicationTypes = createPublicationTypeList("Standard", "Collector");

        repository.createAndAddAppraisalEntity(name, contact, publicationTypes, genres);

        // ACT + ASSERT
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddAppraisalEntity(name, contact, publicationTypes, genres)
        );

        assertEquals("Appraisal Entity with same name and contact already exists.", ex.getMessage());
    }

    /**
     * Verifies duplicate detection ignores differences in lists (Genres/PublicationTypes)
     * and checks only name + contact.
     */
    @Test
    void shouldThrowExceptionWhenSameNameAndContactButDifferentLists() {

        // ARRANGE
        AppraisalEntityRepository repository = new AppraisalEntityRepository();

        String name = "Entity One";
        Contact contact = createValidContact();

        List<Genre> genres1 = createGenreList("Fiction");
        List<PublicationType> publicationTypes1 = createPublicationTypeList("Standard");

        List<Genre> genres2 = createGenreList("History", "Fantasy");
        List<PublicationType> publicationTypes2 = createPublicationTypeList("Collector", "Pocket");

        repository.createAndAddAppraisalEntity(name, contact, publicationTypes1, genres1);

        // ACT + ASSERT
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddAppraisalEntity(name, contact, publicationTypes2, genres2)
        );

        assertEquals("Appraisal Entity with same name and contact already exists.", ex.getMessage());
    }
}