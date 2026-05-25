package TOPSECRET.controller;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;


/**
 * Authorization policy responsible for validating permissions related to
 * AppraisalEntity operations.
 *
 * <p>This class defines the authorization rules used to determine whether
 * a user is allowed to perform specific actions over AppraisalEntity objects.</p>
 */
@Component
public class AppraisalEntityAuthorizationPolicy {

    /**
     * Verifies whether a user has permission to register an AppraisalEntity.
     *
     * <p>Only users with the {@link UserRole#ADMIN} role are allowed to
     * register an AppraisalEntity.</p>
     *
     * @param user the user whose permissions are being validated
     * @return {@code true} if the user has the ADMIN role;
     *         {@code false} otherwise
     */
    public boolean canRegisterAppraisalEntity(User user){

        return user.getUserRoles().contains(UserRole.ADMIN);
    }
}
