package TOPSECRET.repository;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Listing;
import TOPSECRET.domain.User;

import java.util.List;

/**
 * Contract for repositories responsible for managing {@link DirectSale} instances.
 * <p>
 * A DirectSale represents a buyer's cart, containing one or more {@link Listing}
 * objects selected for direct purchase. This interface defines the operations
 * required to create, store, and check the existence of DirectSale objects,
 * independently of the underlying storage mechanism (e.g., in-memory, database).
 * </p>
 *
 * <p>
 * Implementations must enforce uniqueness of DirectSale instances and throw
 * appropriate exceptions when invalid or duplicate entries are provided.
 * </p>
 */

public interface DirectSaleRepository {

    DirectSale createAndAdd(List<Listing> listings, User buyer);
    boolean exists(DirectSale dirSale);
    void add(DirectSale directSale);

}
