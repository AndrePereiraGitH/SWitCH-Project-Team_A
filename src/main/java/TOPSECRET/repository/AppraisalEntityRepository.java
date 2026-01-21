package TOPSECRET.repository;

import TOPSECRET.domain.AppraisalEntity;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

public class AppraisalEntityRepository {

    private List<AppraisalEntity> _appraisalEntityList;

    // Constructor
    public AppraisalEntityRepository() {
        this._appraisalEntityList = new ArrayList<>();
    }

    // CREATE
    // Creates and adds a new AppraisalEntity to the lis, only if it is valid and unique
    public AppraisalEntity createAndAddToAppraisalEntityList (String name, Contact contact, PublicationType publicationType, Genre genre) {

        // Create AppraisalEntity
        AppraisalEntity newApEntity  = new AppraisalEntity(name, contact, publicationType, genre);


        // checking if the new entity is unique in the list
        if (!existAppraisalEntity(newApEntity)) {
            // adding to the list
            this._appraisalEntityList.add(newApEntity);
            return newApEntity;
        }
        throw new IllegalArgumentException("AppraisalEntity already exists");

    }

    // Ensures no duplicates exist in the list.
    private boolean existAppraisalEntity (AppraisalEntity apEntity) {

        for (AppraisalEntity newApEntity : this._appraisalEntityList) {
            if (apEntity.equals(newApEntity)) {
                return true;   // Entity already exists in the list
            }
        }
            return false;    // Entity does not exist in the list
    }
}
