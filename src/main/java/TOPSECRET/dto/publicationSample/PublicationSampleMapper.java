package TOPSECRET.dto.publicationSample;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.publicationSample.response.PublicationDTO;
import TOPSECRET.dto.publicationSample.response.PublicationDetailsDTO;
import TOPSECRET.dto.publicationSample.response.PublicationReferenceDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Utility class for mapping between domain objects related to Publication Samples
 * and their corresponding Data Transfer Objects (DTOs), as well as converting
 * primitive types (Strings) to domain Value Objects (VOs).
 */
public class PublicationSampleMapper {

    private PublicationSampleMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    // -------------------------------------------------------------------------
    // toDetailsDTO
    // -------------------------------------------------------------------------

    /**
     * Maps domain entities to a {@link PublicationDetailsDTO}.
     *
     * @param publication the publication sample domain object
     * @param bd          the bibliographical data domain object
     * @param authors     the list of authors associated with the publication
     * @param edition     the edition details of the publication
     * @return a fully populated {@link PublicationDetailsDTO}
     * @throws NullPointerException if any of the provided parameters are null
     */
    public static PublicationDetailsDTO toDetailsDTO(
            PublicationSample publication,
            BibliographicalData bd,
            List<Author> authors,
            Edition edition) {

        Objects.requireNonNull(publication,"Publication cannot be null");
        Objects.requireNonNull(bd,"BibliographicalData cannot be null");
        Objects.requireNonNull(authors,"Authors cannot be null");
        Objects.requireNonNull(edition,"Edition cannot be null");

        return new PublicationDetailsDTO(
                publication.id().getID(),
                bd.getTitle(),
                authorNames(authors),
                edition.getCode().toString(),
                edition.getMediaFormat());
    }

    /**
     * Maps lists of domain entities to a list of {@link PublicationDetailsDTO}s.
     * <p>
     * Note: All input lists are expected to be of the same size and ordered
     * correspondingly.
     * </p>
     *
     * @param publications          the list of publication samples
     * @param bds                   the list of bibliographical data objects
     * @param authorsPerPublication the list containing lists of authors for each publication
     * @param editions              the list of edition objects
     * @return a list of {@link PublicationDetailsDTO}s
     * @throws NullPointerException if the publications list is null
     */
    public static List<PublicationDetailsDTO> toDetailsDTOs(
            List<PublicationSample> publications,
            List<BibliographicalData> bds,
            List<List<Author>> authorsPerPublication,
            List<Edition> editions) {

        Objects.requireNonNull(publications, "Publications list cannot be null");

        return IntStream.range(0, publications.size())
                .mapToObj(i -> toDetailsDTO(
                        publications.get(i),
                        bds.get(i),
                        authorsPerPublication.get(i),
                        editions.get(i)
                ))
                .toList();
    }

    // -------------------------------------------------------------------------
    // toPublicationDTO
    // -------------------------------------------------------------------------

    /**
     * Maps domain entities to a {@link PublicationDTO}.
     *
     * @param publication the publication sample domain object
     * @param bd          the bibliographical data domain object
     * @param authors     the list of authors associated with the publication
     * @param genres      the list of genres associated with the publication
     * @param owner       the user who owns the publication sample
     * @return a fully populated {@link PublicationDTO}
     * @throws NullPointerException if any of the provided parameters are null
     */
    public static PublicationDTO toPublicationDTO(
            PublicationSample publication,
            BibliographicalData bd,
            List<Author> authors,
            List<Genre> genres,
            User owner) {

        Objects.requireNonNull(publication,"Publication cannot be null");
        Objects.requireNonNull(bd, "BibliographicalData cannot be null");
        Objects.requireNonNull(authors,"Authors cannot be null");
        Objects.requireNonNull(genres,"Genres cannot be null");
        Objects.requireNonNull(owner,"Owner cannot be null");

        return new PublicationDTO(
                publication.id().getID(),
                bd.getTitle(),
                authorNames(authors),
                bd.getOriginalLanguage().toString(),
                genreTypes(genres),
                owner.getUserName(),
                publication.getCondition().toString());
    }

