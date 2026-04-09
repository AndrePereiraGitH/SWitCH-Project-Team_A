package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.ArrayList;
import java.util.List;


public class CollectionOfPublicationsDTOMapper_US020 {

    private final ICollectionOfPublicationsRepository repo;
    private final IUserRepository userRepo;

    public CollectionOfPublicationsDTOMapper_US020(
            ICollectionOfPublicationsRepository repo,
            IUserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public List<CollectionOfPublicationsDTO_US020> toDTO(GenreID genreId) {

        List<CollectionOfPublications> collections = repo.findPublicByGenre(genreId);
        List<CollectionOfPublicationsDTO_US020> result = new ArrayList<>();

        for (CollectionOfPublications c : collections) {
            String collectionName = c.getCollectionName();
            UserId ownerID = c.getOwnerId();
            String ownerName = userRepo.findUserByID(ownerID)
                    .map(User::getUserName)
                    .orElseThrow(() -> new IllegalStateException("User not found: " + ownerID));

            result.add(new CollectionOfPublicationsDTO_US020(collectionName, ownerName));
        }

        return result;
    }

}
