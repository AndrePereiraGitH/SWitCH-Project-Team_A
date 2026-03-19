package TOPSECRET.controller;

import TOPSECRET.domain.*;

import TOPSECRET.dto.PublicationDetailsDTO_US012;
import TOPSECRET.repository.ILibraryRepository;



import javax.management.InstanceNotFoundException;
import java.util.List;

/**
 * Controller for User Story 012: Get List of Publications in Library.
 * <p>
 * This controller handles the retrieval of all publications from a user's library
 * and converts them to DTOs for presentation purposes.
 * </p>
 *
 * @see PublicationDetailsDTO_US012
 *
 */
public class US012GetListOfPublicationsInLibraryController {

    private final ILibraryRepository _libraryRepo;

    /**
     * Constructs a new controller with the specified library repository.
     *
     * @param libraryRepo the repository for accessing library data
     */
    public US012GetListOfPublicationsInLibraryController(ILibraryRepository libraryRepo) {
        this._libraryRepo = libraryRepo;
    }

    /**
     * Retrieves all publications from a user's library as DTOs.
     * <p>
     * This method fetches all publications associated with the specified user's library
     * and converts them to PublicationDetailsDTO_US012 objects for presentation.
     * </p>
     *
     * @param username the username of the library owner
     * @return a list of PublicationDetailsDTO_US012 objects representing all publications in the user's library
     * @throws IllegalArgumentException if the username is empty
     * @throws InstanceNotFoundException if the user's library cannot be found
     */
    public List<PublicationDetailsDTO_US012> getListOfPublicationsInLibraryDTO(String username) throws InstanceNotFoundException {

        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username can not be empty");
        }

        List<PublicationSample> listOfAllPublications = _libraryRepo.getAllPublicationsFromUserLibrary(username);

        List<PublicationDetailsDTO_US012> listOfPublicationsDTO = PublicationDetailsDTO_US012.toDTOs(listOfAllPublications);

        return listOfPublicationsDTO;
    }
}