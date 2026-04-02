//package TOPSECRET.dto;
//
//import TOPSECRET.domain.Author.Author;
//import TOPSECRET.domain.BibliographicalData.BibliographicalData;
//import TOPSECRET.domain.Genre.Genre;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Data Transfer Object representing the bibliographical data of a publication
// * for use in the US024 use case.
// *
// * <p>Carries a flat, presentation-ready summary of a {@link BibliographicalData}
// * domain object, exposing only the title, author names, and genre titles
// * needed by the UI layer.</p>
// */
//public class BibliographicalDataDTO_US024 {
//
//    /** The title of the publication. */
//    public String _publicationTitle;
//
//    /** The names of the authors associated with the publication. */
//    public List<String> _authors;
//
//    /** The titles of the genres associated with the publication. */
//    public List<String> _publicationGenre;
//
//    /**
//     * Constructs a new {@code BibliographicalDataDTO_US024} with all required fields.
//     *
//     * @param publicationTitle the title of the publication; must not be {@code null}
//     * @param authors          the list of author names; must not be {@code null}
//     * @param publicationGenre the list of genre titles; must not be {@code null}
//     */
//    public BibliographicalDataDTO_US024(String publicationTitle,
//                                        List<String> authors,
//                                        List<String> publicationGenre) {
//        _publicationTitle = publicationTitle;
//        _authors = authors;
//        _publicationGenre = publicationGenre;
//    }
//
//    /**
//     * Converts a list of {@link BibliographicalData} domain objects into a list
//     * of {@code BibliographicalDataDTO_US024} instances.
//     *
//     * <p>Each element is converted individually via {@link #toDTO(BibliographicalData)}.
//     * If any element in the list is {@code null}, a {@link NullPointerException}
//     * will be thrown at that point in the iteration.</p>
//     *
//     * @param listOfAllPublicationBD the list of domain objects to convert;
//     *                               must not be {@code null}
//     * @return a {@link List} of {@code BibliographicalDataDTO_US024} in the same
//     *         order as the input; never {@code null}, but may be empty
//     */
//    public static List<BibliographicalDataDTO_US024> toDTOS(List<BibliographicalData> listOfAllPublicationBD) {
//
//        List<BibliographicalDataDTO_US024> listOfAllBiblioDataDTO = new ArrayList<>();
//
//        for (BibliographicalData bd : listOfAllPublicationBD) {
//            listOfAllBiblioDataDTO.add(toDTO(bd));
//        }
//
//        return listOfAllBiblioDataDTO;
//    }
//
//    /**
//     * Converts a single {@link BibliographicalData} domain object into a
//     * {@code BibliographicalDataDTO_US024}.
//     *
//     * <p>Extracts the following fields from the domain object:</p>
//     * <ul>
//     *   <li><b>title</b> — from {@code getTitle()}</li>
//     *   <li><b>authors</b> — each author's name via {@code getName()}</li>
//     *   <li><b>genres</b> — each genre's title via {@code getTitle()}</li>
//     * </ul>
//     *
//     * @param bdPublication the domain object to convert; must not be {@code null}
//     * @return a new {@code BibliographicalDataDTO_US024} populated with data
//     *         extracted from the given {@link BibliographicalData}
//     * @throws NullPointerException if {@code bdPublication} is {@code null}
//     */
//    public static BibliographicalDataDTO_US024 toDTO(BibliographicalData bdPublication) {
//
//        if (bdPublication == null) {
//            throw new NullPointerException("Publication can not be null");
//        }
//
//        String publicationTitleDTO = bdPublication.getTitle();
//
//        List<Author> authors = bdPublication.getAuthors();
//        List<String> authorsDTO = new ArrayList<>();
//        for (Author author : authors) {
//            authorsDTO.add(author.getName());
//        }
//
//        List<Genre> genres = bdPublication.getGenres();
//        List<String> genresDTO = new ArrayList<>();
//        for (Genre genre : genres) {
//            genresDTO.add(genre.getGenreType());
//        }
//
//        return new BibliographicalDataDTO_US024(publicationTitleDTO, authorsDTO, genresDTO);
//    }
//}