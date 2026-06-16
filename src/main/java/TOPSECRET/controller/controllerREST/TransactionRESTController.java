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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for transaction-related endpoints.
 *
 * <p>Exposes operations to create a transaction and retrieve transactions
 * by identifier, by buyer, or all transactions (admin only).
 * All responses include HATEOAS links provided by {@link TransactionLinkProvider}.</p>
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
     * @param dto         the request body containing all transaction information
     * @param requesterId the identifier of the user making the request
     * @return {@code 201 CREATED} with the created transaction DTO,
     *         {@code 403 FORBIDDEN} if the user does not have permission,
     *         {@code 404 NOT FOUND} if the buyer does not exist,
     *         {@code 400 BAD REQUEST} if the request contains invalid data,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @PostMapping("")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionRequestDTO dto,
                                                                    @RequestParam String requesterId) {
        try {
            Transaction transaction = transactionService.createAndSave(
                    TransactionMapper.toUserId(requesterId),
                    TransactionMapper.toPrice(dto.getPrice()),
                    TransactionMapper.toTransactionDate(dto.getTransactionDate()),
                    TransactionMapper.toAddress(dto.getStreet(), dto.getNumber(), dto.getZipCode(), dto.getCityId()),
                    TransactionMapper.toPaymentDetails(dto.getCardHolderName(), dto.getMaskedCardNumber(), dto.getExpiryMonth(), dto.getExpiryYear()),
                    TransactionMapper.toListingIds(dto.getListingIds())
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(buildResponseDTO(transaction, requesterId));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a transaction by its identifier.
     *
     * <p>Only the buyer who created the transaction or an admin can retrieve it.
     * The requester identifier must be provided as a query parameter.</p>
     *
     * @param id          the unique identifier of the transaction
     * @param requesterId the identifier of the user requesting access
     * @return {@code 200 OK} with the transaction DTO and HATEOAS links,
     *         {@code 403 FORBIDDEN} if the requester is neither the buyer nor an admin,
     *         {@code 404 NOT FOUND} if no transaction exists with the given identifier,
     *         {@code 400 BAD REQUEST} if any identifier is invalid,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(@PathVariable String id,
                                                           @RequestParam String requesterId) {
        try {
            Transaction transaction = transactionService.findById(
                    TransactionMapper.toTransactionId(id),
                    TransactionMapper.toUserId(requesterId)
            );
            return ResponseEntity.ok(buildResponseDTO(transaction, requesterId));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all transactions in the system.
     *
     * <p>Only admins can access this endpoint.</p>
     *
     * @param requesterId the identifier of the user requesting access
     * @return {@code 200 OK} with the list of all transaction DTOs,
     *         {@code 403 FORBIDDEN} if the requester is not an admin,
     *         {@code 404 NOT FOUND} if the requester does not exist,
     *         {@code 400 BAD REQUEST} if the identifier is invalid,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @GetMapping("")
    public ResponseEntity<List<TransactionResponseDTO>> findAll(
            @RequestParam String requesterId) {
        try {
            List<Transaction> transactions = transactionService.findAll(
                    TransactionMapper.toUserId(requesterId)
            );
            return ResponseEntity.ok(transactions.stream()
                    .map(t -> buildResponseDTO(t, requesterId))
                    .toList());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
     * <p>Only the buyer themselves or an admin can retrieve this list.
     * The requester identifier must be provided as a query parameter.</p>
     *
     * @param buyerId     the identifier of the buyer whose transactions should be retrieved
     * @param requesterId the identifier of the user requesting access
     * @return {@code 200 OK} with the list of transaction DTOs,
     *         {@code 403 FORBIDDEN} if the requester is neither the buyer nor an admin,
     *         {@code 404 NOT FOUND} if the requester does not exist,
     *         {@code 400 BAD REQUEST} if any identifier is invalid,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<TransactionResponseDTO>> findAllByBuyerId(
            @PathVariable String buyerId,
            @RequestParam String requesterId) {
        try {
            List<Transaction> transactions = transactionService.findAllByBuyerId(
                    TransactionMapper.toUserId(buyerId),
                    TransactionMapper.toUserId(requesterId)
            );
            return ResponseEntity.ok(transactions.stream()
                    .map(t -> buildResponseDTO(t, requesterId))
                    .toList());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Converts a {@link Transaction} into a {@link TransactionResponseDTO} and
     * adds the appropriate HATEOAS links for the given requester.
     *
     * @param transaction the transaction to convert; must not be {@code null}
     * @param requesterId the identifier of the user requesting access; must not be {@code null}
     * @return the response DTO with HATEOAS links attached
     */
    private TransactionResponseDTO buildResponseDTO(Transaction transaction, String requesterId) {

        TransactionResponseDTO responseDTO = TransactionMapper.toResponseDTO(transaction);

        try {
            List<Link> links = new ArrayList<>(
                    transactionLinkProvider.getLinks(TransactionMapper.toUserId(requesterId))
            );
            links.add(linkTo(methodOn(TransactionRESTController.class)
                    .findById(transaction.id().getId(), requesterId))
                    .withSelfRel());
            links.forEach(responseDTO::add);
        } catch (Exception ignored) {
        }

        return responseDTO;
    }
}