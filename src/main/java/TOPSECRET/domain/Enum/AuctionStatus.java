package TOPSECRET.domain.Enum;

public enum AuctionStatus {
    DRAFT("Created but not yet published/activated."),
    SCHEDULED("Scheduled to start in the future."),
    ACTIVE("Currently running and accepting bids."),

    ENDED_WITH_WINNER("Ended with at least one bid; a winner exists."),
    ENDED_WITHOUT_BIDS("Ended with no bids placed; no winner exists."),

    BOUGHT_OUT("Sold via buyout or finalized sale flow."),
    CANCELLED("Cancelled by seller or system.");

    private final String description;

    AuctionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
