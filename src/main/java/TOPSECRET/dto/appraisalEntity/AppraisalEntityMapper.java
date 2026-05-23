package TOPSECRET.dto.appraisalEntity;


import TOPSECRET.domain.ValueObjects.Address;
import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Email;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;

/**
 * Utility mapper responsible for converting AppraisalEntity request DTOs
 * into domain Value Objects used during AppraisalEntity creation.
 *
 * <p>
 * This class provides helper methods to construct domain objects such as
 * {@link Contact} and {@link Email} from request data.
 * </p>
 *
 * <p>
 * This class cannot be instantiated.
 * </p>
 */
public class AppraisalEntityMapper {

    /**
     * Prevents instantiation of this utility class.
     *
     * @throws UnsupportedOperationException always thrown because this is
     *                                       a utility class
     */
    protected AppraisalEntityMapper(){
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a {@link Contact} Value Object from the given request DTO.
     *
     * <p>
     * This method also creates the associated {@link Email},
     * {@link CityId}, and {@link Address} objects required for the
     * Contact construction.
     * </p>
     *
     * @param dto the request DTO containing contact information
     * @return the constructed Contact object
     * @throws IllegalArgumentException if the provided data is invalid
     */
    public static Contact toContact(RequestAppraisalEntityDTO dto) {

        Email email = toEmail(dto);
        CityId cityId = new CityId(dto.cityId());
        Address address = new Address(dto.street(), dto.number(), dto.zipCode(), cityId);

        return new Contact(dto.phoneNumber(), dto.extension(), email, address);
    }

    /**
     * Creates an {@link Email} Value Object from the email value
     * contained in the request DTO.
     *
     * <p>
     * The email address is split into username and domain parts
     * using the '@' separator.
     * </p>
     *
     * @param dto the request DTO containing the email information
     * @return the constructed Email object
     * @throws IllegalArgumentException if the email format is invalid
     */
    public static Email toEmail(RequestAppraisalEntityDTO dto) {

        String[] split = dto.email().split("@");

        if(split.length != 2){
            throw new IllegalArgumentException("Invalid email");
        }

        return new Email(split[0], split[1]);
    }
}
