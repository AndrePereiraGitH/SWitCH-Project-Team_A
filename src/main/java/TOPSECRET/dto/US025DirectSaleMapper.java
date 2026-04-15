package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting Listing, PublicationSample and BibliographicalData entities into DirectSaleDTO_US025.
 */
public class US025DirectSaleMapper {

    private US025DirectSaleMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts domain entities into a list of {@link US025DirectSaleDTO} instances.
     *
     * @param author   the author whose direct sale items are being retrieved; must not be {@code null}
     * @param listings the list of direct sale listings; must not be {@code null}
     * @param samples  the list of publication samples associated with the listings; must not be {@code null}
     * @param bds      the list of bibliographical data associated with the samples; must not be {@code null}
     * @return a list of {@link US025DirectSaleDTO}; never {@code null}, may be empty
     * @throws IllegalArgumentException if any parameter is {@code null}
     */
    public static List<US025DirectSaleDTO> toDTO(Author author, List<Listing> listings, List<PublicationSample> samples, List<BibliographicalData> bds) {

        if (author == null)
            throw new IllegalArgumentException("Author cannot be null");
        if (listings == null)
            throw new IllegalArgumentException("Listings cannot be null");
        if (samples == null)
            throw new IllegalArgumentException("PublicationSamples cannot be null");
        if (bds == null)
            throw new IllegalArgumentException("BibliographicalData list cannot be null");

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
     * Returns the {@link PublicationSample} from the given list whose identifier matches the provided
     * {@link PublicationSampleId}, or {@code null} if no match is found.
     *
     * @param psId    the identifier to look up; must not be {@code null}
     * @param samples the list of publication samples to search; must not be {@code null}
     * @return the matching {@link PublicationSample}, or {@code null} if not found
     */
    private static PublicationSample findSampleById(PublicationSampleId psId, List<PublicationSample> samples) {
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
    private static BibliographicalData findBdForSample(PublicationSample ps, List<BibliographicalData> bds) {
        for (BibliographicalData bd : bds) {
            if (bd.id().equals(ps.getBibliographicalDataID())) return bd;
        }
        return null;
    }
}