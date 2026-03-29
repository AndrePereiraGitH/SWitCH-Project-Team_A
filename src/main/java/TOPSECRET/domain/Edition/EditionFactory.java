package TOPSECRET.domain.Edition;

import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Factory responsible for creating {@link Edition} instances.
 * <p>
 * This factory centralizes the creation logic of {@code Edition} objects,
 * ensuring minor input normalization before instantiation.
 * <p>
 * Currently, it trims the {@code editionNumber} if it is not {@code null}
 * and delegates validation responsibilities to the {@code Edition} constructor.
 */
public class EditionFactory {
    public Edition create(String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationType publicationType,
                          Publisher publisher, Locale editionLanguage, LocalDate editionDate, TypeOfBinding typeOfBinding){
        return new Edition(editionNumber== null ? null : editionNumber.trim(), code, physicalAttributes, publicationType, publisher, editionLanguage, editionDate, typeOfBinding);
    }
}
