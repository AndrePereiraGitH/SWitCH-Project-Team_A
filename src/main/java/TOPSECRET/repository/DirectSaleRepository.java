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











}
