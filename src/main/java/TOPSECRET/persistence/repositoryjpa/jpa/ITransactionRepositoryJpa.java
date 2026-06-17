package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.TransactionID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.TransactionDM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link TransactionDM}.
 *
 * <p>Extends {@link JpaRepository} to inherit standard CRUD operations.
 * Custom queries specific to this aggregate are declared below.</p>
 */
public interface ITransactionRepositoryJpa extends JpaRepository<TransactionDM, TransactionID> {

    /**
     * Retrieves all transactions associated with the given buyer.
     *
     * @param buyerId the identifier of the buyer; must not be {@code null}
     * @return a list of {@link TransactionDM} owned by the given buyer;
     * never {@code null}, may be empty
     */
    List<TransactionDM> findAllByBuyerId(UserId buyerId);
}
