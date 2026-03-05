package TOPSECRET.repository;

import TOPSECRET.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for storing and managing {@link DirectSale} instances.
 * <p>
 * A DirectSale represents a buyer's cart, containing one or more {@link Listing}
 * objects selected for direct purchase. This repository provides operations to
 * add new DirectSale instances, verify duplicates, and query existing ones based
 * on different bibliographical attributes.
 * </p>
 */


public class DirectSaleRepository {

    private List<DirectSale> _directSaleList;
    private DirectSaleFactory _directSaleFactory;

    //============================================================================================================
    // Constructor

    /**
     * Creates a new DirectSaleRepository with an initial list of DirectSale instances
     * and a factory responsible for creating new DirectSale objects.
     *
     * @param directSaleList the initial list of direct sales to populate the repository
     */

    public DirectSaleRepository(List<DirectSale> directSaleList,  DirectSaleFactory directSaleFactory) {
        _directSaleList = new ArrayList<>(directSaleList);
        _directSaleFactory = directSaleFactory;
    }

    //============================================================================================================

    /**
     * Creates a new DirectSale (cart) for the given buyer and listings,
     * stores it in the repository, and returns it.
     */
    public DirectSale createAndAdd(List<Listing> listings, User buyer) {
        DirectSale directSale = _directSaleFactory.create(listings, buyer);
        addDirectSaleOrThrow(directSale);
        return directSale;
    }

    /**
     * Checks whether a given {@link DirectSale} already exists in the repository.
     *
     * @param dirSale the DirectSale to check
     * @return true if the DirectSale already exists, false otherwise
     */

    public boolean ifDirectSaleExists(DirectSale dirSale) {
        for (DirectSale directSale : _directSaleList) {
            if (dirSale.equals(directSale)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a {@link DirectSale} to the repository if it does not already exist.
     * <p>
     * This method performs a duplicate check using {@link #ifDirectSaleExists(DirectSale)}.
     * If the direct sale is already present, the method returns {@code false} and
     * the repository remains unchanged. Otherwise, the direct sale is added and
     * the method returns {@code true}.
     * </p>
     *
     * @param directSale the DirectSale instance to be added
     * @return {@code true} if the direct sale was successfully added,
     *         {@code false} if it already existed in the repository
     * @throws IllegalArgumentException if {@code directSale} is null
     */

    public boolean addDirectSale(DirectSale directSale) {
        if (directSale == null) {
            throw new IllegalArgumentException("DirectSale cannot be null");
        }

        if (ifDirectSaleExists(directSale)){
            return false;
        }
        _directSaleList.add(directSale);
        return true;
    }

    /**
     * Adds a {@link DirectSale} to the repository, throwing an exception if it already exists.
     * <p>
     * This method is similar to {@link #addDirectSale(DirectSale)}, but instead of
     * returning a boolean, it enforces uniqueness by throwing an
     * {@link IllegalArgumentException} when a duplicate is detected.
     * </p>
     *
     * @param directSale the DirectSale instance to be added
     * @throws IllegalArgumentException if the direct sale already exists in the repository
     *                                  or if {@code directSale} is null
     */

    public void addDirectSaleOrThrow(DirectSale directSale) {
        if (directSale == null) {
            throw new IllegalArgumentException("DirectSale cannot be null");
        }

        if (ifDirectSaleExists(directSale)) {
            throw new IllegalArgumentException("DirectSale already exists");
        }
        _directSaleList.add(directSale);
    }
}