    /**
     * Maps lists of domain entities to a list of {@link PublicationDTO}s.
     * <p>
     * Note: All input lists are expected to be of the same size and ordered
     * correspondingly.
     * </p>
     *
     * @param publications          the list of publication samples
     * @param bds                   the list of bibliographical data objects
     * @param authorsPerPublication the list containing lists of authors for each publication
     * @param genresPerPublication  the list containing lists of genres for each publication
     * @param owners                the list of users who own the publication samples
     * @return a list of {@link PublicationDTO}s
     * @throws NullPointerException if the publications list is null
     */
    public static List<PublicationDTO> toPublicationDTOs(
            List<PublicationSample> publications,
            List<BibliographicalData> bds,
            List<List<Author>> authorsPerPublication,
            List<List<Genre>> genresPerPublication,
            List<User> owners) {

        Objects.requireNonNull(publications, "Publications list cannot be null");

        return IntStream.range(0, publications.size())
                .mapToObj(i -> toPublicationDTO(
                        publications.get(i),
                        bds.get(i),
                        authorsPerPublication.get(i),
                        genresPerPublication.get(i),
                        owners.get(i)
                ))
                .toList();
    }

    // -------------------------------------------------------------------------
    // toReferenceDTO
    // -------------------------------------------------------------------------

    /**
     * Maps domain entities to a {@link PublicationReferenceDTO}.
     *
     * @param publication the publication sample domain object
     * @param bd          the bibliographical data domain object
     * @return a fully populated {@link PublicationReferenceDTO}
     * @throws NullPointerException if any of the provided parameters are null
     */
    public static PublicationReferenceDTO toReferenceDTO(
            PublicationSample publication,
            BibliographicalData bd) {

        Objects.requireNonNull(publication,"Publication cannot be null");
        Objects.requireNonNull(bd,"BibliographicalData cannot be null");

        return new PublicationReferenceDTO(publication.id().getID(), bd.getTitle());
    }

    /**
     * Maps lists of domain entities to a list of {@link PublicationReferenceDTO}s.
     * <p>
     * Note: All input lists are expected to be of the same size and ordered
     * correspondingly.
     * </p>
     *
     * @param publications the list of publication samples
     * @param bds          the list of bibliographical data objects
     * @return a list of {@link PublicationReferenceDTO}s
     * @throws NullPointerException if the publications list is null
     */
    public static List<PublicationReferenceDTO> toReferenceDTOs(
            List<PublicationSample> publications,
            List<BibliographicalData> bds) {

        Objects.requireNonNull(publications, "Publications list cannot be null");

        return IntStream.range(0, publications.size())
                .mapToObj(i -> toReferenceDTO(
                        publications.get(i),
                        bds.get(i)
                ))
                .toList();
    }


    // -------------------------------------------------------------------------
    // toVO methods
    // -------------------------------------------------------------------------


    /**
     * Converts a string representation of a user ID into a {@code UserId} object.
     *
     * @param userId the string representing the user's ID
     * @return a new {@code UserId} instance corresponding to the provided string
     */
    public static UserId toUserId(String userId) {
        UserId id = new UserId(userId);
        return id;
    }

    /**
     * Converts a string ID into a {@link PublicationSampleId} value object.
     *
     * @param id the string representation of the publication sample ID
     * @return the {@link PublicationSampleId} value object
     */
    public static PublicationSampleId toPubId(String id){
        PublicationSampleId pubSampleId = new PublicationSampleId(id);
        return  pubSampleId;
    }

    /**
     * Converts a string ID into a {@link BibliographicalDataId} value object.
     *
     * @param id the string representation of the bibliographical data ID
     * @return the {@link BibliographicalDataId} value object
     */
    public static BibliographicalDataId toBdId(String id){
        BibliographicalDataId bdId = new BibliographicalDataId(id);
        return bdId;
    }

    /**
     * Converts a string ID into an {@link EditionId} value object.
     *
     * @param id the string representation of the edition ID
     * @return the {@link EditionId} value object
     */
    public static EditionId toEditionId(String id){
        EditionId editionId = new EditionId(id);
        return editionId;
    }

    /**
     * Converts a string representation to its corresponding {@link Condition} enum value.
     *
     * <p>Accepts both the human-readable description (e.g. {@code "Like New"})
     * and the enum name (e.g. {@code "LIKE_NEW"}), case-insensitively.</p>
     *
     * @param conditionString the string to convert; must not be {@code null}
     * @return the matching {@link Condition} enum value
     * @throws IllegalArgumentException if no matching {@link Condition} is found
     */
    public static Condition toCondition(String conditionString) {
        Condition condition = Condition.fromString(conditionString);
        return condition;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private static List<String> authorNames(List<Author> authors) {
        return authors.stream().map(Author::getName).toList();
    }

    private static List<String> genreTypes(List<Genre> genres) {
        return genres.stream().map(Genre::getGenreType).toList();
    }
}