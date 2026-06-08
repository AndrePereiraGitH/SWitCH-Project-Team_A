package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.PublicationSampleService;

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

    /** Service used to retrieve and manage {@link PublicationSample} instances. */
    private final PublicationSampleService _service;

    /**
     * Constructs a new {@code ToAddPublicationToLibraryController} with the given repository and service.
     *
     * @param libraryRepository        the repository used to manage libraries;
     * must not be {@code null}
     * @param publicationSampleService the service used to manage publication samples;
     * must not be {@code null}
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public ToAddPublicationToLibraryController(ILibraryRepository libraryRepository,
                                               PublicationSampleService publicationSampleService) {
        if (libraryRepository == null) {
            throw new IllegalArgumentException("LibraryRepository cannot be null");
        }
        if (publicationSampleService == null) {
            throw new IllegalArgumentException("PublicationSampleService cannot be null");
        }
        this._libraryRepository = libraryRepository;
        this._service = publicationSampleService;
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
     * must not be {@code null}
     * @return a list of {@link PublicationSample} instances not yet in any library
     */
    public List<PublicationSample> getListOfPublicationsNotInLibrary(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        return _service.findPublicationsNotInLibrary(userId);
    }

    /**
     * Adds a {@link PublicationSample} to the library owned by a given {@link UserId}.
     *
     * <p>This method retrieves the user's {@link Library}, obtains its {@link LibraryId},
     * and then delegates the association to the {@link PublicationSampleService}.</p>
     *
     * @param userId              the identifier of the user whose library will receive the publication
     * @param publicationSampleId the identifier of the publication sample to be added
     * @return {@code true} if the publication was successfully added to the user's library
     *
     * @throws IllegalArgumentException if {@code userId} or {@code publicationSampleId} is null
     * @throws InstanceNotFoundException if the user does not have an associated library
     */
    public boolean addPublicationToLibrary(UserId userId, PublicationSampleId publicationSampleId)
            throws InstanceNotFoundException {

        if (publicationSampleId == null) {
            throw new IllegalArgumentException("PublicationSampleId cannot be null");
        }

        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }


        PublicationSample updatedSample = _service.addPublicationToLibrary(publicationSampleId, userId);

        return updatedSample != null;
    }
}