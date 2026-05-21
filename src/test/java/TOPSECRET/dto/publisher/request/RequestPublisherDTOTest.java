package TOPSECRET.dto.publisher.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestPublisherDTOTest {

    @Test
    void shouldCreateDTO() {
        new RequestPublisherDTO(
                "Publisher Name",
                "123456789",
                "912345678",
                "+351"
        );
    }

    @Test
    void shouldReturnCorrectName() {
        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher Name",
                "123456789",
                "912345678",
                "+351"
        );

        // ASSERT
        assertEquals("Publisher Name", dto.name());
    }

    @Test
    void shouldReturnCorrectVatNumber() {
        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher Name",
                "123456789",
                "912345678",
                "+351"
        );

        // ASSERT
        assertEquals("123456789", dto.vatNumber());
    }

    @Test
    void shouldReturnCorrectPhoneNumber() {
        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher Name",
                "123456789",
                "912345678",
                "+351"
        );

        // ASSERT
        assertEquals("912345678", dto.phoneNumber());
    }

    @Test
    void shouldReturnCorrectExtension() {
        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Publisher Name",
                "123456789",
                "912345678",
                "+351"
        );

        // ASSERT
        assertEquals("+351", dto.extension());
    }

    @Test
    void shouldBeEqualWhenAttributesAreEqual() {
        // ARRANGE
        RequestPublisherDTO dto1 = new RequestPublisherDTO("Publisher", "123456789", "912345678", "+351");
        RequestPublisherDTO dto2 = new RequestPublisherDTO("Publisher", "123456789", "912345678", "+351");

        // ASSERT
        assertEquals(dto1, dto2);
    }

    @Test
    void shouldHaveSameHashCodeWhenAttributesAreEqual() {
        // ARRANGE
        RequestPublisherDTO dto1 = new RequestPublisherDTO("Publisher", "123456789", "912345678", "+351");
        RequestPublisherDTO dto2 = new RequestPublisherDTO("Publisher", "123456789", "912345678", "+351");

        // ASSERT
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenAttributesAreDifferent() {
        // ARRANGE
        RequestPublisherDTO dto1 = new RequestPublisherDTO("Publisher A", "123456789", "912345678", "+351");
        RequestPublisherDTO dto2 = new RequestPublisherDTO("Publisher B", "987654321", "987654321", "+44");

        // ASSERT
        assertNotEquals(dto1, dto2);
    }
}