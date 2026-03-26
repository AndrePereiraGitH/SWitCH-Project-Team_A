package TOPSECRET.controller;

import TOPSECRET.domain.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.PublicationTypeDTO_US014;
import TOPSECRET.repository.IPublicationTypeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US014ToGetPublicationTypeListControllerTest {

    @Test
    void getAllPublicationTypesWhenRepositoryIsEmptyReturnsEmptyList() {

        // Arrange
        IPublicationTypeRepository repository = mock(IPublicationTypeRepository.class);

        when(repository.getAllPublicationTypes()).thenReturn(List.of());

        US014ToGetPublicationTypeListController controller =
                new US014ToGetPublicationTypeListController(repository);

        // Act
        List<PublicationTypeDTO_US014> result = controller.getAllPublicationTypes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).getAllPublicationTypes();
    }

    @Test
    void getAllPublicationTypesOnePublicationTypeReturnsOneDTO() {

        // Arrange
        IPublicationTypeRepository repository = mock(IPublicationTypeRepository.class);

        PublicationTypeFactory factory = new PublicationTypeFactory();

        PublicationType type =
                factory.create(
                        "Book");

        when(repository.getAllPublicationTypes())
                .thenReturn(List.of(type));

        US014ToGetPublicationTypeListController controller =
                new US014ToGetPublicationTypeListController(repository);

        // Act
        List<PublicationTypeDTO_US014> result =
                controller.getAllPublicationTypes();

        // Assert
        assertEquals(1, result.size());

        PublicationTypeDTO_US014 dto = result.get(0);

        assertEquals("Book", dto._mediaFormat);

        verify(repository).getAllPublicationTypes();
    }

    @Test
    void getAllPublicationTypesMultiplePublicationTypesReturnsListDTOs() {

        // Arrange
        IPublicationTypeRepository repository = mock(IPublicationTypeRepository.class);

        PublicationTypeFactory factory = new PublicationTypeFactory();

        PublicationType t1 =
                factory.create(
                        "Book");

        PublicationType t2 =
                factory.create(
                        "Magazine");

        when(repository.getAllPublicationTypes())
                .thenReturn(List.of(t1, t2));

        US014ToGetPublicationTypeListController controller =
                new US014ToGetPublicationTypeListController(repository);

        // Act
        List<PublicationTypeDTO_US014> result =
                controller.getAllPublicationTypes();

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(d -> d._mediaFormat.equals("Book")));
        assertTrue(result.stream().anyMatch(d -> d._mediaFormat.equals("Magazine")));

        verify(repository).getAllPublicationTypes();
    }


}
