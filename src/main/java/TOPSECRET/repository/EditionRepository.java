package TOPSECRET.repository;

import TOPSECRET.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Repository responsible for managing {@link Edition} entities.
 * <p>
 * Provides operations to create and store new editions while ensuring
 * that duplicate editions are not added.
 */
public class EditionRepository {
    private final List<Edition> _editions;


    public EditionRepository ( List<Edition> editions) {
        if (editions == null) {
            throw new IllegalArgumentException("Editions cannot be null");
        }
        this._editions = editions;
    }

    public List<Edition> createAndAddEdition(String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationType publicationType, Publisher publisher, Locale editionLanguage, LocalDate editionDate){
      Edition newEdition = new Edition(editionNumber, code, physicalAttributes, publicationType, publisher, editionLanguage, editionDate);
      if ( _editions.contains(newEdition)){
          throw new IllegalArgumentException("Edition already exists");
      }
      _editions.add(newEdition);
       return _editions;
    }
}

