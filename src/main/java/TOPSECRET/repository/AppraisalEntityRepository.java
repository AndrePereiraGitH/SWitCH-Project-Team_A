package TOPSECRET.repository;

import TOPSECRET.domain.AppraisalEntity;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Enum.Genre;
import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

public class AppraisalEntityRepository {

//  //    private List<AppraisalEntity> _appraisalEntityList;
//
//  //    Constructor
//  //    public AppraisalEntityRepository() {
//  //        this._appraisalEntityList = new ArrayList<>();
//  //    }
//
//    // CREATE
//    // Creates and adds a new AppraisalEntity to the lis, only if it is valid and unique
//    public AppraisalEntity createAndAddToAppraisalEntityList (String name, Contact contact, PublicationType publicationType, Genre genre) {
//
//        AppraisalEntity newApEntity;
//
//        try {
//            newApEntity = new AppraisalEntity(name, contact, publicationType, genre);
//        } catch (IllegalArgumentException e) {
//            // Invalid data provided
//            return null;
//        }
//
//        // validate the new appraisal entity
//        if (!validateAppraisalEntity(newApEntity)) {
//            return null;
//        }
//
//        // checking if the new entity is unique in the list
//        if (!isUniqueAppraisalEntity(newApEntity)) {
//            return null;
//        }
//
//        // adding the new entity to the list
//        _appraisalEntityList.add(newApEntity);
//        return newApEntity;
//    }
//
//    // READ
//    public List<AppraisalEntity> getAllAppraisalEntity () {
//        return new ArrayList<>(_appraisalEntityList);
//    }
//
//    // UPDATE
//    public boolean updateAppraisalEntity (AppraisalEntity oldEntity, AppraisalEntity newEntity) {
//        int index = _appraisalEntityList.indexOf(oldEntity);
//        if (index == -1) return false;
//
//        if (!isUniqueAppraisalEntity(newEntity)) return false;
//
//        _appraisalEntityList.set(index, newEntity);
//        return true;
//    }
//
//    //  DELETE
//    //   Removes an AppraisalEntity from the list.
//    //   Returns true if the entity was successfully removed, or false if the entity was not found.
//    public boolean removeAppraisalEntity (AppraisalEntity entity) {
//        return _appraisalEntityList.remove(entity);
//    }
//
//    // Validation method
//    private boolean validateAppraisalEntity (AppraisalEntity apEntity) {
//        if (apEntity == null) return false;
//        if (apEntity.getName() == null || apEntity.getName().isBlank()) return false;
//        if (apEntity.getContact() == null) return false;
//        if (apEntity.getPublicationType() == null) return false;
//        if (apEntity.getGenre() == null) return false;
//
//        return true;
//    }
//
//    // Ensures no duplicates exist in the list.
//    private boolean isUniqueAppraisalEntity (AppraisalEntity apEntity) {
//
//        if (_appraisalEntityList.contains(apEntity)) {
//            return false;   // Entity already exists in the list
//        } else {
//            return true;    // Entity does not exist in the list
//        }
//    }
}
