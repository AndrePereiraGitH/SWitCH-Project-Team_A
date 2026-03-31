package TOPSECRET.domain.DirectSale;

import TOPSECRET.domain.Listing;
import TOPSECRET.domain.User.User;

import java.util.List;

/**
 * Contract for managing {@link DirectSale} objects.
 * <p>
 * Defines operations for creating, storing, and verifying the existence
 * of direct sale transactions associated with listings and buyers.
 * </p>
 */

public interface IDirectSaleRepository {

    /**
     * Creates a new direct sale and adds it to the repository.
     *
     * @param listings the listings included in the direct sale
     * @param buyer the user who is purchasing the listings
     * @return the created and stored direct sale
     */
    DirectSale createAndAdd(List<Listing> listings, User buyer);

    /**
     * Checks whether the given direct sale already exists in the repository.
     *
     * @param dirSale the direct sale to check
     * @return {@code true} if the direct sale already exists, {@code false} otherwise
     */
    boolean exists(DirectSale dirSale);

    /**
     * Adds an existing direct sale to the repository.
     *
     * @param directSale the direct sale to add
     */
    void add(DirectSale directSale);

}
