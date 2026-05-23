package TOPSECRET.dto.appraisalEntity;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Email;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppraisalEntityMapperTest {

    @Test
    void ToEmailShouldCreateEmailUsernameWhenEmailIsValid() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Email result = AppraisalEntityMapper.toEmail(dto);

        // ASSERT
        assertEquals("john", result.getUserName());
    }

    @Test
    void ToEmailShouldCreateEmailDomainWhenEmailIsValid() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Email result = AppraisalEntityMapper.toEmail(dto);

        // ASSERT
        assertEquals("test.com", result.getDomain());
    }

    @Test
    void ToContactShouldCreatePhoneNumber() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertEquals("912345678", result.getPhoneNum());
    }

    @Test
    void ToContactShouldCreateExtension() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertEquals("123", result.getExtension());
    }

    @Test
    void ToContactShouldCreateEmail() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertNotNull(result.getEmail());
    }

    @Test
    void ToContactShouldCreateAddress() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertNotNull(result.getAddress());
    }

    @Test
    void ConstructorShouldThrowException() {

        // ARRANGE

        // ACT + ASSERT
        assertThrows(
                UnsupportedOperationException.class,
                AppraisalEntityMapper::new
        );
    }

    @Test
    void ToContactShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toContact(dto)
        );
    }

    @Test
    void ToEmailShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toEmail(dto)
        );
    }

    @Test
    void ToAddressShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toAddress(dto)
        );
    }

    @Test
    void ToPubTypeIdsShouldConvertListSizeCorrectly() {

        // ARRANGE
        List<String> input = List.of("A", "B", "C");

        // ACT
        List<PublicationTypeId> result = AppraisalEntityMapper.toPubTypeIds(input);

        // ASSERT
        assertEquals(3, result.size());
    }

    @Test
    void ToPubTypeIdsShouldConvertFirstElementCorrectly() {

        // ARRANGE
        List<String> input = List.of("A");

        // ACT
        List<PublicationTypeId> result = AppraisalEntityMapper.toPubTypeIds(input);

        // ASSERT
        assertEquals("A", result.get(0).toString());
    }

    @Test
    void ToPubTypeIdsShouldReturnEmptyList() {

        // ARRANGE
        List<String> input = List.of();

        // ACT
        List<PublicationTypeId> result = AppraisalEntityMapper.toPubTypeIds(input);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void ToPubTypeIdsShouldThrowExceptionWhenListIsNull() {

        // ARRANGE
        List<String> input = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toPubTypeIds(input)
        );
    }

    @Test
    void ToGenreIdsShouldConvertListSizeCorrectly() {

        // ARRANGE
        List<String> input = List.of("Drama", "Action");

        // ACT
        List<GenreId> result = AppraisalEntityMapper.toGenreIds(input);

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void ToGenreIdsShouldConvertFirstElementCorrectly() {

        // ARRANGE
        List<String> input = List.of("Drama");

        // ACT
        List<GenreId> result = AppraisalEntityMapper.toGenreIds(input);

        // ASSERT
        assertEquals("Drama", result.get(0).toString());
    }

    @Test
    void ToGenreIdsShouldReturnEmptyList() {

        // ARRANGE
        List<String> input = List.of();

        // ACT
        List<GenreId> result = AppraisalEntityMapper.toGenreIds(input);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void ToGenreIdsShouldThrowExceptionWhenListIsNull() {

        // ARRANGE
        List<String> input = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toGenreIds(input)
        );
    }
}