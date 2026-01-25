package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.dto.LibraryPublicationDTO_US015;
import TOPSECRET.dto.LibraryPublicationMapper_US015;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.ItemRepository;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;

public class ToPutPublicationInDirectSaleController {

    // attributes
    private final Library library;
    private final DirectSaleRepository directSaleRepo;
    private final ItemRepository itemRepo;

    public ToPutPublicationInDirectSaleController(Library library, DirectSaleRepository directSaleRepo, ItemRepository itemRepo) {

        if (library == null) throw new IllegalArgumentException("Library cannot be null");
        if (directSaleRepo == null) throw new IllegalArgumentException("DirectSaleRepository cannot be null");
        if (itemRepo == null) throw new IllegalArgumentException("ItemRepository cannot be null");

        this.library = library;
        this.directSaleRepo = directSaleRepo;
        this.itemRepo = itemRepo;
    }

    public List<LibraryPublicationDTO_US015> getAllPublications() {
        List<Publication> publications = library.getAllPublications();
        return LibraryPublicationMapper_US015.toDTO(publications);
    }

    public Item createAndAddItem(Condition condition, double weight, Dimensions dimensions, Publication publication) throws InstanceAlreadyExistsException {
        return itemRepo.createAndAddItem(condition, weight, dimensions, publication);
    }


    public boolean createAndAddDirectSale(Listing listing) {

        if (listing == null)
            throw new IllegalArgumentException("Listing cannot be null");

        for (Item item : listing.getItems()) {
            if (item == null)
                throw new IllegalArgumentException("Listing contains a null item");
        }

        return directSaleRepo.createAndAddDirectSale(listing, date());
    }
    LocalDateTime date() {
        return LocalDateTime.now();
    }

}