package TOPSECRET.services;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.GenreId;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Service responsible for handling operations related to {@link BibliographicalData}.
 * <p>
 * This service coordinates the creation of bibliographical data objects using
 * the {@link BibliographicalDataFactory} and persists them through the
 * {@link IBibliographicalDataRepository}.
 */
@Service
public class BibliographicalDataService {

    /**
     * Repository used to persist bibliographical data.
     */
    private IBibliographicalDataRepository bibliographicalDataRepository;

    /**
     * Factory responsible for creating {@link BibliographicalData} instances.
     */
    private BibliographicalDataFactory bibliographicalDataFactory;

    /**
     * Repository used to retrieve author entities.
     */
    private IAuthorRepository authorRepository;

    /**
     * Constructs a {@code BibliographicalDataService} with required dependencies.
     *
     * @param bibliographicalDataRepository repository for persistence operations
     * @param bibliographicalDataFactory    factory for creating bibliographical data objects
     * @param authorRepository              repository used to retrieve author entities
     */
    public BibliographicalDataService(
            IBibliographicalDataRepository bibliographicalDataRepository,
            BibliographicalDataFactory bibliographicalDataFactory,
            IAuthorRepository authorRepository) {

        this.bibliographicalDataRepository = bibliographicalDataRepository;
        this.bibliographicalDataFactory = bibliographicalDataFactory;
        this.authorRepository = authorRepository;
    }

    /**
     * Creates a new {@link BibliographicalData} instance and stores it in the repository.
     *
     * @param title              the title of the work (must not be null)
     * @param authors            list of author identifiers (must not be null)
     * @param originalLanguage   the original language of the work
     * @param publicationDate    the publication date of the work
     * @param genres             list of genre identifiers (must not be null)
     * @param chunksOfText       textual content or excerpts associated with the work
     * @return the persisted {@link BibliographicalData} instance
     * @throws NullPointerException if {@code title}, {@code authors}, or {@code genres} are null
            * @throws InstanceAlreadyExistsException if a bibliographical data entry already exists
     *                                        with the same identifying attributes
     */
    public BibliographicalData createAndAdd(String title, List<AuthorId> authors, Locale originalLanguage, LocalDate publicationDate,
                                            List<GenreId> genres, String chunksOfText) throws InstanceAlreadyExistsException
    {
        Objects.requireNonNull(title, "Title cannot be null");
        Objects.requireNonNull(authors, "Authors cannot be null");
        Objects.requireNonNull(genres, "Genres cannot be null");

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

}
