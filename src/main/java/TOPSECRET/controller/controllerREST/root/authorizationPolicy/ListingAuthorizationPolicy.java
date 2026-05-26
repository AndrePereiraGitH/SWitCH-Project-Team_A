package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

@Component
public class ListingAuthorizationPolicy {

    public boolean canCreateListing(User user) {
        return user.getUserRoles().equals(UserRole.NORMAL_USER);
    }
}
