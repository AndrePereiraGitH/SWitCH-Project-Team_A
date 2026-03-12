package TOPSECRET.domain.Enum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuctionStatusTest {

    @Test
    void shouldContainAllExpectedStatuses() {
        AuctionStatus[] statuses = AuctionStatus.values();

        assertEquals(7, statuses.length);
    }

    @Test
    void shouldHaveExpectedEnumNames() {
        assertEquals("DRAFT", AuctionStatus.DRAFT.name());
        assertEquals("SCHEDULED", AuctionStatus.SCHEDULED.name());
        assertEquals("ACTIVE", AuctionStatus.ACTIVE.name());
        assertEquals("ENDED_WITH_WINNER", AuctionStatus.ENDED_WITH_WINNER.name());
        assertEquals("ENDED_WITHOUT_BIDS", AuctionStatus.ENDED_WITHOUT_BIDS.name());
        assertEquals("BOUGHT_OUT", AuctionStatus.BOUGHT_OUT.name());
        assertEquals("CANCELLED", AuctionStatus.CANCELLED.name());
    }

    @Test
    void shouldReturnCorrectDescriptions() {
        assertEquals("Created but not yet published/activated.", AuctionStatus.DRAFT.getDescription());
        assertEquals("Scheduled to start in the future.", AuctionStatus.SCHEDULED.getDescription());
        assertEquals("Currently running and accepting bids.", AuctionStatus.ACTIVE.getDescription());
        assertEquals("Ended with at least one bid; a winner exists.", AuctionStatus.ENDED_WITH_WINNER.getDescription());
        assertEquals("Ended with no bids placed; no winner exists.", AuctionStatus.ENDED_WITHOUT_BIDS.getDescription());
        assertEquals("Sold via buyout or finalized sale flow.", AuctionStatus.BOUGHT_OUT.getDescription());
        assertEquals("Cancelled by seller or system.", AuctionStatus.CANCELLED.getDescription());
    }


}



