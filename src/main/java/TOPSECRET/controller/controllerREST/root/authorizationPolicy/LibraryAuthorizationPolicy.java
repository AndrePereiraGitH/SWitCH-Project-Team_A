package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.springframework.stereotype.Component;

@Component
public class LibraryAuthorizationPolicy {
    public boolean canCreateLibrary(User user) {
        return user.getUserRoles().contains(UserRole.NORMAL_USER);
    }
    public boolean canGetAllLibrary(User user) {
        return user.getUserRoles().contains(UserRole.ADMIN);
    }
}
