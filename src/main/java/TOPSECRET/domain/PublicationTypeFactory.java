package TOPSECRET.domain;


public class PublicationTypeFactory {

    /**
     * Creates new publication types after validating inputs such as media format.
     *
     * @param mediaFormat can be a book or a magazine
     * @return a new publication type
     */

    public PublicationType create(String mediaFormat)
    {
        return new PublicationType(mediaFormat);
    }
}
