package TOPSECRET.controller;

import TOPSECRET.domain.*;

import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US012PublicationDetailsDTO;
import TOPSECRET.dto.US012PublicationSampleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US012GetListOfPublicationsInLibraryController}.
 *
 * <p>Verifies the controller's behaviour when retrieving publications from a user's
 * library and converting them to DTOs, covering input validation, delegation to
 * collaborators, and edge cases.</p>
 *
 * <p>All collaborators ({@link IUserRepository}, {@link IPublicationSampleRepository},
 * {@link US012PublicationSampleMapper}) are replaced with Mockito doubles to isolate
 * the controller from persistence and mapping concerns.</p>
 *
 * <p>Test coverage includes:</p>
 * <ul>
 *   <li><b>Input validation</b> – null and empty username guards.</li>
 *   <li><b>User lookup</b> – exception when username is not found in the repository.</li>
 *   <li><b>Happy path</b> – correct DTOs returned when user has publications in library.</li>
 *   <li><b>Edge case</b> – empty list returned when user has no publications in library.</li>
 * </ul>
 */
class US012GetListOfPublicationsInLibraryControllerTest {

    private IUserRepository userRepo;
    private IPublicationSampleRepository pubRepo;
    private US012PublicationSampleMapper mapper;


    // ============ Tests ============
    @BeforeEach
    void setUp(){
        userRepo = mock(IUserRepository.class);
        pubRepo = mock(IPublicationSampleRepository.class);
        mapper = mock(US012PublicationSampleMapper.class);
    }


    @Test
    void getListOfPublicationsInLibraryDTO_shouldThrowExceptionWhenUserNameIsEmpty() {
        // Arrange
        String emptyUser = "";

        US012GetListOfPublicationsInLibraryController ctrl =
                new US012GetListOfPublicationsInLibraryController(userRepo,pubRepo,mapper);


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ctrl.getListOfPublicationsInLibraryDTO(emptyUser));
    }

    @Test
    void getListOfPublicationsInLibraryDTO_shouldThrowExceptionWhenUserNameIsNull() {
        // Arrange
        String emptyUser = "";

        US012GetListOfPublicationsInLibraryController ctrl =
                new US012GetListOfPublicationsInLibraryController(userRepo,pubRepo,mapper);


        // Act & Assert
        assertThrows(NullPointerException.class, () -> ctrl.getListOfPublicationsInLibraryDTO(null));
    }


    @Test
    void getListOfPublicationsInLibraryDTO_shouldReturnEmptyList_whenUserHasNoPublicationsInLibrary() throws InstanceNotFoundException {
        // Arrange
        String username = "User1";

        User user = mock(User.class);
        UserId userId = mock(UserId.class);

        when(user.id()).thenReturn(userId);
        when(user.getUserName()).thenReturn(username);

        when(userRepo.findUserIdByUsername(username)).thenReturn(Optional.of(userId));

        US012GetListOfPublicationsInLibraryController ctrl =
                new US012GetListOfPublicationsInLibraryController(userRepo,pubRepo,mapper);

        // Act
        List<US012PublicationDetailsDTO> result = ctrl.getListOfPublicationsInLibraryDTO(username);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getListOfPublicationsInLibraryDTO_shouldThrownAnException_whenUserIsInvalidOrNotFound() {
        // Arrange
        String username = "User1";

        User user = mock(User.class);
        UserId userId = mock(UserId.class);

        when(user.id()).thenReturn(userId);
        when(user.getUserName()).thenReturn(username);

        when(userRepo.findUserIdByUsername(username)).thenReturn(Optional.empty());

        US012GetListOfPublicationsInLibraryController ctrl =
                new US012GetListOfPublicationsInLibraryController(userRepo,pubRepo,mapper);


        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> ctrl.getListOfPublicationsInLibraryDTO(username));
    }


    @Test
    void getListOfPublicationsInLibraryDTO_ensureListOfPublicationsInLibraryReturnsDTOs() throws InstanceNotFoundException {
        // Arrange
        String username = "User1";
        UserId userId = mock(UserId.class);

        when(userRepo.findUserIdByUsername(username)).thenReturn(Optional.of(userId));


        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        List<PublicationSample> listOfPubs = List.of(pbSample_1,pbSample_2);
        when(pubRepo.getAllPublicationsInUserLibrary(userId)).thenReturn(listOfPubs);

        US012PublicationDetailsDTO dto1 = mock(US012PublicationDetailsDTO.class);
        US012PublicationDetailsDTO dto2 = mock(US012PublicationDetailsDTO.class);
        List<US012PublicationDetailsDTO> listOfPubsDTO = List.of(dto1,dto2);
        when(mapper.toDTOs(listOfPubs)).thenReturn(listOfPubsDTO);

        US012GetListOfPublicationsInLibraryController ctrl =
                new US012GetListOfPublicationsInLibraryController(userRepo,pubRepo,mapper);

        // Act
        List<US012PublicationDetailsDTO> result = ctrl.getListOfPublicationsInLibraryDTO(username);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

}