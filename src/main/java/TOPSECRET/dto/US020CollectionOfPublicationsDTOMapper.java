package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for mapping raw collection and user data
 * into {@link US020CollectionOfPublicationsDTO} instances.
 * <p>
 * This mapper applies basic data normalization rules such as trimming
 * leading and trailing whitespace from input values.
 * </p>
 *
 * <p>
 * All methods are static and the class is not meant to be instantiated.
 * </p>
 */
public class US020CollectionOfPublicationsDTOMapper {

    private US020CollectionOfPublicationsDTOMapper() {
        // Prevents instantiation
    }

    /**
     * Converts a list of {@link CollectionOfPublications} entities into a list of
     * {@link US020CollectionOfPublicationsDTO} objects.
     *
     * <p>For each collection, this method retrieves the collection name and resolves
     * the owner's username using the provided {@link IUserRepository}. If a user
     * corresponding to a collection's owner ID is not found, an exception is thrown.</p>
     *
     * @param collections the list of {@link CollectionOfPublications} to be converted;
     *                    must not be null
     * @param userRepo the repository used to fetch user information by owner ID;
     *                 must not be null
     * @return a list of {@link US020CollectionOfPublicationsDTO} containing the
     *         collection name and owner username
     * @throws IllegalStateException if a user corresponding to any collection's owner ID is not found
     */
    public static List<US020CollectionOfPublicationsDTO> toDTO(List<CollectionOfPublications> collections, IUserRepository userRepo) {

        List<US020CollectionOfPublicationsDTO> dto = new ArrayList<>();

        for (CollectionOfPublications c : collections) {
            String collectionName = c.getCollectionName();
            String ownerName = userRepo.findUserByID(c.getOwnerId())
                    .map(User::getUserName)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            dto.add(new US020CollectionOfPublicationsDTO(collectionName.trim(), ownerName.trim()));
        }
        return dto;
    }
}
