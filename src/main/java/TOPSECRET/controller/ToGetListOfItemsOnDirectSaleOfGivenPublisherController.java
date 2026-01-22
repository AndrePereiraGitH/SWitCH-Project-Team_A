package TOPSECRET.controller;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.Publisher;
import TOPSECRET.repository.DirectSaleRepository;

import java.util.ArrayList;
import java.util.List;

public class ToGetListOfItemsOnDirectSaleOfGivenPublisherController {

    private final DirectSaleRepository _directSaleRepository;

    ToGetListOfItemsOnDirectSaleOfGivenPublisherController(DirectSaleRepository directSaleRepository) {
        _directSaleRepository = directSaleRepository;

        if (_directSaleRepository == null) {
            throw new IllegalArgumentException("directSaleRepository cannot be null");
        }
    }

    public List<Item> getItemsOnDirectSaleByPublisher(Publisher publisher) {
        List<Item> result = new ArrayList<>();

        if (publisher == null) {
            return result;
        }

        List<DirectSale> directSales = _directSaleRepository.getAll();
        for (DirectSale directSale : directSales) {
            for (Item item : directSale.getListing().getItems()) {
                Publisher itemPublisher = item.getPublication().getBibliographicalData().getPublisher();
                if (publisher.equals(itemPublisher)) {
                    // item is on direct sale and has the target publisher, add it to the result!
                    result.add(item);
                }
            }
        }

        return result;
    }

}
