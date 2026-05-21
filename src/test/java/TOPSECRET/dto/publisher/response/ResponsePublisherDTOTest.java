package TOPSECRET.dto.publisher.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.RepresentationModel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ResponsePublisherDTO}.
 */
class ResponseResponsePublisherDTOTest {

    private String name;
    private String publisherId;

    @BeforeEach
    void setUp() {
        name = "Springer";
        publisherId = "PUB-1";
    }

    @Test
    void constructor_ShouldStoreValuesCorrectly() {
        ResponsePublisherDTO dto = new ResponsePublisherDTO(name, publisherId);

        assertEquals(name, dto.getName());
        assertEquals(publisherId, dto.getPublisherId());
    }

    @Test
    void shouldBeInstanceOfRepresentationModel() {
        ResponsePublisherDTO dto = new ResponsePublisherDTO(name, publisherId);

        assertInstanceOf(RepresentationModel.class, dto);
    }

    @Test
    void shouldBeEqual_whenDTOsHaveSameValues() {
        ResponsePublisherDTO dto1 = new ResponsePublisherDTO(name, publisherId);
        ResponsePublisherDTO dto2 = new ResponsePublisherDTO(name, publisherId);

        assertEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqual_whenDTOsHaveDifferentValues() {
        ResponsePublisherDTO dto1 = new ResponsePublisherDTO(name, publisherId);
        ResponsePublisherDTO dto2 = new ResponsePublisherDTO("Anabela", "otherId");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldHaveSameHashCode_whenDTOsAreEqual() {
        ResponsePublisherDTO dto1 = new ResponsePublisherDTO(name, publisherId);
        ResponsePublisherDTO dto2 = new ResponsePublisherDTO(name, publisherId);

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldReturnMeaningfulToString() {
        ResponsePublisherDTO dto = new ResponsePublisherDTO(name, publisherId);

        String result = dto.toString();

        assertFalse(result.isBlank());
        assertTrue(result.contains(name));
        assertTrue(result.contains(publisherId));
    }
}