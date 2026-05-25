package TOPSECRET.dto.bibliographicalData;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting {@link BibliographicalData}
 * domain objects into DTO representations.
 */
public class BibliographicalDataMapper {

    private final IAuthorRepository _authorRepo;

    /**
     * Creates a new bibliographical data mapper.
     *
     * @param authorRepo repository used to retrieve authors
     * @throws NullPointerException if the author repository is null
     */
    public BibliographicalDataMapper(IAuthorRepository authorRepo) {
        if(authorRepo == null){
            throw new NullPointerException("Author repository cannot be null");
        }
        this._authorRepo = authorRepo;
    }


    /**
     * Converts a {@link BibliographicalData} domain object into
     * a {@link responseBibliographicalDataDTO}.
     *
     * @param bd the bibliographical data domain object
     * @return the corresponding response DTO
     * @throws IllegalArgumentException if bibliographical data is null
     * @throws InstanceNotFoundException if an author associated with the
     *                                   bibliographical data cannot be found
     */
    public responseBibliographicalDataDTO toDTO(BibliographicalData bd) throws InstanceNotFoundException {

        if (bd == null){
            throw new IllegalArgumentException("Bibliographical data cannot be null");
        }

        return new responseBibliographicalDataDTO(
                bd.id().getID(),
                bd.getTitle(),
                getAuthors(bd)
        );
    }

    /**
     * Converts a list of {@link BibliographicalData} domain objects
     * into a list of {@link responseBibliographicalDataDTO}.
     *
     * @param bds list of bibliographical data domain objects
     * @return list of corresponding response DTOs
     * @throws IllegalArgumentException if the list is null
     * @throws InstanceNotFoundException if an author associated with any
     *                                   bibliographical data cannot be found
     */
    public List<responseBibliographicalDataDTO> toDTOs(List<BibliographicalData> bds) throws InstanceNotFoundException {

        if (bds == null) {
            throw new IllegalArgumentException("Bibliographical data cannot be null");
        }

        List<responseBibliographicalDataDTO> result = new ArrayList<>();

        for (BibliographicalData bd : bds) {
            result.add(toDTO(bd));
        }

        return result;
    }


    // =================================================================================================================
    // ====== PRIVATE HELPERS ==========================================================================================
    // =================================================================================================================

    /**
     * Retrieves the names of all authors associated with the given
     * bibliographical data object.
     *
     * @param bd the bibliographical data object
     * @return list of author names
     * @throws InstanceNotFoundException if an author cannot be found
     */
    private List<String> getAuthors(BibliographicalData bd) throws InstanceNotFoundException {

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
}
