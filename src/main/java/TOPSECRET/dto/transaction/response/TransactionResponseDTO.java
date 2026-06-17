package TOPSECRET.dto.transaction.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * Data transfer object used to expose {@code Transaction} data in API responses.
 *
 * <p>Extends {@link RepresentationModel} to support HATEOAS links.
 * Carries all transaction fields returned to the client, including
 * shipping address and payment details as flat fields.</p>
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
@Getter
public class TransactionResponseDTO extends RepresentationModel<TransactionResponseDTO> {

    private String transactionId;
    private String buyerId;
    private String price;
    private String transactionDate;

    // Shipping Address
    private String street;
    private String number;
    private String zipCode;
    private String cityId;

    // Payment Details
    private String cardHolderName;
    private String maskedCardNumber;
    private String expiryMonth;
    private String expiryYear;

    private String status;
    private List<String> listingIds;
}