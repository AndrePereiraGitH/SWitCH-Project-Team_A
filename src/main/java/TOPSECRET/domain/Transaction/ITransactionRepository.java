package TOPSECRET.domain.Transaction;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.*;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionRepository {

    /**
     * Creates and stores a transaction resulting from an auction.
     *
     * @param buyerId         the buyer involved in the transaction
     * @param auctionId       the associated auction
     * @param price           the agreed price
     * @param transactionDate the transaction date
     * @param paymentDate     the payment date
     * @param sellersId       the list of sellers
     * @param status          the transaction status
     * @return the created transaction
     * @throws InstanceAlreadyExistsException if an equivalent transaction already exists
     */

    Transaction createAndAddFromAuction(UserId buyerId, AuctionId auctionId, Price price, LocalDateTime transactionDate,
                                        LocalDateTime paymentDate, List<UserId> sellersId, TransactionStatus status

    ) throws InstanceAlreadyExistsException;

    /**
     * Creates and stores a transaction resulting from a direct sale.
     *
     * @param buyerId         the buyer involved in the transaction
     * @param price           the agreed price
     * @param directSaleId    the associated direct sale
     * @param transactionDate the transaction date
     * @param paymentDate     the payment date
     * @param sellersId       the list of sellers
     * @param status          the transaction status
     * @return the created transaction
     * @throws InstanceAlreadyExistsException if an equivalent transaction already exists
     */

    Transaction createAndAddFromDirectSale(UserId buyerId, Price price, DirectSaleID directSaleId, LocalDateTime transactionDate,
                                           LocalDateTime paymentDate, List<UserId> sellersId, TransactionStatus status

    ) throws InstanceAlreadyExistsException;

    /**
     * Returns an immutable copy of all stored transactions.
     *
     * @return an unmodifiable list containing all transactions
     */

    List<Transaction> getAllTransactions();
}
