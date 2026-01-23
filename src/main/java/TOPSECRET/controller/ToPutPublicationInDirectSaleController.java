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

        if (condition == null) throw new IllegalArgumentException("Condition cannot be null");
        if (dimensions == null) throw new IllegalArgumentException("Dimensions cannot be null");
        if (publication == null) throw new IllegalArgumentException("Publication cannot be null");
        if (weight <= 0) throw new IllegalArgumentException("Weight must be positive");

        return itemRepo.createAndAddItem(condition, weight, dimensions, publication);
    }

    public boolean createAndAddDirectSale(Listing listing) {

        return directSaleRepo.createAndAddDirectSale(listing, LocalDateTime.now());
    }



}
