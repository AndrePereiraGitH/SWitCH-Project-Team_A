package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting domain objects of type {@link BibliographicalData}
 * into their corresponding DTOs used in the US023 use case.
 * <p>
 * This mapper requires an {@link IAuthorRepository} to resolve the names of the
 * authors referenced in the bibliographical data.
 */
public class US023BibliographicalDataMapper {

    private final IAuthorRepository _authorRepo;

    // =================================================================================================================
    // ======= Constructor =============================================================================================
    // =================================================================================================================

    /**
     * Creates a new instance of the mapper.
     *
     * @param authorRepo the repository used to resolve author information
     * @throws NullPointerException if {@code authorRepo} is {@code null}
     */
    public US023BibliographicalDataMapper(IAuthorRepository authorRepo) {
        this._authorRepo = authorRepo;
    }


    // =================================================================================================================
    // ====== CONVERSION METHODS =======================================================================================
    // =================================================================================================================

    /**
     * Converts a list of {@link BibliographicalData} domain objects into a list of
     * {@link US023BibliographicalDataDTO} objects.
     *
     * @param listOfAllPublicationsBD the list of bibliographical data to convert
     * @return a list of DTOs representing the provided bibliographical data
     * @throws InstanceNotFoundException if any referenced author cannot be resolved
     * @throws NullPointerException if {@code listOfAllPublicationsBD} is {@code null}
     */
    public List<US023BibliographicalDataDTO> toDTOs(List<BibliographicalData> listOfAllPublicationsBD)
            throws InstanceNotFoundException {

        List<US023BibliographicalDataDTO> listOfAllBiblioDataDTO = new ArrayList<>();

        for (BibliographicalData bd : listOfAllPublicationsBD) {
            listOfAllBiblioDataDTO.add(toDTO(bd));
        }

        return listOfAllBiblioDataDTO;
    }

    /**
     * Converts a single {@link BibliographicalData} domain object into its
     * corresponding {@link US023BibliographicalDataDTO}.
     *
     * @param bdPublication the bibliographical data to convert
     * @return the DTO representation of the provided bibliographical data
     * @throws InstanceNotFoundException if any referenced author cannot be resolved
     * @throws NullPointerException if {@code bdPublication} is {@code null}
     */
    public US023BibliographicalDataDTO toDTO(BibliographicalData bdPublication) throws InstanceNotFoundException {

        if (bdPublication == null) {
            throw new NullPointerException("Publication can not be null");
        }

        BibliographicalDataID bdId = bdPublication.id();
        String titleDTO = bdPublication.getTitle();
        List<String> authorsDTO = getAuthors(bdPublication);

        US023BibliographicalDataDTO bdDTO = new US023BibliographicalDataDTO(bdId, titleDTO, authorsDTO);

        return bdDTO;
    }


    // =================================================================================================================
    // ====== PRIVATE HELPERS ==========================================================================================
    // =================================================================================================================

    /**
     * Extracts the list of author names associated with the given bibliographical data.
     *
     * @param bd the bibliographical data containing the author identifiers
     * @return a list of author names; never {@code null}, may be empty
     * @throws InstanceNotFoundException if any referenced author cannot be found
     * @throws NullPointerException if {@code bd} is {@code null}
     */
    private List<String> getAuthors(BibliographicalData bd) throws InstanceNotFoundException {

        List<AuthorId> listOfAuthorsId = bd.getAuthors();

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
}