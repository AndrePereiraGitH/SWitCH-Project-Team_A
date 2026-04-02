package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.DirectSale.DirectSale;
import TOPSECRET.domain.DirectSale.DirectSaleFactory;
import TOPSECRET.domain.DirectSale.IDirectSaleRepository;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of {@link IDirectSaleRepository}.
 * <p>
 * This repository stores {@link DirectSale} instances in an internal list and
 * provides operations for creating, storing, and verifying direct sales.
 * </p>
 * <p>
 * Direct sales are created through a {@link DirectSaleFactory} and are kept only
 * during the application's runtime.
 * </p>
 */

public class DirectSaleRepositoryMem implements IDirectSaleRepository {

    private List<DirectSale> _directSaleList;
    private DirectSaleFactory _directSaleFactory;

    //============================================================================================================
    // Constructors

    /**
     * Constructs an in-memory direct sale repository with the given factory.
     *
     * @param directSaleFactory the factory used to create {@link DirectSale} instances
     */
    public DirectSaleRepositoryMem(DirectSaleFactory directSaleFactory) {
        this._directSaleFactory = directSaleFactory;
        this._directSaleList = new ArrayList<>();
    }


    /**
     * Constructs an in-memory direct sale repository with an initial list of direct sales.
     *
     * @param directSaleList the initial list of direct sales to populate the repository
     * @param directSaleFactory the factory used to create {@link DirectSale} instances
     */
    public DirectSaleRepositoryMem(List<DirectSale> directSaleList, DirectSaleFactory directSaleFactory) {
        this._directSaleList = new ArrayList<>(directSaleList);
        this._directSaleFactory = directSaleFactory;
    }

    //============================================================================================================


    /**
     * {@inheritDoc}
     */
    @Override
    public DirectSale createAndAdd(List<ListingId> listings, UserId buyer) {
        DirectSale directSale = _directSaleFactory.create(listings, buyer);
        add(directSale);
        return directSale;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(DirectSale dirSale) {
        for (DirectSale directSale : _directSaleList) {
            if (dirSale.equals(directSale)) {
                return true;
            }
        }
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
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