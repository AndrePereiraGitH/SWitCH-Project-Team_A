package TOPSECRET.repository;

import TOPSECRET.domain.AppraisalEntity;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

public class AppraisalEntityRepository {

    private List<AppraisalEntity> _appraisalEntities;

    /**
     * Constructor
     */
    public AppraisalEntityRepository() {
        this._appraisalEntities = new ArrayList<>();
    }

    /**
     * US004
     * Creates and stores a new AppraisalEntity if it does not already exist.
     */
    public AppraisalEntity createAndAddAppraisalEntity(String name, Contact contact, List<PublicationType> publicationTypes, List<Genre> genres) {

        if (existsByNameAndContact(name, contact)) {
            throw new IllegalArgumentException(
                    "Appraisal Entity with same name and contact already exists.");
        }

        AppraisalEntity entity = new AppraisalEntity(name, contact, publicationTypes, genres);

        _appraisalEntities.add(entity);

        return entity;
    }

    /**
     * US004
     * Verifies if an AppraisalEntity already exists with the same name and contact.
     *
     * @param name Name to verify.
     * @param contact Contact to verify.
     * @return true if entity already exists, false otherwise.
     */
    private boolean existsByNameAndContact(String name, Contact contact) {

        for (AppraisalEntity entity : _appraisalEntities) {

            if (entity.getName().equalsIgnoreCase(name)
                    && entity.getContact().equals(contact)) {
                return true;
            }
        }

        return false;
    }
}
