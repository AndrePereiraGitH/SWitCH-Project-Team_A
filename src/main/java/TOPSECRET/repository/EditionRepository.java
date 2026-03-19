package TOPSECRET.repository;

import TOPSECRET.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface EditionRepository {
    List<Edition> createAndAddEdition(String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationType publicationType,
                                      Publisher publisher, Locale editionLanguage, LocalDate editionDate);
}
