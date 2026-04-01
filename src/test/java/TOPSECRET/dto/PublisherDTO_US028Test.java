package TOPSECRET.dto;

import TOPSECRET.domain.Publisher.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublisherDTO_US028Test {

    // ===============================================
    // ================= Set Up ======================
    // ===============================================

    private String name;
    private String vatNumber;
    private Publisher mockPublisher;

    @BeforeEach
    void setUp() {
        name = "publisher name";
        vatNumber = "123456";

        mockPublisher = mock(Publisher.class);
        when(mockPublisher.getName()).thenReturn(name);
        when(mockPublisher.getVatNumber()).thenReturn(vatNumber);
    }

    // ===============================================
    // ================= Tests =======================
    // ===============================================

    // Tests to constructor ============================================================================================

    @Test
    void constructor_ShouldStoreValuesCorrectly() {
        // Act
        PublisherDTO_US028 dto = new PublisherDTO_US028(name, vatNumber);

        // Arrange
        assertEquals(name, dto.getName());
        assertEquals(vatNumber, dto.getVatNumber());
    }

    // Tests to equals and hashcode ====================================================================================

    @Test
    void equals_ShouldReturnTrueForEqualObjects() {
        // Act
        PublisherDTO_US028 dto1 = new PublisherDTO_US028(name, vatNumber);
        PublisherDTO_US028 dto2 = new PublisherDTO_US028(name, vatNumber);

        // Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalseForDifferentNames() {
        // Act
        PublisherDTO_US028 dto1 = new PublisherDTO_US028(name, vatNumber);
        PublisherDTO_US028 dto2 = new PublisherDTO_US028("Different name", vatNumber);

        // Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentVatNumber() {
        // Act
        PublisherDTO_US028 dto1 = new PublisherDTO_US028(name, vatNumber);
        PublisherDTO_US028 dto2 = new PublisherDTO_US028(name, "90928294");

        // Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_ShouldReturnFalseWhenComparingWithNullName() {
        // Act
        PublisherDTO_US028 dto1 = new PublisherDTO_US028(name, vatNumber);
        PublisherDTO_US028 dto2 = new PublisherDTO_US028(null, vatNumber);

        // Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_ShouldReturnFalseWhenComparingWithNullVatNumber() {
        // Act
        PublisherDTO_US028 dto1 = new PublisherDTO_US028(name, vatNumber);
        PublisherDTO_US028 dto2 = new PublisherDTO_US028(name, null);

        // Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_ShouldBeReflexive() {
        // Act
        PublisherDTO_US028 dto = new PublisherDTO_US028(name, vatNumber);

        // Assert
        assertEquals(dto, dto);
    }

    @Test
    void equals_ShouldBeSymmetric() {
        // Act
        PublisherDTO_US028 dto1 = new PublisherDTO_US028(name, vatNumber);
        PublisherDTO_US028 dto2 = new PublisherDTO_US028(name, vatNumber);

        // Assert
        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_ShouldReturnFalseWhenComparedWithNull() {
        // Act
        PublisherDTO_US028 dto = new PublisherDTO_US028(name, vatNumber);

        // Assert
        assertFalse(dto.equals(null));
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedWithDifferentClass() {
        // Act
        PublisherDTO_US028 dto = new PublisherDTO_US028(name, vatNumber);

        // Assert
        assertFalse(dto.equals("not a dto"));
    }

    @Test
    void hashCode_ShouldDifferWhenNameDiffer() {
        // Act
        PublisherDTO_US028 dto1 = new PublisherDTO_US028(name, vatNumber);
        PublisherDTO_US028 dto2 = new PublisherDTO_US028("Different name", vatNumber);

        // Assert
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_ShouldDifferWhenVatNumberDiffer() {
        // Act
        PublisherDTO_US028 dto1 = new PublisherDTO_US028(name, vatNumber);
        PublisherDTO_US028 dto2 = new PublisherDTO_US028(name, "92824892");

        // Assert
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }


    // Tests to toDTO ==================================================================================================

    @Test
    void toDTO_ShouldConvertPublisherCorrectly() {
        // Arrange
        when(mockPublisher.getName()).thenReturn(name);
        when(mockPublisher.getVatNumber()).thenReturn(vatNumber);

        // Act
        PublisherDTO_US028 dto = PublisherDTO_US028.toDTO(mockPublisher);

        // Assert
        assertEquals(name, dto.getName());
        assertEquals(vatNumber, dto.getVatNumber());
    }

    @Test
    void toDTO_ShouldThrowExceptionWhenPublisherIsNull() {
        // Assert
        assertThrows(NullPointerException.class, () -> PublisherDTO_US028.toDTO(null));
    }

    // Tests to toDTOs =================================================================================================

    @Test
    void toDTOs_ShouldConvertListCorrectly() {
        // Arrange
        List<Publisher> publishers = List.of(mockPublisher, mockPublisher);

        // Act
        List<PublisherDTO_US028> dtos = PublisherDTO_US028.toDTOs(publishers);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals(name, dtos.get(0).getName());
        assertEquals(vatNumber, dtos.get(0).getVatNumber());
    }

    @Test
    void toDTOs_ShouldReturnImmutableList() {
        // Arrange
        List<Publisher> publishers = List.of(mockPublisher);

        // Act
        List<PublisherDTO_US028> dtos = PublisherDTO_US028.toDTOs(publishers);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> dtos.add(
                new PublisherDTO_US028("X", "Y")
        ));
    }

    @Test
    void toDTOs_ShouldThrowExceptionWhenListIsNull() {
        // Assert
        assertThrows(NullPointerException.class, () -> PublisherDTO_US028.toDTOs(null));
    }

    @Test
    void toDTOs_ShouldThrowExceptionWhenListContainsNullElement() {
        // Arrange
        List<Publisher> publishers = new ArrayList<>();
        publishers.add(mockPublisher);
        publishers.add(null);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> PublisherDTO_US028.toDTOs(publishers));
    }
}