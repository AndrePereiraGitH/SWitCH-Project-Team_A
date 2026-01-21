package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.dto.PublicationDTO;

import TOPSECRET.dto.PublicationMapper;
import TOPSECRET.repository.LibraryRepository;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        List<PublicationDTO> dtoList = new ArrayList<>();

        List<Publication> publications = getUserLibrary(username).getAll();


        return PublicationMapper.getListOfPublicationsDTO(publications);
    }

}
