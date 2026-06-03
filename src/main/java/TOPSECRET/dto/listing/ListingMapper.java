package TOPSECRET.dto.listing;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.response.ListingResponseDTO;
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
