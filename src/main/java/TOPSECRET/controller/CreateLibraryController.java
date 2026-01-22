package TOPSECRET.controller;

import TOPSECRET.domain.Library;
import TOPSECRET.domain.User;
import TOPSECRET.repository.LibraryRepository;

public class CreateLibraryController {

    private final LibraryRepository _libraryRepository;

    CreateLibraryController(LibraryRepository libraryRepository){
        if (libraryRepository == null) {
            throw new IllegalArgumentException("libraryRepository cannot be null");
        }

        _libraryRepository = libraryRepository;
    }

    public boolean createAndAddLibrary(String libraryName, User user) {
        Library library = new Library(libraryName, user);
        return _libraryRepository.create(user.getUserName(), library);
    }
}
