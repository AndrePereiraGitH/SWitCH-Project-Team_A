package TOPSECRET.controller;

import java.util.List;
import java.util.ArrayList;

import TOPSECRET.domain.*;

import TOPSECRET.dto.AuctionDTO_US028;
import TOPSECRET.dto.AuctionMapper_US028;
import TOPSECRET.dto.PublisherDTO_US028;
import TOPSECRET.dto.PublisherMapper_US028;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.PublisherRepository;

public class US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController {
    // attributes
    private final PublisherRepository publisherRepo;
    private final AuctionRepository auctionRepo;
    private final AuctionMapper_US028 auctionMapper;
    private final PublisherMapper_US028 publisherMapper;
    // constructor
    public US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(AuctionRepository auctionRepository, PublisherRepository publisherRepository, AuctionMapper_US028 auctionMapper, PublisherMapper_US028 publisherMapper) {


        this.publisherRepo = publisherRepository;
        this.auctionRepo = auctionRepository;
        this.auctionMapper = auctionMapper;
        this.publisherMapper = publisherMapper;
    }

    // methods

    // "UI" requests all publishers (step 2)
    public List<PublisherDTO_US028> getAllPublishers() {

        List<Publisher> publishers =
                publisherRepo.getAllPublishers();

        return publisherMapper.toDTO(publishers);
    }

    // "UI" provides a publisher, controller retrieves auctions (step 8)
    public List<AuctionDTO_US028> getListOfItemsOnAuction(String publisherVatNumber) {
        Publisher publisher =
                publisherRepo.findByVatNumber(publisherVatNumber);
        if (publisher == null) return List.of();

        List<Item> items = auctionRepo.getItemsOnAuctionGivenPublisher(publisher);

        return auctionMapper.toDTO(items);


    }
}
