package TOPSECRET.controller;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.FactoryPublicationType;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.PublicationTypeDTO_US014;
import TOPSECRET.repository.PublicationTypeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US014ToGetPublicationTypeListControllerTest {

    @Test
    void getAllPublicationTypesWhenRepositoryIsEmptyReturnsEmptyList() {

        // Arrange
        PublicationTypeRepository repository = mock(PublicationTypeRepository.class);

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
        PublicationTypeRepository repository = mock(PublicationTypeRepository.class);

        FactoryPublicationType factory = new FactoryPublicationType();

        PublicationType type =
                factory.create(
                        MediaFormat.BOOK,
                        TypeOfBinding.HARDBACK,
                        "Hardback");

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

        assertEquals("BOOK", dto._mediaFormat);
        assertEquals("HARDBACK", dto._typeOfBinding);
        assertEquals("Hardback", dto._details);

        verify(repository).getAllPublicationTypes();
    }

    @Test
    void getAllPublicationTypesMultiplePublicationTypesReturnsListDTOs() {

        // Arrange
        PublicationTypeRepository repository = mock(PublicationTypeRepository.class);

        FactoryPublicationType factory = new FactoryPublicationType();

        PublicationType t1 =
                factory.create(
                        MediaFormat.BOOK,
                        TypeOfBinding.HARDBACK,
                        "Hardback");

        PublicationType t2 =
                factory.create(
                        MediaFormat.BOOK,
                        TypeOfBinding.PAPERBACK,
                        "Paperback");

        when(repository.getAllPublicationTypes())
                .thenReturn(List.of(t1, t2));

        US014ToGetPublicationTypeListController controller =
                new US014ToGetPublicationTypeListController(repository);

        // Act
        List<PublicationTypeDTO_US014> result =
                controller.getAllPublicationTypes();

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(d -> d._details.equals("Hardback")));
        assertTrue(result.stream().anyMatch(d -> d._details.equals("Paperback")));

        verify(repository).getAllPublicationTypes();
    }


}