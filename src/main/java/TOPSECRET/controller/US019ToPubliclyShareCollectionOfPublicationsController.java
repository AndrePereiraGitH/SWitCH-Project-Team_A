package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.User;
import TOPSECRET.dto.CollectionOfPublicationsDTO;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;
import TOPSECRET.repository.CollectionOfPublicationsRepository;

import java.util.ArrayList;
import java.util.List;

public class US019ToPubliclyShareCollectionOfPublicationsController {

    CollectionOfPublicationsRepository collectionOfPublicationsRepository;

    public US019ToPubliclyShareCollectionOfPublicationsController(CollectionOfPublicationsRepository collectionOfPublicationsRepository) {
        this.collectionOfPublicationsRepository = collectionOfPublicationsRepository;
    }

    public List<CollectionOfPublicationsDTO_US019> requestAllCollectionsOfPublicationsByUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<CollectionOfPublications> collections = collectionOfPublicationsRepository.getAllCollectionOfPublicationsByUser(user);

        List<CollectionOfPublicationsDTO_US019> collectionsDTO = new ArrayList<>();

        collections.forEach( collection -> {
            collectionsDTO.add(CollectionOfPublicationsDTO_US019.toDTO(collection));
        });

        return collectionsDTO;
    }

    }

