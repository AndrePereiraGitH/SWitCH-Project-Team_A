package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuthorDM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Assembler responsible for converting between the {@link Author} domain object
 * and the {@link AuthorDM} JPA data model.
 *
 * <p>
 * This class is used in the persistence layer to translate domain objects into
 * database entities and database entities back into domain objects.
 * </p>
 */
@Service
public class AuthorAssembler {

    /**
     * Factory used to recreate {@link Author} domain objects from persisted data.
     */
    @Autowired
    private AuthorFactory authorFactory;


    /**
     * Converts an {@link Author} domain object into an {@link AuthorDM} data model.
     *
     * @param author the domain author to convert
     * @return an {@link AuthorDM} containing the same author data
     * @throws IllegalArgumentException if {@code author} is {@code null}
     */
    public AuthorDM toDM(Author author) {

        if (author == null){
            throw new IllegalArgumentException("Author cannot be null");
        }

        return new AuthorDM(
                author.id(),
                author.getName(),
                author.getCountryID(),
                author.getDateOfBirth(),
                author.getBiography()
                );
    }


    /**
     * Converts an {@link AuthorDM} data model into an {@link Author} domain object.
     *
     * <p>
     * The author is recreated through {@link AuthorFactory}, preserving the
     * identifier stored in the data model.
     * </p>
     *
     * @param authorDM the data model to convert
     * @return an {@link Author} domain object containing the same author data
     * @throws IllegalArgumentException if {@code authorDM} is {@code null}
     */
    public Author toDomain (AuthorDM authorDM) {

        if (authorDM == null) {
            throw new IllegalArgumentException("authorDM cannot be null");
        }

        return authorFactory.create(
                authorDM.getId(),
                authorDM.getName(),
                authorDM.getCountryId(),
                authorDM.getDateOfBirth(),
                authorDM.getBiography()
        );
    }

}
