package TOPSECRET.domain.Factory;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;

public class FactoryPublicationType {

    /**
     * Creates new publication types after validating inputs such as media format, type of binding and details
     *
     * @param mediaFormat can be a book or a magazine
     * @param typeOfBinding examples include, hardback, paperback, saddle stitching, etc
     * @param details any extra details necessary to describe the publication type
     * @return a new publication type
     */

    public PublicationType create(MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details)
    {
        return new PublicationType(mediaFormat, typeOfBinding, details);
    }
}
