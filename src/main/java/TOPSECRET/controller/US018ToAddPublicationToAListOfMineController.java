package TOPSECRET.controller;

import TOPSECRET.repository.CollectionOfPublicationsRepository;

// Controller for US018 - Add publication to private collection.
// Acts as the entry point between UI and repository.
// Review the code after all the changes in the necessary classes
// Check the methods and interactions
public class US018ToAddPublicationToAListOfMineController {

    private CollectionOfPublicationsRepository _repository;

    // Constructor
    public US018ToAddPublicationToAListOfMineController() {
        this._repository = new CollectionOfPublicationsRepository();
    }

}

