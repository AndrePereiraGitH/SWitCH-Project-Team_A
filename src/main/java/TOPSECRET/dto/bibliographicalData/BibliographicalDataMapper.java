package TOPSECRET.dto.bibliographicalData;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
import TOPSECRET.services.BibliographicalDataService;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting {@link BibliographicalData}
 * domain objects into DTO representations.
 */
public class BibliographicalDataMapper {

    private final BibliographicalDataService service;

    /**
     * Creates a new bibliographical data mapper.
     *
     * @param service used to retrieve authors
     * @throws NullPointerException if the service is null
     */
    public BibliographicalDataMapper(BibliographicalDataService service) {
        if(service == null){
            throw new NullPointerException("Service cannot be null");
        }
        this.service = service;
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
                service.getAuthors(bd)
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
}
