package TOPSECRET.dto;

import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting {@link Listing} and {@link BibliographicalData}
 * domain objects into {@link US025DirectSaleDTO} data transfer objects.
 */
public class US025DirectSaleMapper {

    /**
     * Converts a {@link Listing}, its associated {@link BibliographicalData},
     * and a list of resolved author names into a {@link US025DirectSaleDTO}.
     *
     * @param listing     the listing to convert; must not be {@code null}
     * @param bd          the bibliographical data associated with the listing; must not be {@code null}
     * @param authorNames the list of author names resolved from the bibliographical data; must not be {@code null}
     * @return a new {@link US025DirectSaleDTO}
     */
    public US025DirectSaleDTO toDTO(Listing listing, BibliographicalData bd, List<String> authorNames) {
        String title = bd.getTitle();
        BigDecimal price = listing.getPrice().getPrice();
        String listingTitle = listing.getListingTitle();
        String seller = listing.getSellerId().toString();
        return new US025DirectSaleDTO(title, authorNames, price, listingTitle, seller);
    }

    /**
     * Converts a list of {@link Listing} objects into a list of {@link US025DirectSaleDTO},
     * resolving bibliographical data and author names for each listing.
     *
     * @param listings            the list of listings to convert; must not be {@code null}
     * @param bdRepository        repository for retrieving {@link BibliographicalData}
     * @param pubSampleRepository repository for retrieving {@link PublicationSample}
     * @param authorRepository    repository for retrieving authors
     * @return a list of {@link US025DirectSaleDTO}
     */
    public List<US025DirectSaleDTO> toDTOList(
            List<Listing> listings,
            IBibliographicalDataRepository bdRepository,
            IPublicationSampleRepository pubSampleRepository,
            IAuthorRepository authorRepository) {

        List<US025DirectSaleDTO> dtos = new ArrayList<>();
        for (Listing listing : listings) {
            PublicationSampleId firstPubId = listing.getItemsId().get(0);
            PublicationSample pub = pubSampleRepository.findById(firstPubId).orElseThrow();
            BibliographicalData bd = bdRepository.findById(pub.getBibliographicalDataID()).orElseThrow();
            List<String> authorNames = new ArrayList<>();
            for (AuthorId aId : bd.getAuthors()) {
                authorRepository.findById(aId).ifPresent(a -> authorNames.add(a.getName()));
            }
            dtos.add(toDTO(listing, bd, authorNames));
        }
        return dtos;
    }
}