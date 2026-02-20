package TOPSECRET.repository;

import TOPSECRET.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DirectSaleRepository {

    private List<DirectSale> _directSaleList;

    //============================================================================================================
    // Constructor
    public DirectSaleRepository(List<DirectSale> directSaleList) {
        _directSaleList = new ArrayList<>(directSaleList);
    }

    //============================================================================================================

    public boolean createAndAddDirectSale(List<Listing> listings, User buyer, boolean isFinished) {

        DirectSale directSaleToBeCreated = new DirectSale(listings, buyer, isFinished);

        if (ifDirectSaleExists(directSaleToBeCreated)) {
            return false;
        }
        _directSaleList.add(directSaleToBeCreated);
        return true;
    }

    public DirectSale createAndAddDirectSaleReturn(List<Listing> listings, User buyer, boolean isFinished) {

        DirectSale directSaleToBeCreated = new DirectSale(listings, buyer, isFinished);

        if (ifDirectSaleExists(directSaleToBeCreated)) {
            throw new IllegalArgumentException("Instance already exists");
        }
        _directSaleList.add(directSaleToBeCreated);
        return directSaleToBeCreated;
    }

    public boolean ifDirectSaleExists(DirectSale dirSale) {
        for (DirectSale directSale : _directSaleList) {
            if (dirSale.equals(directSale)) {
                return true;
            }
        }
        return false;
    }


    public List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenGenre(Genre genre) {

        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        List<PublicationSample> listOfItems = new ArrayList<>();

        for (DirectSale directSale : _directSaleList) {
            for (Listing listing : directSale.getListings()) {
                for (PublicationSample item : listing.getItems()) {
                    if (item.hasGenre(genre)) {
                        listOfItems.add(item);
                    }
                }
            }
        }

        return listOfItems;
    }

    //TODO - rever este metodo. o argumento nao deveria ser bibliographicalData?

//    public List<DirectSale> getListOfItemsOnDirectSale(PublicationSample publication) {
//
//        if (publication == null) {
//            throw new IllegalArgumentException("Publication cannot be null");
//        }
//
//        List<DirectSale> dsList = new ArrayList<>();
//
//
//        for (DirectSale ds : _directSaleList) {
//            boolean added = false;
//            for (Listing listing : ds.getListings()) {
//                for (PublicationSample item : listing.getItems()) {
//                    dsList.add(ds);
//                    added = true;
//                    break;                                          // avoids adding twice the same DirectSale
//                }
//                if (added) break;                                      // leaves listings
//            }
//        }
//
//        return dsList;
//    }


    public List<DirectSale> getListOfItemsOnDirectSale(Author author) {
        List<DirectSale> dsList = new ArrayList<>();

        for (DirectSale ds : _directSaleList) {
            for (Listing listing : ds.getListings()) {
                for (PublicationSample item : listing.getItems()) {
                    if (item.getBibliographicalData().getAuthors().contains(author)) {
                        dsList.add(ds);
                        break;
                    }
                }
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
}