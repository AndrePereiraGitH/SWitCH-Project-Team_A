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
     *         {@code 403 FORBIDDEN} if the user does not have permission,
     *         {@code 404 NOT FOUND} if the buyer does not exist,
     *         {@code 400 BAD REQUEST} if the request contains invalid data,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @PostMapping("")
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO dto) {

        try {
            Transaction transaction = transactionService.createAndSave(
                    TransactionMapper.toUserId(dto.getBuyerId()),
                    TransactionMapper.toPrice(dto.getPrice()),
                    TransactionMapper.toTransactionDate(dto.getTransactionDate()),
                    TransactionMapper.toAddress(dto.getStreet(), dto.getNumber(), dto.getZipCode(), dto.getCityId()),
                    TransactionMapper.toPaymentDetails(dto.getCardHolderName(), dto.getMaskedCardNumber(), dto.getExpiryMonth(), dto.getExpiryYear()),
                    TransactionMapper.toListingIds(dto.getListingIds())
            );

            TransactionResponseDTO responseDTO = buildResponseDTO(transaction);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

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
     * <p>Only the buyer who created the transaction can retrieve it.
     * The requester identifier must be provided as a query parameter.</p>
     *
     * @param id          the unique identifier of the transaction
     * @param requesterId the identifier of the user requesting access
     * @return {@code 200 OK} with the transaction DTO and HATEOAS links,
     *         {@code 403 FORBIDDEN} if the requester is not the transaction owner,
     *         {@code 404 NOT FOUND} if no transaction exists with the given identifier,
     *         {@code 400 BAD REQUEST} if any identifier is invalid,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(
            @PathVariable String id,
            @RequestParam String requesterId) {

        try {
            TransactionID transactionId = TransactionMapper.toTransactionId(id);
            UserId requesterIdVO        = TransactionMapper.toUserId(requesterId);

            Transaction transaction = transactionService.findById(transactionId, requesterIdVO);

            TransactionResponseDTO responseDTO = buildResponseDTO(transaction);

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);

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
     * <p>Only the buyer themselves can retrieve their own transactions.
     * The requester identifier must be provided as a query parameter.</p>
     *
     * @param buyerId     the identifier of the buyer whose transactions should be retrieved
     * @param requesterId the identifier of the user requesting access
     * @return {@code 200 OK} with the list of transaction DTOs,
     *         {@code 403 FORBIDDEN} if the requester is not the buyer,
     *         {@code 400 BAD REQUEST} if any identifier is invalid,
     *         or {@code 500 INTERNAL SERVER ERROR} for unexpected errors
     */
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<TransactionResponseDTO>> findAllByBuyerId(
            @PathVariable String buyerId,
            @RequestParam String requesterId) {

        try {
            UserId buyerIdVO      = TransactionMapper.toUserId(buyerId);
            UserId requesterIdVO  = TransactionMapper.toUserId(requesterId);

            List<Transaction> transactions = transactionService.findAllByBuyerId(buyerIdVO, requesterIdVO);

            List<TransactionResponseDTO> responseDTOs = transactions.stream()
                    .map(this::buildResponseDTO)
                    .toList();

            return ResponseEntity.status(HttpStatus.OK).body(responseDTOs);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
                .findById(transaction.id().getId(), null))
                .withSelfRel());

        links.forEach(responseDTO::add);

        return responseDTO;
    }
}