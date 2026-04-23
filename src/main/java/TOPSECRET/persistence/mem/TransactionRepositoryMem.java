package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.Transaction.ITransactionRepository;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.Transaction.TransactionFactory;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.DirectSaleID;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of the {@code ITransactionRepository} interface.
 * <p>
 * This repository stores {@link Transaction} instances in a local {@link List},
 * providing basic creation, storage, and retrieval functionality.
 * <p>
 * Transactions can be created through:
 * <ul>
 *     <li>Auction-based transactions</li>
 *     <li>Direct sale transactions</li>
 * </ul>
 * <p>
 * Each transaction is created via a {@link TransactionFactory}, ensuring
 * consistent construction and validation logic.
 * <p>
 * Duplicate transactions are prevented based on the {@code equals} method
 * of the {@link Transaction} class.
 * <p>
 * <b>Note:</b> This implementation is intended for testing or non-persistent
 * environments. Data is not persisted beyond runtime.
 */


public class TransactionRepositoryMem implements ITransactionRepository {

    private TransactionFactory _transactionFactory;

    private List<Transaction> _transactions;

    /**
     * Constructs a new in-memory transaction repository.
     *
     * @param transactionFactory the factory used to create transactions
     */

    public TransactionRepositoryMem(TransactionFactory transactionFactory) {

        this._transactions = new ArrayList<>();
        this._transactionFactory = transactionFactory;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public Transaction
    createAndAddFromAuction(UserId buyerId, AuctionId auctionId, Price price, LocalDateTime transactionDate,
                            LocalDateTime paymentDate, List<UserId> sellersId, TransactionStatus status
    )
            throws InstanceAlreadyExistsException {

        Transaction newTransactionFromAuction = _transactionFactory.createFromAuction(buyerId, auctionId, price,
                transactionDate, paymentDate, sellersId, status);

        if (isExistent(newTransactionFromAuction))
            throw new InstanceAlreadyExistsException("This transaction already took place.");

        _transactions.add(newTransactionFromAuction);

        return newTransactionFromAuction;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public Transaction createAndAddFromDirectSale(
            UserId buyerId, Price price, DirectSaleID directSaleId, LocalDateTime transactionDate,
            LocalDateTime paymentDate, List<UserId> sellersId, TransactionStatus status)

            throws InstanceAlreadyExistsException {

        Transaction newTransactionFromDirectSale = _transactionFactory.createFromDirectSale(buyerId, price, directSaleId,
                transactionDate, paymentDate, sellersId, status);

        if (isExistent(newTransactionFromDirectSale))
            throw new InstanceAlreadyExistsException("This transaction already took place.");

        _transactions.add(newTransactionFromDirectSale);

        return newTransactionFromDirectSale;
    }

    private boolean isExistent(Transaction transaction) {
        return _transactions.contains(transaction);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public List<Transaction> getAllTransactions() {
        return List.copyOf(_transactions);
    }

}
