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
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.bibliographicalData.BibliographicalDataMapper;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
     * Retrieves all bibliographical data records and converts them into DTO objects.
     *
     * <p>Each {@link BibliographicalData} entity is mapped to a
     * {@link responseBibliographicalDataDTO}, including the list of author names
     * associated with the bibliographical record.</p>
     *
     * @return a list of {@link responseBibliographicalDataDTO} containing all
     *         bibliographical data records
     * @throws InstanceNotFoundException if an expected related instance
     *         (such as an author) cannot be found during processing
     */
    public List<responseBibliographicalDataDTO> createDTOlist(List<BibliographicalData> bibliographicalData) throws InstanceNotFoundException {

        List<responseBibliographicalDataDTO> result = new ArrayList<>();

        for (BibliographicalData bd : bibliographicalData) {
            List<String> authorNames = getAuthors(bd);
            result.add(BibliographicalDataMapper.toDTO(bd, authorNames));
        }

        return result;
    }

}
