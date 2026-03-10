package TOPSECRET.controller;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Publisher;
import TOPSECRET.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US006ToRegisterNewPublisherControllerTest {

    private PublisherRepository repo;

    @BeforeEach
    void setup(){
        repo = mock(PublisherRepository.class);
    }


    @Test
    void ensureConstructorIsBuildSucessfully(){
        //Arrange

        //Act
        US006ToRegisterNewPublisherController ctrl = new US006ToRegisterNewPublisherController(repo);

        //Assert
        assertNotNull(ctrl);
    }

    @Test
    void ensure_ConstructorThrowsException_WhenRepositoryIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new US006ToRegisterNewPublisherController(null));
    }

    @Test
    void registerPublisher_returnTrue_WhenPublisherIsCreated() throws InstanceAlreadyExistsException {
        //Arrange
        String name = "Publisher1";
        String vatNumber = "12345679";

        Publisher publisherDouble = mock(Publisher.class);
        Contact contactDouble = mock(Contact.class);

        when(repo.createAndAdd(name,vatNumber,contactDouble)).thenReturn(publisherDouble);

        US006ToRegisterNewPublisherController ctrl = new US006ToRegisterNewPublisherController(repo);


        //Act
        boolean result = ctrl.registerPublisher(name,vatNumber,contactDouble);

        //Assert
        assertTrue(result);
    }

}