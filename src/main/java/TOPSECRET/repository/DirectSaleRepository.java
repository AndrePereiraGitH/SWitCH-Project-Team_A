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

    //============================================================================================================
    // Constructor

    /**
     * Creates a new DirectSaleRepository with an initial list of DirectSale instances.
     *
     * @param directSaleList the initial list of direct sales to populate the repository
     */

    public DirectSaleRepository(List<DirectSale> directSaleList) {
        _directSaleList = new ArrayList<>(directSaleList);
    }

    //============================================================================================================

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

    // TODO - os metodos seguintes devem ser movidos para a classe ListingRepository
    /*
       public List<DirectSale> getListOfItemsOnDirectSale(PublicationSample publication) {

        if (publication == null) {
            throw new IllegalArgumentException("Publication cannot be null");
        }

        List<DirectSale> dsList = new ArrayList<>();


        for (DirectSale ds : _directSaleList) {
            boolean added = false;
            for (Listing listing : ds.getListings()) {
                for (PublicationSample item : listing.getItems()) {
                    dsList.add(ds);
                    added = true;
                    break;                                          // avoids adding twice the same DirectSale
                }
                if (added) break;                                      // leaves listings
            }
        }

        return dsList;
    }

    public List<DirectSale> getListOfItemsOnDirectSale(Publisher publisher) {
        List<DirectSale> dsList = new ArrayList<>();

        for (DirectSale ds : _directSaleList) {
            for (Listing listing : ds.getListings()) {
                for (PublicationSample item : listing.getItems()) {
                    if (item.getEdition().getPublisher().equals(publisher)) {
                        dsList.add(ds);
                        break;
                    }
                }
            }
        }

        return dsList;
    }

     */
}