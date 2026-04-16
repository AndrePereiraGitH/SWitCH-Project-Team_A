package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class US026AuctionDTOTest {

@Test
void shouldThrow_whenIdIsNULL() {
    // act + assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new US026AuctionDTO(
            null,
            "Auction Lord of the Rings",
            "ACTIVE"
    ));

    assertEquals("auctionId cannot be null", ex.getMessage());
}

@Test
void shouldThrow_whenListingTitleIsNull() {

    // act + assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new US026AuctionDTO(
            "auctionId",
            null,
            "ACTIVE"
    ));

    assertEquals("listing title cannot be null", ex.getMessage());


}

    @Test
    void shouldThrow_whenAuctionStatusIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new US026AuctionDTO(
                "auctionId",
                "Auction Lord of the Rings",
                null
        ));

        assertEquals("auction status cannot be null", ex.getMessage());
    }

    @Test
    void shouldCreateValidDto_whenAllFieldsAreValid() {
        // act
        US026AuctionDTO dto = new US026AuctionDTO(
                "auctionId",
                "Auction Lord of the Rings",
                "ACTIVE"
        );

        // assert
        assertNotNull(dto);
        assertEquals("auctionId", dto.auctionId());
        assertEquals("Auction Lord of the Rings", dto.listingTitle());
        assertEquals("ACTIVE", dto.auctionStatus());
    }

}