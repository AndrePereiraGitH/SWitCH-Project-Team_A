package TOPSECRET.domain.Transaction;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.DirectSaleID;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Factory responsible for creating {@link Transaction} instances.
 * <p>
 * This class centralizes the creation logic for transactions originating
 * from different business contexts, such as auctions and direct sales.
 * <p>
 * It ensures that transaction objects are instantiated with the appropriate
 * parameters and construction paths, improving consistency and maintainability.
 */

public class TransactionFactory {

    /**
     * Creates a {@link Transaction} resulting from an auction.
     *
     * @param buyer           the identifier of the user who won the auction
     * @param auction         the identifier of the auction associated with the transaction
     * @param price           the final agreed price
     * @param transactionDate the date and time when the transaction occurred
     * @param paymentDate     the date and time when the payment was completed
     * @param sellers         the list of seller identifiers involved in the transaction
     * @param status          the initial status of the transaction
     * @return a new {@link Transaction} instance representing the auction-based transaction
     * @throws IllegalArgumentException if any of the provided arguments violate
     *                                  the constraints defined in {@link Transaction}
     */

    public Transaction createFromAuction(UserId buyer, AuctionId auction, Price price, LocalDateTime transactionDate,
                                         LocalDateTime paymentDate, List<UserId> sellers, TransactionStatus status) {

        return new Transaction(buyer, auction, price, transactionDate,
                paymentDate, sellers, status);
    }

    /**
     * Creates a {@link Transaction} resulting from a direct sale.
     *
     * @param buyer           the identifier of the purchasing user
     * @param price           the agreed price of the sale
     * @param directSale      the identifier of the direct sale instance
     * @param transactionDate the date and time when the transaction occurred
     * @param paymentDate     the date and time when the payment was completed
     * @param sellers         the list of seller identifiers involved in the transaction
     * @param status          the initial status of the transaction
     * @return a new {@link Transaction} instance representing the direct sale transaction
     * @throws IllegalArgumentException if any of the provided arguments violate
     *                                  the constraints defined in {@link Transaction}
     */

    public Transaction createFromDirectSale(UserId buyer, Price price, DirectSaleID directSale, LocalDateTime transactionDate,
                                            LocalDateTime paymentDate, List<UserId> sellers, TransactionStatus status) {

        return new Transaction(buyer, price, directSale, transactionDate, paymentDate, sellers, status);
    }
}
