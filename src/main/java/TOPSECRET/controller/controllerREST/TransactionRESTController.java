package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.TransactionLinkProvider;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.transaction.TransactionMapper;
import TOPSECRET.dto.transaction.request.TransactionRequestDTO;
import TOPSECRET.dto.transaction.response.TransactionResponseDTO;
import TOPSECRET.services.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for transaction-related endpoints.
 *
 * <p>Exposes operations to create a transaction and retrieve transactions
 * by identifier or by buyer. All responses include HATEOAS links provided
 * by {@link TransactionLinkProvider}.</p>
 */
@RestController
@Tag(name = "Transactions", description = "Transaction management endpoints")
@RequestMapping(path = "/transactions")
public class TransactionRESTController {

    private final TransactionService transactionService;
    private final TransactionLinkProvider transactionLinkProvider;

    /**
     * Constructs a new {@code TransactionRESTController}.
     *
     * @param transactionService      the service used to manage transactions; must not be {@code null}
     * @param transactionLinkProvider the provider used to generate transaction HATEOAS links;
     *                                must not be {@code null}
     */
    public TransactionRESTController(TransactionService transactionService,
                                     TransactionLinkProvider transactionLinkProvider) {
        this.transactionService      = transactionService;
        this.transactionLinkProvider = transactionLinkProvider;
    }

    /**
     * Creates a new transaction.
     *
     * <p>Receives the buyer identifier, shipping address, payment details,
     * and list of purchased listing identifiers. Persists the transaction
     * and returns the created resource with HATEOAS links.</p>
     *
     * @param dto the request body containing all transaction information
     * @return {@code 201 CREATED} with the created transaction DTO,
     *         {@code 400 BAD REQUEST} if the request contains invalid data,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @PostMapping("")
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO dto) {

        try {
            UserId buyerId           = TransactionMapper.toUserId(dto.getBuyerId());
            Price price              = TransactionMapper.toPrice(dto.getPrice());
            LocalDateTime date       = TransactionMapper.toTransactionDate(dto.getTransactionDate());
            Address shippingAddress  = TransactionMapper.toAddress(dto.getStreet(), dto.getNumber(), dto.getZipCode(), dto.getCityId());
            PaymentDetails payment   = TransactionMapper.toPaymentDetails(dto.getCardHolderName(), dto.getMaskedCardNumber(), dto.getExpiryMonth(), dto.getExpiryYear());
            List<ListingId> listings = TransactionMapper.toListingIds(dto.getListingIds());

            Transaction transaction = transactionService.createAndSave(
                    buyerId, price, date, shippingAddress, payment, listings);

            TransactionResponseDTO responseDTO = buildResponseDTO(transaction);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a transaction by its identifier.
     *
     * @param id the unique identifier of the transaction
     * @return {@code 200 OK} with the transaction DTO and HATEOAS links,
     *         {@code 404 NOT FOUND} if no transaction exists with the given identifier,
     *         {@code 400 BAD REQUEST} if the identifier is invalid,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(@PathVariable String id) {

        try {
            TransactionID transactionId = TransactionMapper.toTransactionId(id);

            Transaction transaction = transactionService.findById(transactionId);

            TransactionResponseDTO responseDTO = buildResponseDTO(transaction);

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all transactions associated with a given buyer.
     *
     * <p>Returns the full list of transactions for the buyer, each with
     * HATEOAS links including a self link and a buyer-transactions link.</p>
     *
     * @param buyerId the identifier of the buyer whose transactions should be retrieved
     * @return {@code 200 OK} with the list of transaction DTOs,
     *         {@code 400 BAD REQUEST} if the buyer identifier is invalid,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<TransactionResponseDTO>> findAllByBuyerId(
            @PathVariable String buyerId) {

        try {
            UserId buyerIdVO = TransactionMapper.toUserId(buyerId);

            List<Transaction> transactions = transactionService.findAllByBuyerId(buyerIdVO);

            List<TransactionResponseDTO> responseDTOs = transactions.stream()
                    .map(this::buildResponseDTO)
                    .toList();

            return ResponseEntity.status(HttpStatus.OK).body(responseDTOs);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Converts a {@link Transaction} into a {@link TransactionResponseDTO} and
     * adds the appropriate HATEOAS links.
     *
     * @param transaction the transaction to convert; must not be {@code null}
     * @return the response DTO with HATEOAS links attached
     */
    private TransactionResponseDTO buildResponseDTO(Transaction transaction) {

        TransactionResponseDTO responseDTO = TransactionMapper.toResponseDTO(transaction);

        List<Link> links = new ArrayList<>(transactionLinkProvider.getPublicLinks());
        links.addAll(transactionLinkProvider.getBuyerLinks(transaction.getBuyerId()));

        links.add(linkTo(methodOn(TransactionRESTController.class)
                .findById(transaction.id().getId()))
                .withSelfRel());

        links.forEach(responseDTO::add);

        return responseDTO;
    }
}