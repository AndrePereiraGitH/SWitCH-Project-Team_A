package TOPSECRET.repository;

import TOPSECRET.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of {@link DirectSaleRepository}, responsible for storing
 * and managing {@link DirectSale} instances.
 * <p>
 * A DirectSale represents a buyer's cart, containing one or more {@link Listing}
 * objects selected for direct purchase. This repository provides operations to
 * create, store, and verify the existence of DirectSale objects.
 * </p>
 */

public class DirectSaleRepositoryMem implements DirectSaleRepository {

    private List<DirectSale> _directSaleList;
    private DirectSaleFactory _directSaleFactory;

    //============================================================================================================
    // Constructors

    /**
     * Creates a new DirectSaleRepository with an empty internal list of direct sales.
     * The repository will use the provided factory to create new DirectSale instances.
     *
     * @param directSaleFactory the factory responsible for creating DirectSale objects
     */

    public DirectSaleRepositoryMem(DirectSaleFactory directSaleFactory) {
        this._directSaleFactory = directSaleFactory;
        this._directSaleList = new ArrayList<>();
    }

    /**
     * Creates a new DirectSaleRepository initialized with an existing list of direct sales.
     * A defensive copy of the provided list is created to preserve encapsulation and prevent
     * external modifications from affecting the repository's internal state.
     *
     * @param directSaleList the initial list of direct sales to populate the repository
     * @param directSaleFactory the factory responsible for creating DirectSale objects
     */

    public DirectSaleRepositoryMem(List<DirectSale> directSaleList, DirectSaleFactory directSaleFactory) {
        this._directSaleList = new ArrayList<>(directSaleList);
        this._directSaleFactory = directSaleFactory;
    }

    //============================================================================================================

    /**
     * Creates a new {@link DirectSale} for the given buyer and listings,
     * stores it in the repository, and returns it.
     *
     * @param listings the listings included in the direct sale
     * @param buyer the user performing the purchase
     * @return the newly created DirectSale
     * @throws IllegalArgumentException if the factory rejects the input or
     *                                  if the created DirectSale already exists
     */

    public DirectSale createAndAdd(List<Listing> listings, User buyer) {
        DirectSale directSale = _directSaleFactory.create(listings, buyer);
        add(directSale);
        return directSale;
    }

    /**
     * Checks whether a given {@link DirectSale} already exists in the repository.
     *
     * @param dirSale the DirectSale to check
     * @return true if the DirectSale already exists, false otherwise
     */

    public boolean exists(DirectSale dirSale) {
        for (DirectSale directSale : _directSaleList) {
            if (dirSale.equals(directSale)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a {@link DirectSale} to the repository, throwing an exception if it already exists.
     * <p>
     * This method performs a duplicate check using {@link #exists(DirectSale)}.
     * If the direct sale is already present, an {@link IllegalArgumentException} is thrown.
     * Otherwise, the direct sale is added.
     * </p>
     *
     * @param directSale the DirectSale instance to be added
     * @throws IllegalArgumentException if the direct sale already exists in the repository
     *                                  or if {@code directSale} is null
     */

    public void add(DirectSale directSale) {
        if (directSale == null) {
            throw new IllegalArgumentException("DirectSale cannot be null");
        }

        if (exists(directSale)) {
            throw new IllegalArgumentException("DirectSale already exists");
        }
        _directSaleList.add(directSale);
    }
}