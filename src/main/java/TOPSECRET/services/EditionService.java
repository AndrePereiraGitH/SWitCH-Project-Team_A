package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.EditionAuthorizationPolicy;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Application service responsible for managing {@link Edition} creation and persistence.
 *
 * <p>This service coordinates the interaction between the domain layer and the
 * persistence layer. It delegates the creation of {@link Edition} aggregates
 * to the {@link EditionFactory} and ensures their persistence through the
 * {@link IEditionRepository}. Authorization is enforced via
 * {@link EditionAuthorizationPolicy} before any state-changing operation.</p>
 */
@Service
public class EditionService {

    private final IEditionRepository editionRepository;
    private final EditionFactory factory;
    private final EditionAuthorizationPolicy policy;
    private final IUserRepository userRepository;

    /**
     * Constructs an {@code EditionService} with the required collaborators.
     *
     * @param editionRepository the repository used to persist editions; must not be {@code null}
     * @param factory           the factory used to create {@link Edition} domain objects; must not be {@code null}
     * @param policy            the authorization policy for edition operations; must not be {@code null}
     * @param userRepository    the repository used to retrieve users; must not be {@code null}
     */
    public EditionService(IEditionRepository editionRepository,
                          EditionFactory factory,
                          EditionAuthorizationPolicy policy,
                          IUserRepository userRepository) {
        this.editionRepository = Objects.requireNonNull(editionRepository, "Edition Repository must not be null");
        this.factory = Objects.requireNonNull(factory, "Edition Factory must not be null");
        this.policy = Objects.requireNonNull(policy, "Edition Authorization Policy must not be null");
        this.userRepository = Objects.requireNonNull(userRepository, "User Repository must not be null");
    }

    /**
     * Creates a new {@link Edition} and persists it in the repository.
     *
     * <p>The acting user is fetched from the repository and checked against
     * the {@link EditionAuthorizationPolicy}. If the user does not have
     * permission, an {@link AccessDeniedException} is thrown.</p>
     *
     * @param editionNumber      the edition number identifier
     * @param code               the unique code associated with the edition
     * @param physicalAttributes the physical characteristics of the edition
     * @param publicationTypeId  the type of publication
     * @param publisherID        the publisher responsible for the edition
     * @param mediaFormat        the format of the media (e.g., Book, Magazine)
     * @param editionLanguage    the language of the edition
     * @param editionDate        the publication date
     * @param typeOfBinding      the binding type of the edition
     * @param userId             the ID of the user performing the operation
     * @return the persisted {@link Edition}
     * @throws AccessDeniedException if the user does not have permission
     * @throws NullPointerException  if any of the parameters is {@code null}
     */
    public Edition createAndSave(String editionNumber,
                                 Code code,
                                 PhysicalAttributes physicalAttributes,
                                 PublicationTypeId publicationTypeId,
                                 PublisherID publisherID,
                                 String mediaFormat,
                                 Locale editionLanguage,
                                 LocalDate editionDate,
                                 TypeOfBinding typeOfBinding,
                                 UserId userId) throws AccessDeniedException {

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!policy.canCreateEdition(user)) {
            throw new AccessDeniedException(
                    "You do not have permission to register an edition.");
        }

        Edition editionCreated = factory.create(
                editionNumber,
                code,
                physicalAttributes,
                publicationTypeId,
                publisherID,
                mediaFormat,
                editionLanguage,
                editionDate,
                typeOfBinding);

        return editionRepository.save(editionCreated);
    }

    /**
     * Retrieves an {@link Edition} by its unique identifier.
     *
     * @param editionId the identifier of the edition to retrieve; must not be {@code null}
     * @return the {@link Edition} associated with the given identifier
     * @throws NullPointerException if {@code editionId} is {@code null}
     */
    public Edition findById(EditionId editionId) {
        Objects.requireNonNull(editionId, "EditionId cannot be null");

        return editionRepository.findById(editionId).get();
    }

    /**
     * Retrieves all editions stored in the repository.
     *
     * @return a list containing all {@link Edition} instances
     */
    public List<Edition> findAll() {
        return editionRepository.findAll();
    }

    /**
     * Retrieves the identifiers of editions associated with a given publisher.
     *
     * @param publisherId the identifier of the publisher; must not be {@code null}
     * @return a list of {@link EditionId} associated with the given publisher
     * @throws NullPointerException if {@code publisherId} is {@code null}
     */
    public List<EditionId> findIdsByPublisherId(PublisherID publisherId) {

        Objects.requireNonNull(publisherId, "Publisher Id cannot be null");

        return editionRepository.findIdsByPublisherId(publisherId);
    }

    /**
     * Retrieves all editions associated with a given publication type.
     *
     * @param publicationTypeId the identifier of the publication type; must not be {@code null}
     * @return a list of {@link Edition} instances associated with the given publication type
     * @throws NullPointerException if {@code publicationTypeId} is {@code null}
     */
    public List<Edition> findByPublicationTypeId(PublicationTypeId publicationTypeId) {
        Objects.requireNonNull(publicationTypeId, "PublicationSampleID cannot be null");

        return editionRepository.findByPublicationTypeId(publicationTypeId);
    }

    /**
     * Retrieves all editions associated with a given publisher.
     *
     * @param publisherID the identifier of the publisher; must not be {@code null}
     * @return a list of {@link Edition} instances associated with the given publisher
     * @throws NullPointerException if {@code publisherID} is {@code null}
     */
    public List<Edition> findByPublisherId(PublisherID publisherID) {
        Objects.requireNonNull(publisherID, "PublisherId cannot be null");
        return editionRepository.findByPublisherId(publisherID);
    }

    /**
     * Determines whether the specified user is authorized to create
     * {@link Edition} resources.
     *
     * <p>The user is resolved from the repository and evaluated against
     * the edition authorization policy.</p>
     *
     * @param userId the identifier of the user to validate; must not be {@code null}
     * @return {@code true} if the user is allowed to create editions;
     *         {@code false} otherwise
     * @throws InstanceNotFoundException if no user exists for the given identifier
     */
    public boolean canCreateEdition(UserId userId)
            throws InstanceNotFoundException {

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found"));

        return policy.canCreateEdition(user);
    }


}
