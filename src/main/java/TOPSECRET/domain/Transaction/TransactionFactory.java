package TOPSECRET.domain.Transaction;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Factory responsible for creating and reconstructing {@link Transaction}
 * aggregate instances.
 */
@Component
public final class TransactionFactory {


    /**
     * Creates a new {@link Transaction} with status {@link TransactionStatus#PENDING}.
     *
     * <p>Note: this project is demonstrative and does not implement payment
     * confirmation logic. Transactions are created with {@code PENDING} status
     * and no status transition is performed.</p>
     */
    public Transaction create(UserId buyerId,
                              Price price,
                              LocalDateTime transactionDate,
                              Address shippingAddress,
                              PaymentDetails paymentDetails,
                              List<ListingId> listingIds) {

        return new Transaction(
                buyerId,
                price,
                transactionDate,
                shippingAddress,
                paymentDetails,
                TransactionStatus.PENDING,
                listingIds
        );
    }

    /**
     * Reconstructs an existing {@link Transaction} from persisted data.
     *
     * @param id the existing transaction identifier
     * @param buyerId the buyer identifier
     * @param price the transaction price
     * @param transactionDate the transaction date and time
     * @param shippingAddress the shipping address
     * @param paymentDetails the payment details
     * @param status the transaction status
     * @param listingIds the purchased listing identifiers
     * @return a reconstructed transaction instance
     */
    public Transaction create (TransactionID id,
                               UserId buyerId,
                               Price price,
                               LocalDateTime transactionDate,
                               Address shippingAddress,
                               PaymentDetails paymentDetails,
                               TransactionStatus status,
                               List<ListingId> listingIds) {

        return new Transaction(
                id,
                buyerId,
                price,
                transactionDate,
                shippingAddress,
                paymentDetails,
                status,
                listingIds
        );
    }
}