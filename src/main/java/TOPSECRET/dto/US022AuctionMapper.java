package TOPSECRET.dto;

import TOPSECRET.domain.Auction.Auction;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting {@link Auction} domain objects
 * into {@link US022AuctionDTO} instances.
 * <p>
 * This class provides utility methods to transform single Auction objects
 * as well as lists of Auction objects into DTOs used by the application layer.
 */
public class US022AuctionMapper {

    /**
     * Converts a single {@link Auction} into a {@link US022AuctionDTO}.
     *
     * @param auction the auction to convert
     * @return a DTO representing the auction
     * @throws IllegalArgumentException if auction or its required fields are null
     */
    public static US022AuctionDTO toDTO(Auction auction) {

        if (auction == null) {
            throw new IllegalArgumentException("auction cannot be null");
        }

        return new US022AuctionDTO(
                auction.id().getId(),
                auction.getListingId().getId()
        );
    }

    /**
     * Converts a list of {@link Auction} objects into a list of {@link US022AuctionDTO}.
     *
     * @param auctions the list of auctions to convert
     * @return list of DTOs
     * @throws IllegalArgumentException if the list is null or contains null elements
     */
    public static List<US022AuctionDTO> toDTOList(List<Auction> auctions) {

        if (auctions == null) {
            throw new IllegalArgumentException("auctions list cannot be null");
        }

        return auctions.stream()
                .map(US022AuctionMapper::toDTO)
                .toList();
    }
}

