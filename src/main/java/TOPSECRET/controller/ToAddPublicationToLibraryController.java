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
public class ToAddPublicationToLibraryController {

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
    public ToAddPublicationToLibraryController(ILibraryRepository libraryRepository,
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
        return _publicationSampleRepository.findByUserIdAndLibraryIdIsNull(userId);
    }

    /**
     * Adds a {@link PublicationSample} to the library owned by a given {@link UserId}.
     *
     * <p>This method retrieves the user's {@link Library}, obtains its {@link LibraryId},
     * and then fetches the corresponding {@link PublicationSample} using the provided
     * {@link PublicationSampleId}. The association between the publication and the library
     * is delegated to the {@link IPublicationSampleRepository}, which handles the domain
     * operation and persistence.</p>
     *
     * @param userId the identifier of the user whose library will receive the publication
     * @param publicationSampleId the identifier of the publication sample to be added
     * @return {@code true} if the publication was successfully added to the user's library
     *
     * @throws IllegalArgumentException if {@code userId} is null
     * @throws IllegalArgumentException if {@code publicationSampleId} is null
     * @throws IllegalArgumentException if the publication sample is not found
     * @throws IllegalArgumentException if the publication cannot be added to the library
     *                                  (e.g., already assigned or invalid state)
     * @throws InstanceNotFoundException if the user does not have an associated library
     */
    public boolean addPublicationToLibrary(UserId userId, PublicationSampleId publicationSampleId)
            throws InstanceNotFoundException {

        if (publicationSampleId == null) {
            throw new IllegalArgumentException("PublicationSampleId cannot be null");
        }

        if (userId == null){
            throw new IllegalArgumentException("UserId cannot be null");
        }

        Library library = _libraryRepository.getByUserId(userId);
        LibraryId libraryId = library.id();

        PublicationSample publicationSample = _publicationSampleRepository
                .findById(publicationSampleId)
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));

        _publicationSampleRepository.addPublicationToLibrary(publicationSample, libraryId);

        return true;
    }
}