package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;
import TOPSECRET.domain.PublicationType;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface EditionRepository {
    List<Edition> createAndAddEdition(String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationType publicationType,
                                      Publisher publisher, Locale editionLanguage, LocalDate editionDate);
}
