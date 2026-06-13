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
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.List;



/**
 * Mapper responsible for converting {@link Listing} domain objects into
 * {@link ListingResponseDTO} objects.
 */
@Component
public class ListingMapper {

    private static final String UNKNOWN = "Unknown";
    private static final String NOT_AVAILABLE = "N/A";
    private static final String EMPTY = "";

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
     * @param pubSamples the publication samples associated with the listing
     * @param editions the editions associated with the publication samples
     * @param authors the authors associated with the bibliographical data
     * @param genres the genres associated with the bibliographical data
     * @param publishers the publishers associated with the editions
     * @return a {@link ListingDetailsDTO} containing the aggregated listing details
     */
    public static ListingDetailsDTO toListingDetailsDTO(
            Listing listing,
            List<PublicationSample> pubSamples,
            List<BibliographicalData> bds,
            List<Edition> editions,
            List<Author> authors,
            List<Genre> genres,
            List<Publisher> publishers
    ) {
        Edition edition = getFirstOrNull(editions);
        Publisher publisher = getFirstOrNull(publishers);
        PublicationSample sample = getFirstOrNull(pubSamples);

        return new ListingDetailsDTO(
                getListingId(listing),
                listing.getListingTitle(),
                getAuthorNames(authors),
                getGenreTypes(genres),
                getPrice(listing),
                listing.getPublicationDate(),
                getCondition(sample),
                getSaleType(listing),
                getFirstPicture(listing),
                listing.getShortDescription(),
                getSellerId(listing),
                getPublisherName(publisher),
                getEditionCode(edition),
                getEditionYear(edition),
                getNumberOfPages(edition),
                getEditionLanguage(edition),
                getBibliographicalDataIds(bds)
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

    /**
     * Converts a {@link String} to a {@link BibliographicalDataId}.
     *
     * @param id the string representation of the identifier; must not be {@code null}
     * @return the corresponding {@link BibliographicalDataId}
     */
    public static BibliographicalDataId toBibliographicalDataId(String id) {
        BibliographicalDataId bdId = new BibliographicalDataId(id);
        return bdId;
    }

    /**
     * Retrieves the listing identifier.
     *
     * @param listing the listing whose identifier should be extracted
     * @return the listing identifier as a string
     */
    private static String getListingId(Listing listing) {
        return listing.id().getId();
    }

    /**
     * Retrieves the seller identifier associated with the listing.
     *
     * @param listing the listing whose seller identifier should be extracted
     * @return the seller identifier as a string
     */
    private static String getSellerId(Listing listing) {
        return listing.getSellerId().getUserId();
    }

    /**
     * Retrieves the sale type of the listing.
     *
     * @param listing the listing whose sale type should be extracted
     * @return the sale type as a string
     */
    private static String getSaleType(Listing listing) {
        return listing.getSaleType().toString();
    }

    /**
     * Retrieves the listing price.
     *
     * @param listing the listing whose price should be extracted
     * @return the price as a string
     */
    private static String getPrice(Listing listing) {
        return listing.getPrice().getPrice().toString();
    }

    /**
     * Retrieves all picture URLs associated with the listing.
     *
     * @param listing the listing whose pictures should be extracted
     * @return a list containing the picture URLs
     */
    private static List<String> getPictures(Listing listing) {
        return listing.getPictures()
                .stream()
                .map(Picture::getPicture)
                .toList();
    }

    /**
     * Retrieves the first picture associated with the listing.
     *
     * <p>If the listing does not contain any pictures, an empty string is returned.</p>
     *
     * @param listing the listing whose first picture should be extracted
     * @return the first picture URL, or an empty string if none exists
     */
    private static String getFirstPicture(Listing listing) {
        return listing.getPictures()
                .stream()
                .findFirst()
                .map(Picture::getPicture)
                .orElse(EMPTY);
    }

    /**
     * Retrieves the titles of the provided bibliographical data records.
     *
     * @param bibliographicalDatas the bibliographical data records
     * @return a list containing the extracted titles
     */
    private static List<String> getBibliographicalTitles(List<BibliographicalData> bibliographicalDatas) {
        return bibliographicalDatas.stream()
                .map(BibliographicalData::getTitle)
                .toList();
    }


    private static List<String> getBibliographicalDataIds(List<BibliographicalData> bds) {
        return bds.stream()
                .map(BibliographicalData::id)
                .map(Object::toString)
                .toList();
    }

    /**
     * Retrieves the genre identifiers from the first bibliographical data record.
     *
     * @param bibliographicalDatas the bibliographical data records
     * @return a list containing the genre identifiers
     */
    private static List<String> getGenreIds(List<BibliographicalData> bibliographicalDatas) {
        return bibliographicalDatas.get(0)
                .getGenres()
                .stream()
                .map(GenreId::getID)
                .toList();
    }

    /**
     * Retrieves the distinct author names.
     *
     * @param authors the authors whose names should be extracted
     * @return a list containing the distinct author names
     */
    private static List<String> getAuthorNames(List<Author> authors) {
        return authors.stream()
                .map(Author::getName)
                .distinct()
                .toList();
    }

    /**
     * Retrieves the distinct genre types.
     *
     * @param genres the genres whose types should be extracted
     * @return a list containing the distinct genre types
     */
    private static List<String> getGenreTypes(List<Genre> genres) {
        return genres.stream()
                .map(Genre::getGenreType)
                .distinct()
                .toList();
    }

    /**
     * Retrieves the condition of a publication sample.
     *
     * <p>If the sample or its condition is unavailable, the value
     * {@code "Unknown"} is returned.</p>
     *
     * @param sample the publication sample
     * @return the condition description, or {@code "Unknown"} if unavailable
     */
    private static String getCondition(PublicationSample sample) {
        if (sample == null || sample.getCondition() == null) {
            return UNKNOWN;
        }

        return sample.getCondition().toString();
    }

    /**
     * Retrieves the publisher name.
     *
     * <p>If no publisher is provided, the value {@code "N/A"} is returned.</p>
     *
     * @param publisher the publisher
     * @return the publisher name, or {@code "N/A"} if unavailable
     */
    private static String getPublisherName(Publisher publisher) {
        if (publisher == null) {
            return NOT_AVAILABLE;
        }

        return publisher.getName();
    }

    /**
     * Retrieves the edition code.
     *
     * <p>If the edition or its code is unavailable, the value {@code "N/A"}
     * is returned.</p>
     *
     * @param edition the edition
     * @return the edition code, or {@code "N/A"} if unavailable
     */
    private static String getEditionCode(Edition edition) {
        if (edition == null || edition.getCode() == null) {
            return NOT_AVAILABLE;
        }

        return edition.getCode().toString();
    }

    /**
     * Retrieves the publication year of an edition.
     *
     * @param edition the edition
     * @return the publication year, or {@code null} if unavailable
     */
    private static Integer getEditionYear(Edition edition) {
        if (edition == null || edition.getEditionDate() == null) {
            return null;
        }

        return edition.getEditionDate().getYear();
    }

    /**
     * Retrieves the number of pages of an edition.
     *
     * @param edition the edition
     * @return the number of pages, or {@code null} if unavailable
     */
    private static Integer getNumberOfPages(Edition edition) {
        if (edition == null || edition.getPhysicalAttributes() == null) {
            return null;
        }

        return edition.getPhysicalAttributes().getNumberOfPages();
    }

    /**
     * Retrieves the language of an edition.
     *
     * <p>If the edition or its language is unavailable, the value
     * {@code "N/A"} is returned.</p>
     *
     * @param edition the edition
     * @return the language name, or {@code "N/A"} if unavailable
     */
    private static String getEditionLanguage(Edition edition) {
        if (edition == null || edition.getEditionLanguage() == null) {
            return NOT_AVAILABLE;
        }

        return edition.getEditionLanguage().getDisplayLanguage();
    }

    /**
     * Retrieves the first element of a list.
     *
     * <p>If the list is empty, {@code null} is returned.</p>
     *
     * @param list the source list
     * @param <T> the element type
     * @return the first element, or {@code null} if the list is empty
     */
    private static <T> T getFirstOrNull(List<T> list) {
        return list.stream()
                .findFirst()
                .orElse(null);
    }

}
