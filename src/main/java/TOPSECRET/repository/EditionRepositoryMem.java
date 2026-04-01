package TOPSECRET.repository;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;
import TOPSECRET.domain.PublicationType.PublicationType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Repository responsible for managing {@link Edition} entities.
 * <p>
 * Provides operations to create and store new editions while ensuring
 * that duplicate editions are not added.
 */
public class EditionRepositoryMem implements IEditionRepository {
    private final List<Edition> _editions = new ArrayList<>();
    private final EditionFactory _editionFactory;

    public EditionRepositoryMem(EditionFactory editionFactory) {
        if (editionFactory == null) {
            throw new IllegalArgumentException("Editions Factory cannot be null");
        }
        this._editionFactory = editionFactory;
    }

    /**
     * {@inheritDoc}
     */
    public List<Edition> createAndAddEdition(String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationType publicationType, Publisher publisher, Locale editionLanguage, LocalDate editionDate, TypeOfBinding typeOfBinding){
      Edition newEdition = _editionFactory.create(editionNumber, code, physicalAttributes, publicationType, publisher, editionLanguage, editionDate, typeOfBinding);
      if ( _editions.contains(newEdition)){
          throw new IllegalArgumentException("Edition already exists");
      }
      _editions.add(newEdition);
       return List.copyOf(_editions);
    }
}

