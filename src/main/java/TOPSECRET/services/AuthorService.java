package TOPSECRET.services;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.CountryID;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;

/**
 * Service responsible for author-related application operations.
 *
 * <p>
 * This service coordinates the creation of {@link Author} domain objects
 * through {@link AuthorFactory} and their persistence through
 * {@link IAuthorRepository}.
 * </p>
 */
@Service
public class AuthorService {

    private final AuthorFactory authorFactory;
    private final IAuthorRepository authorRepo;

    /**
     * Creates an instance of {@code AuthorService}.
     *
     * @param authorFactory the factory used to create {@link Author} domain objects
     * @param authorRepo the repository used to persist and retrieve authors
     */
    public AuthorService(AuthorFactory authorFactory, IAuthorRepository authorRepo) {
        this.authorFactory = authorFactory;
        this.authorRepo = authorRepo;
    }

    /**
     * Creates a new {@link Author} and persists it.
     *
     * <p>
     * The author is first created using the {@link AuthorFactory}. After that,
     * it is saved through the {@link IAuthorRepository}. The operation runs
     * within a transaction.
     * </p>
     *
     * @param name the author's name
     * @param countryID the identifier of the author's country
     * @param dateOfBirth the author's date of birth
     * @param biography the author's biography
     * @return the persisted {@link Author}
     * @throws InstanceAlreadyExistsException if an author with the same identifier already exists
     * @throws IllegalArgumentException if any invalid argument is rejected by the domain factory or repository
     */
    @Transactional
    public Author createAndSave(String name, CountryID countryID, LocalDate dateOfBirth, String biography) throws InstanceAlreadyExistsException {

         Author author= authorFactory.create(name, countryID, dateOfBirth, biography);

         return authorRepo.save(author);
    }
}
