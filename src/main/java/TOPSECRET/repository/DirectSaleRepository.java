package TOPSECRET.repository;

import TOPSECRET.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DirectSaleRepository {

    private List<DirectSale> _directSaleList;

    public DirectSaleRepository(List<DirectSale> directSaleList) {
        _directSaleList =  new ArrayList<>(directSaleList);
    }

    public boolean createAndAddDirectSale(Listing listing, LocalDateTime directSaleDate){

        DirectSale directSaleToBeCreated = new DirectSale(listing, directSaleDate);

        if(ifDirectSaleExists(directSaleToBeCreated)){
            return false;
        }
        _directSaleList.add(directSaleToBeCreated);
        return true;
    }

    public boolean ifDirectSaleExists(DirectSale dirSale){
        for(DirectSale directSale: _directSaleList){
            if(dirSale.equals(directSale)){
                return true;
            }
        }
        return false;
    }

    public List<DirectSale> getListOfItemsOnDirectSale(Publication publication) {
        List<DirectSale> dsList = new ArrayList<>();

        for (DirectSale ds : _directSaleList) {
            for (Item item : ds.getListing().getItems()) {          // one listing can have one or more items
                if (item.getPublication().equals(publication)) {
                    dsList.add(ds);
                    break;                                          //avoids adding twice the same DirectSale
                }
            }
        }

        return dsList;
    }

    public List<DirectSale> getListOfItemsOnDirectSale(Author author) {
        List<DirectSale> dsList = new ArrayList<>();

        for (DirectSale ds : _directSaleList) {
            for (Item item : ds.getListing().getItems()) {
                if (item.getPublication().getBibliographicalData().getAuthors().contains(author)) {
                    dsList.add(ds);
                    break;
                }
            }
        }

        return dsList;
    }

    public List<DirectSale> getListOfItemsOnDirectSale(Publisher publisher) {
        List<DirectSale> dsList = new ArrayList<>();

        for (DirectSale ds : _directSaleList) {
            for (Item item : ds.getListing().getItems()) {
                if (item.getPublication().getBibliographicalData().getPublisher().equals(publisher)) {
                    dsList.add(ds);
                    break;
                }
            }
        }

        return dsList;
    }
}