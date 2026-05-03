package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * JPA data model representing a publisher in the persistence layer.
 *
 * <p>This entity is mapped to the {@code Publishers} table and stores the publisher's
 * identifier, name, and contact information.</p>
 *
 * <p>This class is a persistence-layer entity and should not be used directly
 * in domain logic. Domain objects should be mapped to and from this class
 * via {@link TOPSECRET.persistence.repositoryjpa.assembler.PublisherAssembler}.</p>
 */
@Entity
@Table(name = "Publishers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublisherDM {

    @EmbeddedId
    @AttributeOverride(name = "_publisherID", column = @Column(name = "publisher_id"))
    private PublisherID id;

    private String name;

    @Embedded
    @AttributeOverrides({
                @AttributeOverride(name = "_phoneNum",                column = @Column(name = "phone_number")),
                @AttributeOverride(name = "_extension",               column = @Column(name = "phone_extension")),
                @AttributeOverride(name = "_email.userName",          column = @Column(name = "email_username")),
                @AttributeOverride(name = "_email.domain",            column = @Column(name = "email_domain")),
                @AttributeOverride(name = "_location.street",         column = @Column(name = "address_street")),
                @AttributeOverride(name = "_location.number",         column = @Column(name = "address_number")),
                @AttributeOverride(name = "_location.zipCode",        column = @Column(name = "address_zip_code")),
                @AttributeOverride(name = "_location.cityID._cityID", column = @Column(name = "city_id"))
    })
    private Contact contact;

    public PublisherDM(PublisherID id, String name, Contact contact) {
        this.id = Objects.requireNonNull(id, "PublisherID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.contact = Objects.requireNonNull(contact, "Contact cannot be null");
    }
}

