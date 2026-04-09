package TOPSECRET.dto;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;

import javax.management.InstanceNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper responsible for converting {@link PublicationSample} domain objects
 * into {@link US016PublicationSampleDTO} instances.
 * <p>
 * It uses the bibliographical data repository to obtain the bibliographical data
 * associated with each publication sample before performing the conversion.
 */
public class US016PublicationSampleMapper {

    private final IBibliographicalDataRepository _bdRepository;

    /**
     * Creates a new mapper with access to the bibliographical data repository.
     *
     * @param bdRepo repository used to retrieve bibliographical data
     */
    public US016PublicationSampleMapper(IBibliographicalDataRepository bdRepo) {
        this._bdRepository = bdRepo;
    }

    /**
     * Converts a publication sample and its corresponding bibliographical data
     * into a DTO for presentation.
     *
     * @param publicationSample the publication sample to be mapped
     * @param bd the bibliographical data associated with the publication sample
     * @return a DTO containing the mapped publication sample information
     */
    public static US016PublicationSampleDTO fromDomain(PublicationSample publicationSample, BibliographicalData bd) {

        return new US016PublicationSampleDTO(
                publicationSample.id(),
                bd.getTitle(),
                bd.getAuthors().stream().map(AuthorId::getId).toList(),
                bd.getOriginalLanguage(),
                bd.getGenres().stream().map(GenreID::getID).toList(),
                publicationSample.getOwnerID(),
                publicationSample.getCondition().toString()
        );
    }

    /**
     * Converts a list of publication samples into a list of DTOs.
     * <p>
     * For each publication sample, the corresponding bibliographical data is retrieved
     * from the repository and then used to build the DTO representation.
     *
     * @param publicationSamples the publication samples to be mapped
     * @return a list of DTOs containing the mapped publication sample information
     * @throws InstanceNotFoundException if the bibliographical data for any publication sample
     * cannot be found
     */
    public List<US016PublicationSampleDTO> fromDomainList(List<PublicationSample> publicationSamples) throws InstanceNotFoundException {

        Map<BibliographicalDataID, BibliographicalData> bibliographicalDataMap = new HashMap<>();

        for (PublicationSample pub : publicationSamples) {
            BibliographicalData bd = _bdRepository
                    .findBibliographicalDataById(pub.getBibliographicalDataID())
                    .orElseThrow(() -> new InstanceNotFoundException(
                            "BibliographicalData not found for id: " + pub.getBibliographicalDataID()
                    ));

            bibliographicalDataMap.put(pub.getBibliographicalDataID(), bd);
        }

        return publicationSamples.stream()
                .map(pub -> fromDomain(pub, bibliographicalDataMap.get(pub.getBibliographicalDataID())))
                .toList();
    }
}
