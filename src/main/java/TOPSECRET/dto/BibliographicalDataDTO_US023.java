package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing the bibliographical data of a publication
 * for use in the US023 - "To get the list of items on direct sale of a given publication."
 *
 * <p>Carries a flat, presentation-ready summary of a {@link BibliographicalData}
 * domain object, exposing only the title, author names, and genre titles
 * needed by the UI layer.</p>
 */
public class BibliographicalDataDTO_US023 {

    private final String _publicationTitle;
    private final List<String> _authors;
    private final List<String> _publicationGenres;

    // ======= Constructor =============================================================================================

    /**
     * Constructs a new {@code BibliographicalDataDTO_US023} with all required fields.
     *
     * @param publicationTitle the title of the publication; must not be {@code null}
     * @param authors          the list of author names; must not be {@code null}
     * @param publicationGenres the list of genres titles; must not be {@code null}
     */

    public BibliographicalDataDTO_US023(String publicationTitle, List<String> authors, List<String> publicationGenres) {
        _publicationTitle = publicationTitle;
        _authors = authors;
        _publicationGenres = publicationGenres;
    }

    // ====== Getters ==================================================================================================

    /**
     * Returns the title of the publication.
     *
     * @return the non-null title of the work
     */

    public String getTitle() {
        return _publicationTitle;
    }

    /**
     * Returns the list of authors associated with the publication.
     * The returned list is immutable and contains at least one author.
     *
     * @return a non-null, non-empty immutable list of author names
     */

    public List<String> getAuthors() {
        return _authors;
    }

    /**
     * Returns the list of genres assigned to the publication.
     * The returned list is immutable and contains at least one genre.
     *
     * @return a non-null, non-empty immutable list of genre names
     */

    public List<String> getGenres() {
        return _publicationGenres;
    }

    // ====== toDTO(s) =================================================================================================
    /**
     * Converts a list of {@link BibliographicalData} domain objects into a list
     * of {@code BibliographicalDataDTO_US023} instances.
     *
     * <p>Each element is converted individually via {@link #toDTO(BibliographicalData)}.
     * If any element in the list is {@code null}, a {@link NullPointerException}
     * will be thrown at that point in the iteration.</p>
     *
     * @param listOfAllPublicationsBD the list of domain objects to convert; must not be {@code null}
     * @return a {@link List} of {@code BibliographicalDataDTO_US023} in the same order as the input;
     *                  never {@code null}, but may be empty
     */
    public static List<BibliographicalDataDTO_US023> toDTOs(List<BibliographicalData> listOfAllPublicationsBD) {

        List<BibliographicalDataDTO_US023> listOfAllBiblioDataDTO = new ArrayList<>();

        for (BibliographicalData bd : listOfAllPublicationsBD) {
            listOfAllBiblioDataDTO.add(toDTO(bd));
        }

        return listOfAllBiblioDataDTO;
    }

    /**
     * Converts a single {@link BibliographicalData} domain object into a
     * {@code BibliographicalDataDTO_US023}.
     *
     * <p>Extracts the following fields from the domain object:</p>
     * <ul>
     *   <li><b>title</b> — from {@code getTitle()}</li>
     *   <li><b>genres</b> — each genre's title via {@code getTitle()}</li>
     * </ul>
     *
     * @param bdPublication the domain object to convert; must not be {@code null}
     * @return a new {@code BibliographicalDataDTO_US023} populated with data
     *         extracted from the given {@link BibliographicalData}
     * @throws NullPointerException if {@code bdPublication} is {@code null}
     */
    public static BibliographicalDataDTO_US023 toDTO(BibliographicalData bdPublication) {

        if (bdPublication == null) {
            throw new NullPointerException("Publication can not be null");
        }

        String publicationTitleDTO = bdPublication.getTitle();

        List<Author> authors = bdPublication.getAuthors();
        List<String> authorsDTO = new ArrayList<>();
        for (Author author : authors) {
            authorsDTO.add(author.getName());
        }

        List<Genre> genres = bdPublication.getGenres();
        List<String> genresDTO = new ArrayList<>();
        for (Genre genre : genres) {
            genresDTO.add(genre.getGenreType());
        }

        return new BibliographicalDataDTO_US023(publicationTitleDTO, authorsDTO, genresDTO);
    }
}