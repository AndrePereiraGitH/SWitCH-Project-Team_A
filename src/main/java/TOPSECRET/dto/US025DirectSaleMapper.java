package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting domain entities into {@link US025DirectSaleDTO} instances.
 *
 * <p>This mapper holds references to the repositories required to resolve domain objects
 * from their identifiers, keeping that responsibility out of the controller.
 */
public class US025DirectSaleMapper {

    private final IPublicationSampleRepository publicationSampleRepository;
    private final IBibliographicalDataRepository bibliographicalDataRepository;

    /**
     * Constructs a new {@code US025DirectSaleMapper} with the required repository dependencies.
     *
     * @param publicationSampleRepository   the repository for resolving publication samples; must not be {@code null}
     * @param bibliographicalDataRepository the repository for resolving bibliographical data; must not be {@code null}
     * @throws IllegalArgumentException if any parameter is {@code null}
     */
    public US025DirectSaleMapper(
            IPublicationSampleRepository publicationSampleRepository,
            IBibliographicalDataRepository bibliographicalDataRepository) {

        if (publicationSampleRepository == null)
            throw new IllegalArgumentException("PublicationSampleRepository cannot be null");
        if (bibliographicalDataRepository == null)
            throw new IllegalArgumentException("BibliographicalDataRepository cannot be null");

        this.publicationSampleRepository = publicationSampleRepository;
        this.bibliographicalDataRepository = bibliographicalDataRepository;
    }

    /**
     * Converts domain entities into a list of {@link US025DirectSaleDTO} instances.
     *
     * <p>Domain objects required for DTO construction are resolved internally using the
     * repositories provided at construction time.
     *
     * @param author   the author whose direct sale items are being retrieved; must not be {@code null}
     * @param listings the list of direct sale listings; must not be {@code null}
     * @param bdIds    the list of bibliographical data identifiers associated with the author; must not be {@code null}
     * @return a list of {@link US025DirectSaleDTO}; never {@code null}, may be empty
     * @throws IllegalArgumentException if any parameter is {@code null}
     */
    public List<US025DirectSaleDTO> toDTOs(
            Author author,
            List<Listing> listings,
            List<BibliographicalDataID> bdIds) {

        if (author == null)
            throw new IllegalArgumentException("Author cannot be null");
        if (listings == null)
            throw new IllegalArgumentException("Listings cannot be null");
        if (bdIds == null)
            throw new IllegalArgumentException("BibliographicalDataIds cannot be null");

        List<PublicationSample> samples = publicationSampleRepository
                .findPublicationSampleByBibliographicalDataIds(bdIds);
        List<BibliographicalData> bds = bibliographicalDataRepository
                .findBibliographicalDataByAuthorId(author.id());

        List<US025DirectSaleDTO> result = new ArrayList<>();

        for (Listing listing : listings) {
            for (PublicationSampleId psId : listing.getItemsId()) {
                PublicationSample ps = findSampleById(psId, samples);
                if (ps == null) continue;

                BibliographicalData bd = findBdForSample(ps, bds);
                if (bd == null) continue;

                result.add(new US025DirectSaleDTO(
                        bd.getTitle(),
                        List.of(author.getName()),
                        listing.getPrice().getPrice().doubleValue(),
                        listing.getListingTitle(),
                        listing.getSellerId().toString()
                ));
            }
        }
        return result;
    }

    /**
     * Returns the {@link PublicationSample} from the given list whose identifier matches
     * the provided {@link PublicationSampleId}, or {@code null} if no match is found.
     *
     * @param psId    the identifier to look up; must not be {@code null}
     * @param samples the list of publication samples to search; must not be {@code null}
     * @return the matching {@link PublicationSample}, or {@code null} if not found
     */
    private PublicationSample findSampleById(PublicationSampleId psId, List<PublicationSample> samples) {
        for (PublicationSample ps : samples) {
            if (ps.id().equals(psId)) return ps;
        }
        return null;
    }

    /**
     * Returns the {@link BibliographicalData} from the given list whose identifier matches
     * the bibliographical data ID of the provided {@link PublicationSample}, or {@code null} if no match is found.
     *
     * @param ps  the publication sample whose bibliographical data ID is used for lookup; must not be {@code null}
     * @param bds the list of bibliographical data to search; must not be {@code null}
     * @return the matching {@link BibliographicalData}, or {@code null} if not found
     */
    private BibliographicalData findBdForSample(PublicationSample ps, List<BibliographicalData> bds) {
        for (BibliographicalData bd : bds) {
            if (bd.id().equals(ps.getBibliographicalDataID())) return bd;
        }
        return null;
    }
}