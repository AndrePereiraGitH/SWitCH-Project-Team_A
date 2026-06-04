package TOPSECRET.dto.listing;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.response.ListingDetailsDTO;
import TOPSECRET.dto.listing.response.ListingResponseDTO;
import TOPSECRET.dto.listing.response.ListingResponseExploreDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * Mapper responsible for converting {@link Listing} domain objects into
 * {@link ListingResponseDTO} objects.
 */
@Service
public class ListingMapper {

    /**
     * Converts a {@link Listing} and its associated bibliographical data into a
     * {@link ListingResponseDTO}.
     *
     * @param listing the listing domain object to be converted
     * @param bibliographicalDatas the bibliographical data associated with the listing
     * @return a {@link ListingResponseDTO} containing the listing response data
     */
    public static ListingResponseDTO toResponseDTO(Listing listing, List<BibliographicalData> bibliographicalDatas) {

        return new ListingResponseDTO(
                listing.id().getId(),
                listing.getListingTitle(),
                bibliographicalDatas.stream().map(BibliographicalData::getTitle).toList(),
                listing.getSaleType().toString(),
                listing.getPrice().getPrice().toString(),
                listing.getShortDescription(),
                listing.getSellerId().getUserId(),
                listing.getPictures().stream().map(Picture::getPicture).toList(),
                listing.getPublicationDate()
        );
    }

    public static ListingResponseExploreDTO toResponseExploreDTO(Listing listing, List<BibliographicalData> bd, List<String> authors, Condition condition) {
        return new ListingResponseExploreDTO(
                listing.id().getId(),
                listing.getListingTitle(),
                authors,
                bd.get(0).getGenres().stream().map(GenreId::getID).toList(),
                listing.getPrice().getPrice().toString(),
                condition.getDescription(),
                listing.getSaleType().toString(),
                listing.getPictures().stream().findFirst().map(Picture::getPicture).orElse("")
        );
    }

    /**
     * Converts a listing and its related domain entities into a {@link ListingDetailsDTO}.
     *
     * <p>This method aggregates information from the listing, publication samples,
     * bibliographical data, editions, authors, genres, and publishers in order to
     * build a detailed representation suitable for API responses.</p>
     *
     * <p>When multiple editions, publication samples, or publishers are provided,
     * only the first available instance is used to populate edition-specific and
     * publisher-specific fields.</p>
     *
     * <p>Missing optional information is replaced with sensible default values
     * such as {@code "N/A"} or {@code "Unknown"}.</p>
     *
     * @param listing the listing to convert
     * @param bibliographicalDatas the bibliographical data associated with the listing
     * @param pubSamples the publication samples associated with the listing
     * @param editions the editions associated with the publication samples
     * @param authors the authors associated with the bibliographical data
     * @param genres the genres associated with the bibliographical data
     * @param publishers the publishers associated with the editions
     * @return a {@link ListingDetailsDTO} containing the aggregated listing details
     */
    public static ListingDetailsDTO toListingDetailsDTO(
            Listing listing,
            List<BibliographicalData> bibliographicalDatas,
            List<PublicationSample> pubSamples,
            List<Edition> editions,
            List<Author> authors,
            List<Genre> genres,
            List<Publisher> publishers
    ) {
        Edition edition = editions.stream()
                .findFirst()
                .orElse(null);

        Publisher publisher = publishers.stream()
                .findFirst()
                .orElse(null);

        PublicationSample sample = pubSamples.stream()
                .findFirst()
                .orElse(null);

        return new ListingDetailsDTO(
                listing.id().toString(),
                listing.getListingTitle(),
                authors.stream()
                        .map(Author::getName)
                        .distinct()
                        .toList(),
                genres.stream()
                        .map(Genre::getGenreType)
                        .distinct()
                        .toList(),
                listing.getPrice().getPrice(),
                sample != null && sample.getCondition() != null
                        ? sample.getCondition().toString()
                        : "Unknown",
                listing.getSaleType().toString(),
                listing.getPictures().stream()
                        .findFirst()
                        .map(Picture::getPicture)
                        .orElse(""),
                listing.getShortDescription(),
                listing.getSellerId().toString(),
                publisher != null
                        ? publisher.getName()
                        : "N/A",
                edition != null && edition.getCode() != null
                        ? edition.getCode().toString()
                        : "N/A",
                edition != null && edition.getEditionDate() != null
                        ? edition.getEditionDate().getYear()
                        : null,
                edition != null && edition.getPhysicalAttributes() != null
                        ? edition.getPhysicalAttributes().getNumberOfPages()
                        : null,
                edition != null && edition.getEditionLanguage() != null
                        ? edition.getEditionLanguage().getDisplayLanguage()
                        : "N/A"
        );
    }


    /**
     * Converts a string identifier into a {@link ListingId}.
     *
     * @param id the publication sample identifier as a string
     * @return a {@link ListingId} value object
     */
    public static ListingId toListingId(String id) {
        return new ListingId(id);
    }

    /**
     * Converts a string identifier into a {@link PublicationSampleId}.
     *
     * @param id the publication sample identifier as a string
     * @return a {@link PublicationSampleId} value object
     */
    public static PublicationSampleId toPublicationId(String id) {
        return new PublicationSampleId(id);
    }

    /**
     * Converts a string value into a {@link SaleType}.
     *
     * @param saleType the sale type as a string
     * @return the corresponding {@link SaleType}
     * @throws IllegalArgumentException if the sale type is unknown
     */
    public static SaleType toSaleType(String saleType) {
        return SaleType.fromString(saleType);
    }

    /**
     * Converts a string value into a {@link Price}.
     *
     * @param price the price value as a string
     * @return a {@link Price} value object
     * @throws NumberFormatException if the price is not a valid decimal number
     */
    public static Price toPrice (String price) {
        return new Price(new BigDecimal(price));
    }

    /**
     * Converts a string value into a {@link Picture}.
     *
     * @param picture the price value as a string
     * @return a {@link Picture} value object
     */
    public static Picture toPicture(String picture) {
        return new Picture(picture);
    }

    /**
     * Converts a string value into a {@link UserId}.
     *
     * @param userId the price value as a string
     * @return a {@link UserId} value object
     */
    public static UserId toUserId(String userId) {
        return new UserId(userId);
    }

    /**
     * Converts a string value into a {@link GenreId}.
     *
     * @param genreId the price value as a string
     * @return a {@link GenreId} value object
     */
    public static GenreId toGenreId(String genreId) {
        return new GenreId(genreId);
    }

    /**
     * Converts a string value into a {@link PublisherID}.
     *
     * @param publisherId the price value as a string
     * @return a {@link PublisherID} value object
     */
    public static PublisherID toPublisherId(String publisherId) {
        return new PublisherID(publisherId);
    }


}
