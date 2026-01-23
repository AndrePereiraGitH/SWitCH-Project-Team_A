package TOPSECRET.controller;

import TOPSECRET.domain.*;

import TOPSECRET.dto.PublicationDTO_US012;

import TOPSECRET.dto.PublicationMapper_US012;
import TOPSECRET.repository.LibraryRepository;


import javax.management.InstanceNotFoundException;
import java.util.List;


public class GetListOfPublicationsInLibraryController {

    private final LibraryRepository _libraryRepo;


    public GetListOfPublicationsInLibraryController(LibraryRepository libraryRepo) {
        this._libraryRepo = libraryRepo;
    }


    public Library getUserLibrary(String userName) throws InstanceNotFoundException {
        if(userName==null){
            throw new IllegalArgumentException("Username can not be null");
        }

        if(userName.isEmpty()){
            throw new IllegalArgumentException("Name can not be empty");
        }

        Library userLibrary = _libraryRepo.getLibraryOfUser(userName);

        return userLibrary;
    }

    public List<PublicationDTO_US012> getListOfPublicationsInLibrary(String username) throws InstanceNotFoundException {

        List<Publication> publications = getUserLibrary(username).getAllPublications();

        return PublicationMapper_US012.getListOfPublicationsDTO(publications);
    }

}
