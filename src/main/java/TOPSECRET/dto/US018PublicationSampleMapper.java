package TOPSECRET.dto;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mapper responsible for converting {@link PublicationSample}
 * domain objects into {@link US018PublicationSampleDTO}.
 *
 * <p>
 * This mapper is responsible for transforming domain objects into DTOs,
 * including retrieving additional data required for presentation purposes,
 * such as the publication title.
 * </p>
 *
 * <p>
 * The mapper depends on {@link IBibliographicalDataRepository} to fetch
 * {@link BibliographicalData} associated with each publication.
 * </p>
 */
public class US018PublicationSampleMapper {

    private final IBibliographicalDataRepository _bibliographicalRepo;

    /**
     * Constructs the mapper with the required repository dependency.
     *
     * @param bibliographicalRepo repository used to retrieve bibliographical data
     *
     * @throws IllegalArgumentException if repository is null
     */
    public US018PublicationSampleMapper(
            IBibliographicalDataRepository bibliographicalRepo) {

        if (bibliographicalRepo == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }

        this._bibliographicalRepo = bibliographicalRepo;
    }

    /**
     * Converts a list of {@link PublicationSample} domain objects into DTOs.
     *
     * <p>
     * For each publication, the mapper retrieves the associated
     * bibliographical data to extract the title.
     * </p>
     *
     * @param publications list of publication samples
     * @return list of DTOs; returns an empty list if input is null
     */
    public List<US018PublicationSampleDTO> toDTOs(
            List<PublicationSample> publications) {

        List<US018PublicationSampleDTO> result = new ArrayList<>();

        if (publications == null) {
            return result;
        }

        for (PublicationSample publication : publications) {
            result.add(toDTO(publication));
        }

        return result;
    }

    /**
     * Converts a single {@link PublicationSample} domain object into a DTO.
     *
     * <p>
     * The method retrieves the associated {@link BibliographicalData}
     * to obtain the publication title.
     * </p>
     *
     * @param publication domain object
     * @return DTO representation
     *
     * @throws IllegalArgumentException if publication is null
     */
    public US018PublicationSampleDTO toDTO(
            PublicationSample publication) {

        if (publication == null) {
            throw new IllegalArgumentException("Publication cannot be null");
        }

        BibliographicalDataID bibliographicalDataId =
                publication.getBibliographicalDataID();

        Optional<BibliographicalData> bibliographicalData =
                _bibliographicalRepo.findBibliographicalDataById(bibliographicalDataId);

        String title = bibliographicalData.get().getTitle();

        return new US018PublicationSampleDTO(
                publication.id(),
                title
        );
    }
}