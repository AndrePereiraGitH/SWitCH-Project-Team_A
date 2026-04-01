package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) for User Story US016.
 * <p>
 * Represents a simplified view of a {@link PublicationSample} containing only the fields
 * required by the US016 use case.
 */
public class PublicationSampleDTO_US016 {
    private String title;
    private List<String> authorName;
    private Locale language;
    private List<String> genreType;
    private String ownerName;
    private String condition;


    public PublicationSampleDTO_US016(String title, List<String> authorName, Locale language, List<String> genreType, String ownerName, String condition) {
        this.title = title;
        this.authorName = authorName;
        this.language = language;
        this.genreType = genreType;
        this.ownerName = ownerName;
        this.condition = condition;
    }

    /**
     * Maps a {@link PublicationSample} domain object to a {@link PublicationSampleDTO_US016}.
     *
//     * @param publicationSample the domain object to convert
     * @return a DTO containing the fields required by US016
     */
//    public static PublicationSampleDTO_US016 toDTO(PublicationSample publicationSample) {
//        return new PublicationSampleDTO_US016(
//                publicationSample.getBibliographicalData().getTitle(),
//                publicationSample.getBibliographicalData().getAuthors().stream()
//                        .map(a -> a.getName())
//                        .toList(),
//                publicationSample.getBibliographicalData().getOriginalLanguage(),
//                publicationSample.getBibliographicalData().getGenres().stream()
//                        .map(Genre::getGenreType)
//                        .toList(),
//                publicationSample.getOwnerId().getUserName(),
//                publicationSample.getCondition().getDescription()
//        );
//    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthorName() {
        return authorName;
    }

    public Locale getLanguage() {
        return language;
    }

    public List<String> getGenreType() {
        return genreType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicationSampleDTO_US016 that = (PublicationSampleDTO_US016) o;
        return Objects.equals(title, that.title) && Objects.equals(authorName, that.authorName) && Objects.equals(language, that.language) && Objects.equals(genreType, that.genreType) && Objects.equals(ownerName, that.ownerName) && Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authorName, language, genreType, ownerName, condition);
    }
}
