package TOPSECRET.dto.bibliographicalData;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for mapping {@link BibliographicalData}
 * domain entities into DTO objects.
 */
public final class BibliographicalDataMapper {

    private BibliographicalDataMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts a {@link BibliographicalData} entity into a
     * {@link responseBibliographicalDataDTO}.
     *
     * <p>The resulting DTO includes the bibliographical data identifier,
     * title, and associated author names.</p>
     *
     * @param bd the bibliographical data entity to convert
     * @param authorNames the list of author names associated with the entity
     * @return a {@link responseBibliographicalDataDTO} containing the mapped data
     * @throws IllegalArgumentException if the bibliographical data entity is null
     * @throws InstanceNotFoundException if a required related instance
     *         cannot be found during mapping
     */
    public static responseBibliographicalDataDTO toDTO(BibliographicalData bd, List<String> authorNames) throws InstanceNotFoundException {

        if (bd == null){
            throw new IllegalArgumentException("Bibliographical data cannot be null");
        }

        if(authorNames == null){
            throw new IllegalArgumentException("Author names cannot be null");
        }

        return new responseBibliographicalDataDTO(
                bd.id().getID(),
                bd.getTitle(),
                authorNames
        );
    }

    /**
     * Converts a list of author names into a list of {@link AuthorId} objects.
     *
     * <p>Each author name in the provided list is used to create a corresponding
     * {@link AuthorId} instance.</p>
     *
     * @param authorNames the list of author names to convert
     * @return a list of {@link AuthorId} objects created from the provided names
     */
    public static List<AuthorId> toAuthorId (List<String> authorNames){
        List<AuthorId> authorIds = new ArrayList<>();

        for(String authorName : authorNames){
            authorIds.add(new AuthorId(authorName));
        }
        return authorIds;
    }

    /**
     * Converts a list of genre names into a list of {@link GenreId} objects.
     *
     * <p>Each genre name in the provided list is used to create a corresponding
     * {@link GenreId} instance.</p>
     *
     * @param genreNames the list of genre names to convert
     * @return a list of {@link GenreId} objects created from the provided names
     */
    public static List<GenreId> toGenreId (List<String> genreNames){
        List<GenreId> genreIds = new ArrayList<>();

        for(String genreName : genreNames){
            genreIds.add(new GenreId(genreName));
        }
        return genreIds;
    }

}
