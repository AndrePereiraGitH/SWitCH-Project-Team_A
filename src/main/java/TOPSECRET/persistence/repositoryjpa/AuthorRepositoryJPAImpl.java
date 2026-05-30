package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.persistence.repositoryjpa.assembler.AuthorAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuthorDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IAuthorRepositoryJPA;
import org.springframework.stereotype.Repository;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;


/**
 * JPA implementation of the {@link IAuthorRepository} interface.
 *
 * <p>
 * This repository is responsible for persisting and retrieving {@link Author}
 * domain objects using a Spring Data JPA repository. It converts between
 * domain objects and persistence data models through {@link AuthorAssembler}.
 * </p>
 */
@Repository
public class AuthorRepositoryJPAImpl implements IAuthorRepository {

    private IAuthorRepositoryJPA authorRepositoryJpa;

    private AuthorAssembler authorAssembler;


    public AuthorRepositoryJPAImpl(IAuthorRepositoryJPA authorRepositoryJpa, AuthorAssembler authorAssembler) {
        this.authorRepositoryJpa = authorRepositoryJpa;
        this.authorAssembler = authorAssembler;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Author save(Author author) throws InstanceAlreadyExistsException {

        if (author==null) {
            throw new IllegalArgumentException("author cannot be null");
        }

        if (existsById(author.id())) {
            throw new InstanceAlreadyExistsException("AuthorId already exists");
        }

        AuthorDM authorDM = authorAssembler.toDM(author);

        AuthorDM authorDMPersisted = authorRepositoryJpa.save(authorDM);

        return authorAssembler.toDomain(authorDMPersisted);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public boolean existsById(AuthorId authorId) {

        if (authorId == null) {
            throw new IllegalArgumentException("authorId cannot be null");
        }

        return authorRepositoryJpa.existsById(authorId);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public List<Author> findAll() {

        List<AuthorDM> allAuthors = authorRepositoryJpa.findAll();

        return allAuthors.stream().map(authorAssembler::toDomain).toList();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Optional<Author> findById(AuthorId id) {

        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        Optional<AuthorDM> authorDM = authorRepositoryJpa.findById(id);

        return authorDM.map(authorAssembler::toDomain);

    }
}
