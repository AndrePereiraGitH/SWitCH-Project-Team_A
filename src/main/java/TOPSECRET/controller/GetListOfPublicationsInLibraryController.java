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

    public List<PublicationDTO_US012> getListOfPublicationsInLibraryDTO(String username) throws InstanceNotFoundException {

        Library userLib = getUserLibrary(username);

        List<Publication> publications = userLib.getAllPublications();

        List<PublicationDTO_US012> publicationsDTO = PublicationMapper_US012.getListOfPublicationsDTO(publications);

        return publicationsDTO;
    }

}
