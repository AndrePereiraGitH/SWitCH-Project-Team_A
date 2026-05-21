package TOPSECRET.dto.appraisalEntity.request;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CreateAppraisalEntityRequestDTO}.
 */
class CreateAppraisalEntityRequestDTOTest {

    @Test
    void getNameShouldReturnAssignedValue() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                                            "Entity",
                                            "912345678",
                                            "123",
                                            "test@test.com",
                                            "Street",
                                            "10",
                                            "4000-000",
                                            "1",
                                            List.of("PUB1", "PUB2"),
                                            List.of("GEN1", "GEN2")
                                            );

        // ACT
        String result = dto.getName();

        // ASSERT
        assertEquals("Entity", result);
    }

    @Test
    void getPhoneNumberShouldReturnAssignedValue() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                "Entity",
                "912345678",
                "123",
                "test@test.com",
                "Street",
                "10",
                "4000-000",
                "1",
                List.of("PUB1", "PUB2"),
                List.of("GEN1", "GEN2")
        );

        // ACT
        String result = dto.getPhoneNumber();

        // ASSERT
        assertEquals("912345678", result);
    }

    @Test
    void getExtensionShouldReturnAssignedValue() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                                                    "Entity",
                                                    "912345678",
                                                    "123",
                                                    "test@test.com",
                                                    "Street",
                                                    "10",
                                                    "4000-000",
                                                    "1",
                                                    List.of("PUB1", "PUB2"),
                                                    List.of("GEN1", "GEN2")
                                            );
        // ACT
        String result = dto.getExtension();

        // ASSERT
        assertEquals("123", result);
    }

    @Test
    void getEmailShouldReturnAssignedValue() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                                                        "Entity",
                                                        "912345678",
                                                        "123",
                                                        "test@test.com",
                                                        "Street",
                                                        "10",
                                                        "4000-000",
                                                        "1",
                                                        List.of("PUB1", "PUB2"),
                                                        List.of("GEN1", "GEN2")
                                                );

        // ACT
        String result = dto.getEmail();

        // ASSERT
        assertEquals("test@test.com", result);
    }

    @Test
    void getStreetShouldReturnAssignedValue() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                                                        "Entity",
                                                        "912345678",
                                                        "123",
                                                        "test@test.com",
                                                        "Street",
                                                        "10",
                                                        "4000-000",
                                                        "1",
                                                        List.of("PUB1", "PUB2"),
                                                        List.of("GEN1", "GEN2")
                                                );
        // ACT
        String result = dto.getStreet();

        // ASSERT
        assertEquals("Street", result);
    }

    @Test
    void getNumberShouldReturnAssignedValue() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                                                        "Entity",
                                                        "912345678",
                                                        "123",
                                                        "test@test.com",
                                                        "Street",
                                                        "10",
                                                        "4000-000",
                                                        "1",
                                                        List.of("PUB1", "PUB2"),
                                                        List.of("GEN1", "GEN2")
                                                );

        // ACT
        String result = dto.getNumber();

        // ASSERT
        assertEquals("10", result);
    }

    @Test
    void getZipCodeShouldReturnAssignedValue() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                                                        "Entity",
                                                        "912345678",
                                                        "123",
                                                        "test@test.com",
                                                        "Street",
                                                        "10",
                                                        "4000-000",
                                                        "1",
                                                        List.of("PUB1", "PUB2"),
                                                        List.of("GEN1", "GEN2")
                                                );

        // ACT
        String result = dto.getZipCode();

        // ASSERT
        assertEquals("4000-000", result);
    }

    @Test
    void getCityIdShouldReturnAssignedValue() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                                                        "Entity",
                                                        "912345678",
                                                        "123",
                                                        "test@test.com",
                                                        "Street",
                                                        "10",
                                                        "4000-000",
                                                        "1",
                                                        List.of("PUB1", "PUB2"),
                                                        List.of("GEN1", "GEN2")
                                                );
        // ACT
        String result = dto.getCityId();

        // ASSERT
        assertEquals("1", result);
    }

    @Test
    void getPublicationTypeIdsShouldReturnAssignedValue() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                                                    "Entity",
                                                    "912345678",
                                                    "123",
                                                    "test@test.com",
                                                    "Street",
                                                    "10",
                                                    "4000-000",
                                                    "1",
                                                    List.of("PUB1", "PUB2"),
                                                    List.of("GEN1", "GEN2")
                                            );
        // ACT
        List<String> result = dto.getPublicationTypeIds();

        // ASSERT
        assertEquals(
                List.of("PUB1", "PUB2"),
                result
        );
    }

    @Test
    void getGenreIdsShouldReturnAssignedValue() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                                                        "Entity",
                                                        "912345678",
                                                        "123",
                                                        "test@test.com",
                                                        "Street",
                                                        "10",
                                                        "4000-000",
                                                        "1",
                                                        List.of("PUB1", "PUB2"),
                                                        List.of("GEN1", "GEN2")
                                                );
        // ACT
        List<String> result = dto.getGenreIds();

        // ASSERT
        assertEquals(
                List.of("GEN1", "GEN2"),
                result
        );
    }

    @Test
    void equalsShouldReturnTrueForSameValues() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto1 = new CreateAppraisalEntityRequestDTO(
                                                        "Entity",
                                                        "912345678",
                                                        "123",
                                                        "test@test.com",
                                                        "Street",
                                                        "10",
                                                        "4000-000",
                                                        "1",
                                                        List.of("PUB1", "PUB2"),
                                                        List.of("GEN1", "GEN2")
                                                );

        CreateAppraisalEntityRequestDTO dto2 = new CreateAppraisalEntityRequestDTO(
                                                    "Entity",
                                                    "912345678",
                                                    "123",
                                                    "test@test.com",
                                                    "Street",
                                                    "10",
                                                    "4000-000",
                                                    "1",
                                                    List.of("PUB1", "PUB2"),
                                                    List.of("GEN1", "GEN2")
                                            );
        // ACT
        boolean result = dto1.equals(dto2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentName() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto1 = new CreateAppraisalEntityRequestDTO(
                                                        "Entity",
                                                        "912345678",
                                                        "123",
                                                        "test@test.com",
                                                        "Street",
                                                        "10",
                                                        "4000-000",
                                                        "1",
                                                        List.of("PUB1", "PUB2"),
                                                        List.of("GEN1", "GEN2")
                                                );

        CreateAppraisalEntityRequestDTO dto2 =
                new CreateAppraisalEntityRequestDTO(
                        "Different",
                        "912345678",
                        "123",
                        "test@test.com",
                        "Street",
                        "10",
                        "4000-000",
                        "1",
                        List.of("PUB1"),
                        List.of("GEN1")
                );

        // ACT
        boolean result = dto1.equals(dto2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void hashCodeShouldBeEqualForEqualObjects() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto1 = new CreateAppraisalEntityRequestDTO(
                "Entity",
                "912345678",
                "123",
                "test@test.com",
                "Street",
                "10",
                "4000-000",
                "1",
                List.of("PUB1", "PUB2"),
                List.of("GEN1", "GEN2")
        );

        CreateAppraisalEntityRequestDTO dto2 = new CreateAppraisalEntityRequestDTO(
                "Entity",
                "912345678",
                "123",
                "test@test.com",
                "Street",
                "10",
                "4000-000",
                "1",
                List.of("PUB1", "PUB2"),
                List.of("GEN1", "GEN2")
        );

        // ACT
        int hash1 = dto1.hashCode();
        int hash2 = dto2.hashCode();

        // ASSERT
        assertEquals(hash1, hash2);
    }

    @Test
    void toStringShouldContainName() {

        // ARRANGE
        CreateAppraisalEntityRequestDTO dto = new CreateAppraisalEntityRequestDTO(
                "Entity",
                "912345678",
                "123",
                "test@test.com",
                "Street",
                "10",
                "4000-000",
                "1",
                List.of("PUB1", "PUB2"),
                List.of("GEN1", "GEN2")
        );

        // ACT
        String result = dto.toString();

        // ASSERT
        assertTrue(result.contains("Entity"));
    }
}