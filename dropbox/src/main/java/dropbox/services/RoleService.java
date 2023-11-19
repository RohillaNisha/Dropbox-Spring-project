package dropbox.services;


import dropbox.models.ERole;
import dropbox.models.Role;
import dropbox.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


    @PostConstruct
    public void initRoles() {
        addRoleIfNotExists(ERole.ROLE_ADMIN);
        addRoleIfNotExists(ERole.ROLE_MODERATOR);
        addRoleIfNotExists(ERole.ROLE_USER);
    }

    private void addRoleIfNotExists(ERole roleName) {
        Optional<Role> roleInDb = roleRepository.findByName(roleName);
        if(roleInDb.isEmpty()){
            Role role = new Role(roleName);
            roleRepository.save(role);
        }
    }


    public Role createARole(Role Role) {
        return this.roleRepository.save(Role);

    }
}
