package TOPSECRET.repository;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.domain.PublicationType.PublicationType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Repository responsible for managing {@link Edition} entities.
 * <p>
 * Provides operations to create and store new editions while ensuring
 * that duplicate editions are not added.
 */
public class EditionRepositoryMem implements IEditionRepository {
    private final List<Edition> _editions;
    private final EditionFactory _editionFactory;

    public EditionRepositoryMem(EditionFactory editionFactory) {

        if (editionFactory == null) {
            throw new IllegalArgumentException("Editions Factory cannot be null");
        }

        this._editions = new ArrayList<>();
        this._editionFactory = editionFactory;
    }

    /**
     * {@inheritDoc}
     */
    public List<Edition> createAndAddEdition(String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationTypeId publicationTypeId, PublisherID publisherID, String mediaFormat, Locale editionLanguage, LocalDate editionDate, TypeOfBinding typeOfBinding){
      Edition newEdition = _editionFactory.create(
              editionNumber,
              code,
              physicalAttributes,
              publicationTypeId,
              publisherID,
              mediaFormat,
              editionLanguage,
              editionDate,
              typeOfBinding
      );
      if ( _editions.contains(newEdition)){
          throw new IllegalArgumentException("Edition already exists");
      }
      _editions.add(newEdition);
       return List.copyOf(_editions);
    }

    /**
     * {@inheritDoc}
     */
    public Optional<Edition> findEditionById(EditionID editionID){

        if(editionID == null){
            throw new NullPointerException("EditionId must not be null");
        }

        for(Edition edition: _editions){
            if(edition.id().equals(editionID)){
                return Optional.of(edition);
            }
        }
        return Optional.empty();
    }
}

