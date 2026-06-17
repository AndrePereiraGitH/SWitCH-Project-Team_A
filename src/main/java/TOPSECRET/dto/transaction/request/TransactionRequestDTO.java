package TOPSECRET.dto.transaction.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Data transfer object used to receive the information required to create
 * a new {@code Transaction}.
 *
 * <p>Captures the buyer identifier, total price, transaction date,
 * shipping address, payment details, and the list of listing identifiers
 * included in the purchase.</p>
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TransactionRequestDTO {

    @NotBlank
    private String buyerId;

    @NotBlank
    private String price;

    @NotBlank
    private String transactionDate;

    @NotBlank
    private String street;

    @NotBlank
    private String number;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String cityId;

    @NotBlank
    private String cardHolderName;

    @NotBlank
    private String maskedCardNumber;

    @NotBlank
    private String expiryMonth;

    @NotBlank
    private String expiryYear;

    @NotEmpty
    private List<String> listingIds;
}