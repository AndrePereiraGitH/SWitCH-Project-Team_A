package TOPSECRET.dto;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mapper responsible for converting PublicationSample
 * domain objects into DTOs.
 *
 * <p>
 * This mapper performs only data transformation and does not
 * access any external dependencies.
 * </p>
 */
public class US018PublicationSampleMapper {

    /**
     * Converts a list of publication samples into DTOs.
     *
     * @param publications list of domain objects
     * @param titles list of titles corresponding to each publication
     * @return list of DTOs; returns empty list if input is null
     */
    public static List<US018PublicationSampleDTO> toDTOs(
            List<PublicationSample> publications,
            List<String> titles) {

        List<US018PublicationSampleDTO> result = new ArrayList<>();

        if (publications == null) {
            return result;
        }

        for (int i = 0; i < publications.size(); i++) {
            result.add(toDTO(publications.get(i), titles.get(i)));
        }

        return result;
    }

    /**
     * Converts a single publication sample into a DTO.
     *
     * @param publication domain object
     * @param title title associated with the publication
     * @return DTO representation
     *
     * @throws IllegalArgumentException if publication is null
     */
    public static US018PublicationSampleDTO toDTO(
            PublicationSample publication,
            String title) {

        if (publication == null) {
            throw new IllegalArgumentException("Publication cannot be null");
        }

        return new US018PublicationSampleDTO(
                publication.id(),
                title
        );
    }
}