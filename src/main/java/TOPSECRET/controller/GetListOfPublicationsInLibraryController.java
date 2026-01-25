package TOPSECRET.controller;

import TOPSECRET.domain.*;

import TOPSECRET.dto.PublicationDetailsDTO;

import TOPSECRET.dto.PublicationDetailsMapper;
import TOPSECRET.repository.LibraryRepository;


import javax.management.InstanceNotFoundException;
import java.util.List;


public class GetListOfPublicationsInLibraryController {

    private final LibraryRepository _libraryRepo;


    public GetListOfPublicationsInLibraryController(LibraryRepository libraryRepo) {
        this._libraryRepo = libraryRepo;
    }

    public List<PublicationDetailsDTO> getListOfPublicationsInLibraryDTO(String username) throws InstanceNotFoundException {

        Library userLib = getUserLibrary(username);

        List<Publication> publications = userLib.getAllPublications();

        List<PublicationDetailsDTO> listOfPublicationsDTO = PublicationDetailsMapper.toDTOs(publications);

        return listOfPublicationsDTO;
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

}
