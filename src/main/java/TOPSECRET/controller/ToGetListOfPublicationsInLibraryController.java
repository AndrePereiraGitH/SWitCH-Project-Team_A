package TOPSECRET.controller;

import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US012PublicationDetailsDTO;
import TOPSECRET.dto.US012PublicationSampleMapper;

import javax.management.InstanceNotFoundException;
import java.util.List;

/**
 * Controller for User Story 012: Get List of Publications in Library.
 *
 * <p>Handles the retrieval of all publications belonging to a user's library
 * and converts them into DTOs for presentation purposes.</p>
 *
 * <p>Depends on the following collaborators:</p>
 * <ul>
 *   <li>{@link IUserRepository} – resolves a username to a {@link UserId}</li>
 *   <li>{@link IPublicationSampleRepository} – fetches publications owned by the user that are in a library</li>
 *   <li>{@link US012PublicationSampleMapper} – converts domain objects to {@link US012PublicationDetailsDTO}</li>
 * </ul>
 */
public class ToGetListOfPublicationsInLibraryController {

    private final IUserRepository userRepository;
    private final IPublicationSampleRepository pubSampleRepo;
    private final US012PublicationSampleMapper mapper;

    /**
     * Constructs a new controller with the required collaborators.
     *
     * @param userRepository the repository used to resolve usernames to user IDs; must not be {@code null}
     * @param pubSampleRepo  the repository used to retrieve publication samples; must not be {@code null}
     * @param mapper         the mapper used to convert publication samples to DTOs; must not be {@code null}
     * @throws NullPointerException if any of the provided collaborators is {@code null}
     */
    public ToGetListOfPublicationsInLibraryController(
            IUserRepository userRepository,
            IPublicationSampleRepository pubSampleRepo,
            US012PublicationSampleMapper mapper) {

        if (userRepository == null) throw new NullPointerException("UserRepository can not be null");
        if (pubSampleRepo == null) throw new NullPointerException("PublicationSampleRepository can not be null");
        if (mapper == null) throw new NullPointerException("Mapper can not be null");

        this.userRepository = userRepository;
        this.pubSampleRepo = pubSampleRepo;
        this.mapper = mapper;
    }

    /**
     * Retrieves all publications in the given user's library as DTOs.
     *
     * <p>Resolves the username to a {@link UserId}, fetches all publications owned
     * by that user that are currently assigned to a library, and converts them
     * to {@link US012PublicationDetailsDTO} objects.</p>
     *
     * @param username the username of the library owner; must not be {@code null} or empty
     * @return a list of {@link US012PublicationDetailsDTO} representing the user's library publications;
     *         never {@code null}, may be empty if the user has no publications in a library
     * @throws NullPointerException     if {@code username} is {@code null}
     * @throws IllegalArgumentException if {@code username} is empty or does not match any known user
     * @throws InstanceNotFoundException if the publications cannot be mapped
     */
    public List<US012PublicationDetailsDTO> getListOfPublicationsInLibraryDTO(String username) throws InstanceNotFoundException {

        if (username == null) {
            throw new NullPointerException("Username cannot be null");
        }

        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username can not be empty");
        }

        UserId userId = userRepository.findUserIdByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("Username is not valid or not found"));

        List<PublicationSample> listOfPublications = pubSampleRepo.findByUserIdAndLibraryIdIsNotNull(userId);

        List<US012PublicationDetailsDTO> listOfPublicationsDTO = mapper.toDTOs(listOfPublications);

        return listOfPublicationsDTO;
    }
}