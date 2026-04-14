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
import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for US023: converts {@link PublicationSample} domain objects
 * into {@link US023PublicationSampleDTO} data transfer objects.
 *
 * <p>Because {@link PublicationSample} holds only IDs rather than resolved references,
 * this mapper queries three repositories to assemble each DTO:</p>
 * <ul>
 *   <li>{@link IBibliographicalDataRepository} – resolves the title and author IDs</li>
 *   <li>{@link IAuthorRepository} – resolves each author ID to an author name</li>
 *   <li>{@link IEditionRepository} – resolves the publication type and code</li>
 * </ul>
 */
public class US023PublicationSampleMapper {

    private final IBibliographicalDataRepository _bdRepo;
    private final IAuthorRepository _authorRepo;
    private final IEditionRepository _editionRepo;

    
    // =================================================================================================================
    // ============ CONSTRUCTOR ========================================================================================
    // =================================================================================================================
    
    /**
     * Constructs a new mapper with the required repositories.
     *
     * @param bdRepo     the repository used to resolve bibliographical data; must not be {@code null}
     * @param authorRepo the repository used to resolve authors; must not be {@code null}
     * @param editionRepo the repository used to resolve editions; must not be {@code null}
     * @throws NullPointerException if any of the provided repositories is {@code null}
     */
    public US023PublicationSampleMapper(IBibliographicalDataRepository bdRepo, IAuthorRepository authorRepo, 
                                        IEditionRepository editionRepo) {

        if (bdRepo == null) {
            throw new NullPointerException("BibliographicalDataRepo can not be null");
        }
        if (authorRepo == null) {
            throw new NullPointerException("AuthorRepo can not be null");
        }
        if (editionRepo == null) {
            throw new NullPointerException("EditionRepo can not be null");
        }

        _bdRepo = bdRepo;
        _authorRepo = authorRepo;
        _editionRepo = editionRepo;
    }

    
    // =================================================================================================================
    // ============ CONVERSION METHODS =================================================================================
    // =================================================================================================================

    /**
     * Converts a list of {@link PublicationSample} objects into a list of DTOs.
     *
     * @param publications the list of publication samples to convert; must not be {@code null}
     * @return a list of {@link US023PublicationSampleDTO}; never {@code null}, may be empty
     * @throws InstanceNotFoundException if any referenced bibliographical data, edition, or author
     *                                   cannot be found in their respective repositories
     */
    public List<US023PublicationSampleDTO> toDTOs(List<PublicationSample> publications) throws InstanceNotFoundException {
        List<US023PublicationSampleDTO> listOfPublicationsDTO = new ArrayList<>();

        for (PublicationSample pub : publications) {
            US023PublicationSampleDTO pubDTO = toDTO(pub);
            listOfPublicationsDTO.add(pubDTO);
        }

        return listOfPublicationsDTO;
    }

    /**
     * Converts a single {@link PublicationSample} into a {@link US023PublicationSampleDTO}.
     *
     * @param publicationSample the publication sample to convert; must not be {@code null}
     * @return a fully populated {@link US023PublicationSampleDTO}
     * @throws InstanceNotFoundException if any referenced bibliographical data, edition, or author
     *                                   cannot be found in their respective repositories
     */
    public US023PublicationSampleDTO toDTO(PublicationSample publicationSample) throws InstanceNotFoundException {

        String titleDTO = getTitle(publicationSample);
        List<String> authorsDTO = getAuthors(publicationSample);
        String codeDTO = getCode(publicationSample);
        String pubTypeDTO = getPublicationType(publicationSample);
        String sellerNameDTO = publicationSample.getOwnerID().toString();
        String pubStatusDTO = publicationSample.getPublicationStatus().toString();
        String conditionDTO = publicationSample.getCondition().toString();

        US023PublicationSampleDTO pubDTO = new US023PublicationSampleDTO(titleDTO, authorsDTO, codeDTO, pubTypeDTO,
                sellerNameDTO, pubStatusDTO, conditionDTO);

        return pubDTO;
    }


    // =================================================================================================================
    // ============ PRIVATE HELPERS ====================================================================================
    // =================================================================================================================

    /**
     * Resolves the {@link BibliographicalData} associated with the given publication.
     *
     * @param publicationSample the publication whose bibliographical data is to be resolved
     * @return the resolved {@link BibliographicalData}
     * @throws InstanceNotFoundException if no bibliographical data is found for the publication's ID
     */
    private BibliographicalData getBibliographicalData(PublicationSample publicationSample) throws InstanceNotFoundException {
        BibliographicalDataID bibliographicalDataId = publicationSample.getBibliographicalDataID();
        BibliographicalData bibliographicalData = _bdRepo.findBibliographicalDataById(bibliographicalDataId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("BibliographicalData id was not found:" + bibliographicalDataId));

        return bibliographicalData;
    }

    /**
     * Resolves the {@link Edition} associated with the given publication.
     *
     * @param publicationSample the publication whose edition is to be resolved
     * @return the resolved {@link Edition}
     * @throws InstanceNotFoundException if no edition is found for the publication's edition ID
     */
    private Edition getEdition(PublicationSample publicationSample) throws InstanceNotFoundException {
        EditionID editionId = publicationSample.getEditionID();
        Edition edition = _editionRepo.findEditionById(editionId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("Edition was not found:" + editionId));

        return edition;
    }

    /**
     * Extracts the title from the publication's bibliographical data.
     *
     * @param publication the publication sample
     * @return the publication title
     * @throws InstanceNotFoundException if the bibliographical data cannot be resolved
     */
    private String getTitle(PublicationSample publication) throws InstanceNotFoundException {
        BibliographicalData bibliographicalData = getBibliographicalData(publication);
        String title = bibliographicalData.getTitle();

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
        BibliographicalData bibliographicalData = getBibliographicalData(publication);
        List<AuthorId> listOfAuthorsId = bibliographicalData.getAuthors();

        List<String> authorsName = new ArrayList<>();

        for (AuthorId id : listOfAuthorsId) {
            Author author = _authorRepo.findAuthorById(id)
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
