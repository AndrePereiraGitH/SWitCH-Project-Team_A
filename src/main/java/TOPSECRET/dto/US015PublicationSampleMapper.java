package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for US015: converts {@link PublicationSample} domain objects
 * into {@link US015PublicationSampleDTO} data transfer objects.
 *
 * <p>Because {@link PublicationSample} holds only IDs rather than resolved references,
 * this mapper queries three repositories to assemble each DTO:</p>
 * <ul>
 *   <li>{@link IBibliographicalDataRepository} – resolves the title</li>
 *   <li>{@link IAuthorRepository} – resolves each author ID to an author name</li>
 *   <li>{@link IEditionRepository} – resolves the publication type and code</li>
 * </ul>
 */
public class US015PublicationSampleMapper {

    private final IBibliographicalDataRepository _bdRepo;
    private final IAuthorRepository _authorRepo;
    private final IEditionRepository _editionRepo;

    // =================================================================================================================
    // =========== CONSTRUCTOR =========================================================================================
    // =================================================================================================================

    /**
     * Constructs a new mapper with the required repositories.
     *
     * @param bdRepo     the repository used to resolve bibliographical data; must not be {@code null}
     * @param authorRepo the repository used to resolve authors; must not be {@code null}
     * @param editionRepo the repository used to resolve editions; must not be {@code null}
     * @throws IllegalArgumentException if any of the provided repositories is {@code null}
     */
    public US015PublicationSampleMapper(IBibliographicalDataRepository bdRepo,IAuthorRepository authorRepo,
                                        IEditionRepository editionRepo) {

        if (bdRepo == null) {
            throw new IllegalArgumentException("BibliographicalDataRepo can not be null");
        }

        if (authorRepo == null) {
            throw new IllegalArgumentException("AuthorRepo can not be null");
        }

        if (editionRepo == null) {
            throw new IllegalArgumentException("EditionRepo can not be null");
        }

        this._bdRepo = bdRepo;
        this._authorRepo = authorRepo;
        this._editionRepo = editionRepo;
    }

    // =================================================================================================================
    // =========== CONVERSION METHODS ==================================================================================
    // =================================================================================================================

    /**
     * Converts a list of {@link PublicationSample} objects into a list of DTOs.
     *
     * @param publications the list of publication samples to convert; must not be {@code null}
     * @return a list of {@link US015PublicationSampleDTO}; never {@code null}, may be empty
     * @throws InstanceNotFoundException if any referenced bibliographical data, edition, or author
     *                                   cannot be found in their respective repositories
     */
    public List<US015PublicationSampleDTO> toDTOs(List<PublicationSample> publications) throws InstanceNotFoundException {

        if (publications == null) {
            throw new IllegalArgumentException("Publications list cannot be null");
        }

        List<US015PublicationSampleDTO> dtosList = new ArrayList<>();

        for (PublicationSample pub : publications) {
            US015PublicationSampleDTO pubDTO = toDTO(pub);
            dtosList.add(pubDTO);
        }
        return dtosList;
    }

    /**
     * Converts a single {@link PublicationSample} into a {@link US015PublicationSampleDTO}.
     *
     * @param publication the publication sample to convert; must not be {@code null}
     * @return a fully populated {@link US015PublicationSampleDTO}
     * @throws InstanceNotFoundException if any referenced bibliographical data, edition, or author
     *                                   cannot be found in their respective repositories
     */
    public US015PublicationSampleDTO toDTO(PublicationSample publication) throws InstanceNotFoundException {

        if (publication == null) {
            throw new IllegalArgumentException("PublicationSample cannot be null");
        }

        String titleDTO = getTitle(publication);
        List<String> authorsDTO = getAuthors(publication);
        String pubTypeDTO = getPublicationType(publication);
        String codeDTO = getCode(publication);

        US015PublicationSampleDTO pubDTO = new US015PublicationSampleDTO(titleDTO, authorsDTO, codeDTO, pubTypeDTO);

        return pubDTO;
    }

    // =================================================================================================================
    // =========== HELPERS (ENTITIES RESOLUTION) =======================================================================
    // =================================================================================================================

    /**
     * Resolves the {@link BibliographicalData} associated with the given publication.
     *
     * @param publication the publication whose bibliographical data is to be resolved
     * @return the resolved {@link BibliographicalData}
     * @throws InstanceNotFoundException if no bibliographical data is found for the publication's ID
     */
    private BibliographicalData getBibliographicalData(PublicationSample publication) throws InstanceNotFoundException {
        BibliographicalDataID bibliographicalDataId = publication.getBibliographicalDataID();
        BibliographicalData bd = _bdRepo.findBibliographicalDataById(bibliographicalDataId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("BibliographicalData id was not found:" + bibliographicalDataId));

        return bd;
    }

    /**
     * Resolves the {@link Edition} associated with the given publication.
     *
     * @param publication the publication whose edition is to be resolved
     * @return the resolved {@link Edition}
     * @throws InstanceNotFoundException if no edition is found for the publication's edition ID
     */
    private Edition getEdition(PublicationSample publication) throws InstanceNotFoundException {
        EditionID editionId = publication.getEditionID();
        Edition edition = _editionRepo.findEditionById(editionId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("Edition was not found:" + editionId));

        return edition;
    }

    // =================================================================================================================
    // =========== HELPERS - GETTERS ===================================================================================
    // =================================================================================================================

    /**
     * Extracts the title from the publication's bibliographical data.
     *
     * @param publication the publication sample
     * @return the publication title
     * @throws InstanceNotFoundException if the bibliographical data cannot be resolved
     */
    private String getTitle(PublicationSample publication) throws InstanceNotFoundException {
        BibliographicalData bd = getBibliographicalData(publication);
        String title = bd.getTitle();

        return title;
    }

    /**
     * Extracts the list of author names from the publication's bibliographical data.
     *
     * @param publication the publication sample
     * @return a list of author names; never {@code null}, may be empty
     * @throws InstanceNotFoundException if the bibliographical data or any author cannot be resolved
     */
    private List<String> getAuthors(PublicationSample publication) throws InstanceNotFoundException {
        BibliographicalData bd = getBibliographicalData(publication);
        List<AuthorId> listOfAuthorsId = bd.getAuthors();

        List<String> authorsName = new ArrayList<>();

        for (AuthorId id : listOfAuthorsId) {
            Author author = _authorRepo.findById(id)
                    .orElseThrow(() ->
                            new InstanceNotFoundException("Author was not found:" + id));

            String authorName = author.getName();
            authorsName.add(authorName);
        }

        return authorsName;
    }

    /**
     * Extracts the publication type media format from the publication's edition.
     *
     * @param publicationSample the publication sample
     * @return the media format string of the publication type
     * @throws InstanceNotFoundException if the edition cannot be resolved
     */
    private String getPublicationType(PublicationSample publicationSample) throws InstanceNotFoundException {
        Edition edition = getEdition(publicationSample);
        String pbType = edition.getMediaFormat();

        return pbType;
    }

    /**
     * Extracts the code from the publication's edition.
     *
     * @param publicationSample the publication sample
     * @return the publication code as a string
     * @throws InstanceNotFoundException if the edition cannot be resolved
     */
    private String getCode(PublicationSample publicationSample) throws InstanceNotFoundException {
        Edition edition = getEdition(publicationSample);
        String code = edition.getCode().toString();

        return code;
    }
}
