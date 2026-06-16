package TOPSECRET.dto.transaction;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.transaction.response.TransactionResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Mapper responsible for converting {@link Transaction} domain objects into
 * {@link TransactionResponseDTO} objects, and for converting raw request
 * values into domain value objects.
 */
public class TransactionMapper {

    /**
     * Converts a {@link Transaction} domain object into a {@link TransactionResponseDTO}.
     *
     * @param transaction the transaction to convert; must not be {@code null}
     * @return a fully populated {@link TransactionResponseDTO}
     */
    public static TransactionResponseDTO toResponseDTO(Transaction transaction) {

        return new TransactionResponseDTO(
                transaction.id().getId(),
                transaction.getBuyerId().getUserId(),
                transaction.getPrice().getPrice().toString(),
                transaction.getTransactionDate().toString(),
                transaction.getShippingAddress().getStreet(),
                transaction.getShippingAddress().getNumber(),
                transaction.getShippingAddress().getZipCode(),
                transaction.getShippingAddress().getCityID().getCityID(),
                transaction.getPaymentDetails().cardHolderName(),
                transaction.getPaymentDetails().maskedCardNumber(),
                transaction.getPaymentDetails().expiryMonth(),
                transaction.getPaymentDetails().expiryYear(),
                transaction.getStatus().toString(),
                transaction.getListingIds().stream()
                        .map(ListingId::getId)
                        .toList()
        );
    }

    /**
     * Converts a string identifier into a {@link TransactionID}.
     *
     * @param id the transaction identifier as a string; must not be {@code null} or blank
     * @return the corresponding {@link TransactionID}
     */
    public static TransactionID toTransactionId(String id) {
        return new TransactionID(id);
    }

    /**
     * Converts a string identifier into a {@link UserId}.
     *
     * @param userId the user identifier as a string; must not be {@code null} or blank
     * @return the corresponding {@link UserId}
     */
    public static UserId toUserId(String userId) {
        return new UserId(userId);
    }

    /**
     * Converts a string value into a {@link Price}.
     *
     * @param price the price value as a string; must not be {@code null}
     * @return the corresponding {@link Price}
     * @throws NumberFormatException if the price is not a valid decimal number
     */
    public static Price toPrice(String price) {
        return new Price(new BigDecimal(price));
    }

    /**
     * Converts a string value into a {@link LocalDateTime}.
     *
     * @param transactionDate the date-time as a string in ISO format; must not be {@code null}
     * @return the corresponding {@link LocalDateTime}
     */
    public static LocalDateTime toTransactionDate(String transactionDate) {
        return LocalDateTime.parse(transactionDate);
    }

    /**
     * Converts address fields into an {@link Address} value object.
     *
     * @param street  the street name; must not be {@code null} or blank
     * @param number  the address number; must not be {@code null} or blank
     * @param zipCode the zip code; must not be {@code null} or blank
     * @param cityId  the city identifier; must not be {@code null} or blank
     * @return the corresponding {@link Address}
     */
    public static Address toAddress(String street, String number, String zipCode, String cityId) {
        return new Address(street, number, zipCode, new CityId(cityId));
    }

    /**
     * Converts payment detail fields into a {@link PaymentDetails} value object.
     *
     * @param cardHolderName   the name on the card; must not be {@code null} or blank
     * @param maskedCardNumber the masked card number; must not be {@code null} or blank
     * @param expiryMonth      the expiry month; must not be {@code null} or blank
     * @param expiryYear       the expiry year; must not be {@code null} or blank
     * @return the corresponding {@link PaymentDetails}
     */
    public static PaymentDetails toPaymentDetails(String cardHolderName, String maskedCardNumber,
                                                  String expiryMonth, String expiryYear) {
        return new PaymentDetails(cardHolderName, maskedCardNumber, expiryMonth, expiryYear);
    }

    /**
     * Converts a string identifier into a {@link ListingId}.
     *
     * @param id the listing identifier as a string; must not be {@code null} or blank
     * @return the corresponding {@link ListingId}
     */
    public static ListingId toListingId(String id) {
        return new ListingId(id);
    }

    /**
     * Converts a list of string identifiers into a list of {@link ListingId} value objects.
     *
     * @param ids the listing identifiers as strings; must not be {@code null}
     * @return a list of corresponding {@link ListingId} instances
     */
    public static List<ListingId> toListingIds(List<String> ids) {
        return ids.stream()
                .map(TransactionMapper::toListingId)
                .toList();
    }
}