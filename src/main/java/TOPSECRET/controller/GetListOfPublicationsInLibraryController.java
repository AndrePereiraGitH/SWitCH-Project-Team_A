package TOPSECRET.controller;

import TOPSECRET.domain.*;

import TOPSECRET.dto.PublicationDTO;

import TOPSECRET.dto.PublicationMapper;
import TOPSECRET.repository.LibraryRepository;


import java.util.List;


public class GetListOfPublicationsInLibraryController {

    private final LibraryRepository _libraryRepo;


    public GetListOfPublicationsInLibraryController(LibraryRepository libraryRepo) {
        this._libraryRepo = libraryRepo;
    }


    public Library getUserLibrary(String userName){
        if(userName==null){
            throw new IllegalArgumentException("Username can not be null");
        }

        if(userName.isEmpty()){
            throw new IllegalArgumentException("Name can not be empty");
        }

        Library userLibrary = _libraryRepo.getLibraryOfUser(userName);

        return userLibrary;
    }

    public List<PublicationDTO> getListOfPublicationsInLibrary(String username){

        List<Publication> publications = getUserLibrary(username).getAll();

        return PublicationMapper.getListOfPublicationsDTO(publications);
    }

}
