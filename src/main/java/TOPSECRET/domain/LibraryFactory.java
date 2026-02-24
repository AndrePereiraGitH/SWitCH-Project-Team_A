package TOPSECRET.domain;

public class LibraryFactory {

    public Library create(String libraryName, User owner){

        Library newLibrary = new Library(libraryName,owner);

        return newLibrary;
    }

}
