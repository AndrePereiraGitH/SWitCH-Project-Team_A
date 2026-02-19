package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.ItemRepository;
import TOPSECRET.repository.LibraryRepository;
import TOPSECRET.repository.ListingRepository;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class US016ToPutPublicationInAuctionController {

    private final LibraryRepository libraryRepo;
    private final ItemRepository itemRepository;
    private final ListingRepository listingRepository;
    private final AuctionRepository auctionRepository;

    public US016ToPutPublicationInAuctionController(LibraryRepository libraryRepo, ItemRepository itemRepository, ListingRepository listingRepository, AuctionRepository auctionRepository) {

        this.libraryRepo = libraryRepo;
        this.itemRepository = itemRepository;
        this.listingRepository = listingRepository;
        this.auctionRepository = auctionRepository;
    }


    private Library getUserLibrary(String username) throws InstanceNotFoundException {
        Library userLibrary = libraryRepo.getLibraryOfUser(username);
        return userLibrary;
    }


    public List<Publication> getAllPublicationsInUserLibrary(String username) throws InstanceNotFoundException {
        if (username == null) {
            throw new IllegalArgumentException("username cannot be null");
        }

        Library userLibrary = getUserLibrary(username);

        if (userLibrary.getAllPublications().isEmpty()) {
            throw new EmptyStackException();
        }

        return userLibrary.getAllPublications();
    }

//    public boolean putPublicationForAuction(Publication pub1, Price listingPrice, String shortDescription, String listingTitle, User seller, LocalDateTime initialDate, LocalDateTime finalDate, Price buyOutPrice) throws InstanceAlreadyExistsException {
//
//        if (pub1 == null) {
//            throw new IllegalArgumentException("Publication cannot be null");
//        }
//
//        if (initialDate == null || finalDate == null || buyOutPrice == null) {
//            throw new IllegalArgumentException("initialDate, finalDate and buyOutPrice cannot be null");
//        }
//
//        List<Item> items = new ArrayList<>();
//
//        Item item = itemRepository.createAndAddItem(Condition.LIKE_NEW, 1.2, new Dimensions(10, 10, 10), pub1);
//        items.add(item);
//
//        Listing listing = listingRepository.createAndAdd(items, listingPrice, shortDescription, listingTitle, seller);
//
//        auctionRepository.createAndAdd(listing, initialDate, finalDate, buyOutPrice);
//
//        return true;
//    }


}
