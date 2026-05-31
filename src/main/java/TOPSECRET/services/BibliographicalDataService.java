package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.BibliographicalDataAuthorizationPolicy;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.bibliographicalData.BibliographicalDataMapper;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.*;

/**
 * Service responsible for managing bibliographical data operations.
 */
@Service
public class BibliographicalDataService {

    private final IBibliographicalDataRepository bibliographicalDataRepository;
    private final BibliographicalDataFactory bibliographicalDataFactory;
    private final IAuthorRepository authorRepository;
    private final IUserRepository userRepository;
    private final BibliographicalDataAuthorizationPolicy authPolicy;

    /**
     * Constructs a new BibliographicalDataService.
     *
     * @param bibliographicalDataRepository repository used to manage bibliographical data
     * @param bibliographicalDataFactory factory used to create bibliographical data entities
     * @param authorRepository repository used to resolve author information
     * @param userRepository repository used to resolve user information
     * @param authPolicy authorization policy used to validate user permissions
     */
    public BibliographicalDataService(
            IBibliographicalDataRepository bibliographicalDataRepository,
            BibliographicalDataFactory bibliographicalDataFactory,
            IAuthorRepository authorRepository,
            IUserRepository userRepository,
            BibliographicalDataAuthorizationPolicy authPolicy) {

        this.bibliographicalDataRepository = bibliographicalDataRepository;
        this.bibliographicalDataFactory = bibliographicalDataFactory;
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
        this.authPolicy = authPolicy;
    }


    /**
     * Creates a new bibliographical data entity and saves it to the repository.
     *
     * <p>The user is first retrieved and checked against the authorization policy
     * to ensure they have permission to create bibliographical data.</p>
     *
     * @param title the title of the bibliographical data
     * @param authors the identifiers of the authors associated with the bibliographical data
     * @param originalLanguage the original language of the bibliographical data
     * @param publicationDate the publication date of the bibliographical data
     * @param genres the identifiers of the genres associated with the bibliographical data
     * @param chunksOfText the text content or excerpts associated with the bibliographical data
     * @param userId the identifier of the user creating the bibliographical data
     * @return the saved {@link BibliographicalData} entity
     * @throws NullPointerException if title, authors, genres, or userId is null
     * @throws IllegalArgumentException if the user cannot be found
     * @throws AccessDeniedException if the user does not have permission to create bibliographical data
     * @throws InstanceAlreadyExistsException if the bibliographical data already exists
     */
    public BibliographicalData createAndSave(String title,
                                             List<AuthorId> authors,
                                             Locale originalLanguage,
                                             LocalDate publicationDate,
                                             List<GenreId> genres,
                                             String chunksOfText,
                                             UserId userId) throws InstanceAlreadyExistsException, AccessDeniedException {
        Objects.requireNonNull(title, "Title cannot be null");
        Objects.requireNonNull(authors, "Authors cannot be null");
        Objects.requireNonNull(genres, "Genres cannot be null");
        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if(!authPolicy.canCreateBibliographicalData(user)) {
            throw new AccessDeniedException("User does not have permission to create bibliographical data");
        }

        BibliographicalData bibliographicalDataCreated = bibliographicalDataFactory.create(
                title,
                authors,
                originalLanguage,
                publicationDate,
                genres,
                chunksOfText
        );

        return bibliographicalDataRepository.save(bibliographicalDataCreated);
    }

    /**
     * Builds a map associating each bibliographical data entry with its list of author names.
     *
     * <p>For every {@link BibliographicalData} in the provided list, this method retrieves
     * the corresponding authors using {@code getAuthors(BibliographicalData)} and stores
     * them in a map keyed by the bibliographical data identifier.</p>
     *
     * @param bibliographicalDataList the list of bibliographical data entries to process
     * @return a map where the key is the {@link BibliographicalDataId} and the value is
     *         the list of author names associated with that entry
     * @throws InstanceNotFoundException if the authors for a bibliographical data entry
     *         cannot be found
     */
    public Map<BibliographicalDataId, List<String>> buildAuthorNamesMap(
            List<BibliographicalData> bibliographicalDataList)
            throws InstanceNotFoundException {

        Map<BibliographicalDataId, List<String>> result = new HashMap<>();

        for (BibliographicalData bd : bibliographicalDataList) {

            List<String> authorNames = getAuthors(bd);

            result.put(bd.id(), authorNames);
        }
        return result;
    }

