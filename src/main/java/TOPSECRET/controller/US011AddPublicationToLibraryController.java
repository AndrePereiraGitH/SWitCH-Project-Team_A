package TOPSECRET.controller;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceNotFoundException;
import java.util.List;

/**
 * Controller responsible for handling the addition of a publication to a user's library.
 * <p>
 * Corresponds to User Story US011: As a User, I want to add a Publication to my Library.
 * </p>
 */
public class US011AddPublicationToLibraryController {

    /** Repository used to retrieve and manage {@link Library} instances. */
    private final ILibraryRepository _libraryRepository;

    /** Repository used to retrieve and manage {@link PublicationSample} instances. */
    private final IPublicationSampleRepository _publicationSampleRepository;

    /**
     * Constructs a new {@code US011AddPublicationToLibraryController} with the given repositories.
     *
     * @param libraryRepository           the repository used to manage libraries;
     *                                    must not be {@code null}
     * @param publicationSampleRepository the repository used to manage publication samples;
     *                                    must not be {@code null}
     * @throws IllegalArgumentException if any repository is {@code null}
     */
    public US011AddPublicationToLibraryController(ILibraryRepository libraryRepository,
                                                  IPublicationSampleRepository publicationSampleRepository) {
        if (libraryRepository == null) {
            throw new IllegalArgumentException("LibraryRepository cannot be null");
        }
        if (publicationSampleRepository == null) {
            throw new IllegalArgumentException("PublicationSampleRepository cannot be null");
        }
        this._libraryRepository = libraryRepository;
        this._publicationSampleRepository = publicationSampleRepository;
    }

    /**
     * Returns the list of {@link PublicationSample} instances owned by the user
     * that have not yet been added to any library.
     * <p>
     * These are the publications available for the user to select and add
     * to their library, shown in the UI as a selectable list.
     * </p>
     *
     * @param userId the ID of the user whose publications are checked;
     *               must not be {@code null}
     * @return a list of {@link PublicationSample} instances not yet in any library
     */
    public List<PublicationSample> getListOfPublicationsNotInLibrary(UserId userId) {
        return _publicationSampleRepository.getAllPublicationsFromUserWithoutLibrary(userId);
    }

    /**
     * Adds the {@link PublicationSample} selected by the user to their library.
     * <p>
     * The flow is as follows:
     * <ol>
     *     <li>Retrieves the user's {@link Library} from the library repository
     *         using the {@link UserId}</li>
     *     <li>Retrieves the selected {@link PublicationSample} from the publication
     *         repository using the {@link PublicationSampleId}</li>
     *     <li>Associates the publication with the library via
     *         {@link PublicationSample#addPublicationToLibrary(LibraryId)}</li>
     *     <li>Persists the updated publication sample</li>
     * </ol>
     * </p>
     *
     * @param userId              the ID of the user whose library the publication will be added to;
     *                            must not be {@code null}
     * @param publicationSampleId the ID of the publication selected by the user;
     *                            must not be {@code null}
     * @return {@code true} if the publication was successfully added
     * @throws IllegalArgumentException  if {@code publicationSampleId} is {@code null}
     *                                   or if the publication is not found
     * @throws InstanceNotFoundException if the user does not have a library
     */
    public boolean addPublicationToLibrary(UserId userId, PublicationSampleId publicationSampleId)
            throws InstanceNotFoundException {

        if (publicationSampleId == null) {
            throw new IllegalArgumentException("PublicationSampleId cannot be null");
        }

        // Step 1: get the user's library and its ID
        Library library = _libraryRepository.getLibraryByUserId(userId);
        LibraryId libraryId = library.id();

        // Step 2: get the selected publication sample
        PublicationSample publicationSample = _publicationSampleRepository
                .findPublicationSampleById(publicationSampleId)
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));

        // Step 3: associate the publication with the library
        publicationSample.addPublicationToLibrary(libraryId);

        // Step 4: persist the updated publication sample
        _publicationSampleRepository.save(publicationSample);

        return true;
    }
}