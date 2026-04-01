package TOPSECRET.domain.Transaction;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.DirectSaleID;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class TransactionFactoryTest {

    //Doubles and attributes
    UserId buyer = mock(UserId.class);
    AuctionId auction = mock(AuctionId.class);
    DirectSaleID directSale = mock(DirectSaleID.class);
    Price price = mock(Price.class);
    UserId seller = mock(UserId.class);
    List<UserId> sellers = List.of(seller);
    LocalDateTime transactionDate = LocalDateTime.now();
    LocalDateTime future = LocalDateTime.now().plusDays(1);
    TransactionStatus status = TransactionStatus.PENDING;

    @Test
    void createFromAuctionShouldReturnCorrectTransaction() {

        //Arrange
        try (MockedConstruction<Transaction> mocked =
                     mockConstruction(Transaction.class,
                             (mock, context) -> {
                             })) {

            TransactionFactory factory = new TransactionFactory();

            // Act
            Transaction transactionResult = factory.createFromAuction(
                    buyer,
                    auction,
                    price,
                    transactionDate,
                    future,
                    sellers,
                    status
            );

            // assert
            assertNotNull(transactionResult);
            assertEquals(1, mocked.constructed().size());

            Transaction constructedTransaction = mocked.constructed().get(0);
            assertSame(transactionResult, constructedTransaction);

        }
    }

    @Test
    void createFromDirectSaleShouldReturnCorrectTransaction() {


        //Arrange
        try (MockedConstruction<Transaction> mocked =
                     mockConstruction(Transaction.class,
                             (mock, context) -> {
                             })) {

            TransactionFactory factory = new TransactionFactory();

            // Act
            Transaction transactionResult = factory.createFromDirectSale(
                    buyer,
                    price,
                    directSale,
                    transactionDate,
                    future,
                    sellers,
                    status
            );

            // assert
            assertNotNull(transactionResult);
            assertEquals(1, mocked.constructed().size());

            Transaction constructedTransaction = mocked.constructed().get(0);
            assertSame(transactionResult, constructedTransaction);

        }
    }
}