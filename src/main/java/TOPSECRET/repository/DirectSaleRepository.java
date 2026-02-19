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

//    public boolean createAndAddDirectSale(Listing listing, LocalDateTime directSaleDate) {
//
//        DirectSale directSaleToBeCreated = new DirectSale(listing, directSaleDate);
//
//        if (ifDirectSaleExists(directSaleToBeCreated)) {
//            return false;
//        }
//        _directSaleList.add(directSaleToBeCreated);
//        return true;
//    }

    public DirectSale createAndAddDirectSaleReturn(List<Listing> listings, User buyer, boolean isFinished) {

        DirectSale directSaleToBeCreated = new DirectSale(listings, buyer, isFinished);

        if (ifDirectSaleExists(directSaleToBeCreated)) {
            throw new IllegalArgumentException("Instance already exists");
        }
        _directSaleList.add(directSaleToBeCreated);
        return directSaleToBeCreated;
    }

//    public DirectSale createAndAddDirectSaleReturn(Listing listing, LocalDateTime directSaleDate) {
//
//        DirectSale directSaleToBeCreated = new DirectSale(listing, directSaleDate);
//
//        if (ifDirectSaleExists(directSaleToBeCreated)) {
//            throw new IllegalArgumentException("Instance already exists");
//        }
//        _directSaleList.add(directSaleToBeCreated);
//        return directSaleToBeCreated;
//    }

    public boolean ifDirectSaleExists(DirectSale dirSale) {
        for (DirectSale directSale : _directSaleList) {
            if (dirSale.equals(directSale)) {
                return true;
            }
        }
        return false;
    }

    public List<Item> getListOfItemsOnDirectSaleOfAGivenGenre(Genre genre) {

        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        List<Item> listOfItems = new ArrayList<>();

        for (DirectSale directSale : _directSaleList) {
            for (Listing listing : directSale.getListings()) {
                for (Item item : listing.getItems()) {
                    if (item.hasGenre(genre)) {
                        listOfItems.add(item);
                    }
                }
            }
        }

        return listOfItems;
    }

    //TODO - rever este metodo. o argumento nao deveria ser bibliographicalData?

    public List<DirectSale> getListOfItemsOnDirectSale(Publication publication) {

        if (publication == null) {
            throw new IllegalArgumentException("Publication cannot be null");
        }

        List<DirectSale> dsList = new ArrayList<>();

//        for (DirectSale ds : _directSaleList) {
//            for (Item item : ds.getListing().getItems()) {          // one listing can have one or more items
//                if (item.getPublication().equals(publication)) {    // representation of method isDirectSaleFromPublication on design
//                    dsList.add(ds);
//                    break;                                          //avoids adding twice the same DirectSale
//                }
//            }
//        }

        for (DirectSale ds : _directSaleList) {
            boolean added = false;
            for (Listing listing : ds.getListings()) {
                for (Item item : listing.getItems()) {
                    if (item.getPublication().equals(publication)) {
                        dsList.add(ds);
                        added = true;
                        break;                                          // avoids adding twice the same DirectSale
                    }
                }
                if (added) break;                                      // leaves listings
            }
        }

        return dsList;
    }


    public List<DirectSale> getListOfItemsOnDirectSale(Author author) {
        List<DirectSale> dsList = new ArrayList<>();

//        for (DirectSale ds : _directSaleList) {
//            for (Item item : ds.getListing().getItems()) {
//                if (item.getPublication().getBibliographicalData().getAuthors().contains(author)) {
//                    dsList.add(ds);
//                    break;
//                }
//            }
//        }

        for (DirectSale ds : _directSaleList) {
            for (Listing listing : ds.getListings()) {
                for (Item item : listing.getItems()) {
                    if (item.getPublication().getBibliographicalData().getAuthors().contains(author)) {
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

//        for (DirectSale ds : _directSaleList) {
//            for (Item item : ds.getListing().getItems()) {
////                if (item.getPublication().getBibliographicalData().getPublisher().equals(publisher)) {
//                if (item.getPublication().getEdition().getPublisher().equals(publisher)) {
//                        dsList.add(ds);
//                        break;
//                }
//            }
//        }

        for (DirectSale ds : _directSaleList) {
            for (Listing listing : ds.getListings()) {
                for (Item item : listing.getItems()) {
                    if (item.getPublication().getEdition().getPublisher().equals(publisher)) {
                        dsList.add(ds);
                        break;
                    }
                }
            }
        }

        return dsList;
    }
}