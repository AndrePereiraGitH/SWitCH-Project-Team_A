package TOPSECRET.dto.bibliographicalData;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.bibliographicalData.response.ResponseBibliographicalDataDTO;

import javax.management.InstanceNotFoundException;
import java.util.*;

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
     * {@link ResponseBibliographicalDataDTO}.
     *
     * <p>The resulting DTO includes the bibliographical data identifier,
     * title, and associated author names.</p>
     *
     * @param bd the bibliographical data entity to convert
     * @param authorNames the list of author names associated with the entity
     * @return a {@link ResponseBibliographicalDataDTO} containing the mapped data
     * @throws IllegalArgumentException if the bibliographical data entity is null
     * @throws InstanceNotFoundException if a required related instance
     *         cannot be found during mapping
     */
    public static ResponseBibliographicalDataDTO toDTO(BibliographicalData bd, List<String> authorNames) throws InstanceNotFoundException {

        if (bd == null){
            throw new IllegalArgumentException("Bibliographical data cannot be null");
        }

        if(authorNames == null){
            throw new IllegalArgumentException("Author names cannot be null");
        }

        return new ResponseBibliographicalDataDTO(
                bd.id().getID(),
                bd.getTitle(),
                authorNames
        );
    }

    /**
     * Converts a list of {@link BibliographicalData} entities into a list of
     * {@code responseBibliographicalDataDTO} objects using the provided author names map.
     *
     * <p>For each bibliographical data entry, this method retrieves the associated
     * author names from the given map and delegates the conversion to
     * {@code toDTO(BibliographicalData, List<String>)}.</p>
     *
     * @param bibliographicalDataList the list of bibliographical data entities to convert
     * @param authorNamesMap a map containing the author names associated with each
     *                       {@link BibliographicalDataId}
     * @return an unmodifiable list of converted DTOs
     * @throws IllegalArgumentException if author names are not found for a bibliographical
     *                                  data entry
     * @throws InstanceNotFoundException if an error occurs while converting an entry
     */
    public static List<ResponseBibliographicalDataDTO> toDTOs(
            List<BibliographicalData> bibliographicalDataList,
            Map<BibliographicalDataId, List<String>> authorNamesMap) throws InstanceNotFoundException {

        Objects.requireNonNull(bibliographicalDataList, "Bibliographical data list can't be null");
        Objects.requireNonNull(authorNamesMap, "Author names map can't be null");

        List<ResponseBibliographicalDataDTO> result = new ArrayList<>();

        for (BibliographicalData bd : bibliographicalDataList) {
            List<String> authorNames = authorNamesMap.get(bd.id());

            if (authorNames == null) {
                throw new IllegalArgumentException(
                        "Author names not found for bibliographical data " + bd.id()
                );
            }
            result.add(toDTO(bd, authorNames));
        }
        return result;
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

    /**
     * Creates a {@link UserId} instance from the given string value.
     *
     * @param id the user identifier as a string
     * @return a new {@link UserId} instance
     */
    public static UserId toUserId (String id){ return new UserId(id); }

    /**
     * Creates a {@link BibliographicalDataId} instance from the given string value.
     *
     * @param id the user identifier as a string
     * @return a new {@link BibliographicalDataId} instance
     */
    public static BibliographicalDataId toBibliographicalDataId (String id) throws IllegalArgumentException {
        if (id == null){
            throw new IllegalArgumentException("Id can't be null");
        }
        return new BibliographicalDataId(id);
    }

    /**
     * Creates a {@link Locale} instance from the given string value.
     *
     * @param originalLanguage the language identifier as a string
     * @return a new {@link Locale} instance
     */
    public static Locale toLocale (String originalLanguage){
        return Locale.forLanguageTag(originalLanguage.toLowerCase());
    }
}