    // ===================================================================
    // ========================== GETTERS ================================
    // ===================================================================

    /**
     * Resolves the names of all authors associated with the given
     * {@link BibliographicalData}.
     *
     * @param bd the bibliographical data containing the author identifiers
     * @return a list containing the names of the authors
     * @throws InstanceNotFoundException if any author identifier cannot be resolved
     *                                   in the repository
     */
    public List<String> getAuthors(BibliographicalData bd) throws InstanceNotFoundException {

        List<AuthorId> listOfAuthorsId = bd.getAuthors();

        List<String> authorsName = new ArrayList<>();

        for (AuthorId id : listOfAuthorsId) {
            Author author = authorRepository.findById(id)
                    .orElseThrow(() ->
                            new InstanceNotFoundException("Author was not found:" + id));

            String authorName = author.getName();
            authorsName.add(authorName);
        }

        return authorsName;
    }

    /**
     * Retrieves all bibliographical data records stored in the repository.
     *
     * @return a list containing all {@link BibliographicalData} entities;
     */
    public List<BibliographicalData> findAll() { return bibliographicalDataRepository.findAll(); }

     /**
     * Retrieves a bibliographical data entity stored in the repository.
     *
     * @return an Optional containing the {@link BibliographicalData} entity;
     * @throws IllegalArgumentException if {@link BibliographicalDataId} is null
     */
    public BibliographicalData findById(BibliographicalDataId bdId) throws InstanceNotFoundException {
        if(bdId == null){
            throw new IllegalArgumentException("Bibliographical Data Id cannot be null");
        }

        BibliographicalData bd = bibliographicalDataRepository.findById(bdId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("BibliographicalData was not found:" + bdId));

        return bd;
    }

    /**
     * Retrieves all bibliographical data records stored in the repository of a given genre.
     *
     * @return a list containing all {@link BibliographicalDataId} ids of a given genre;
     * @throws IllegalArgumentException if {@link GenreId} is null
     */
    public List<BibliographicalDataId> findAllByGenreId(GenreId genreId) {
        if (genreId == null){
            throw new IllegalArgumentException("Genre Id cannot be null");
        }
        return bibliographicalDataRepository.findAllByGenreId(genreId);
    }

    /**
     * Retrieves a bibliographical data Id stored in the repository by its title.
     *
     * @return an Optional containing the {@link BibliographicalDataId} id;
     * @throws IllegalArgumentException if title is null
     */
    public Optional<BibliographicalDataId> findIdByTitle(String bdTitle) {
        if (bdTitle == null){
            throw new IllegalArgumentException("Title cannot be null");
        }
        return bibliographicalDataRepository.findIdByTitle(bdTitle);
    }

    /**
     * Retrieves a bibliographical data Id stored in the repository by its author.
     *
     * @return a List containing all the {@link BibliographicalDataId} ids of the given author;
     * @throws IllegalArgumentException if {@link AuthorId} is null
     */
    public List<BibliographicalDataId> findIdByAuthorId(AuthorId authorId) {
        if (authorId == null){
            throw new IllegalArgumentException("Author Id cannot be null");
        }
        return bibliographicalDataRepository.findIdByAuthorId(authorId);
    }

    /**
     * Retrieves all bibliographical data titles stored in the repository.
     *
     * @return a List containing all the titles;
     */
    public List<String> getAllTitles() {
        return bibliographicalDataRepository.getAllTitles();
    }
}
