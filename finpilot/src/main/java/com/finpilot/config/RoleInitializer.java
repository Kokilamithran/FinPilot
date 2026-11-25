package com.finpilot.config;

import com.finpilot.entity.Role;
import com.finpilot.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        createRoleIfNotExists(
                "USER",
                "Regular User"
        );

        createRoleIfNotExists(
                "ADMIN",
                "System Administrator"
        );
    }

    private void createRoleIfNotExists(String roleName,
                                       String description) {

        if (roleRepository.findByRoleName(roleName).isEmpty()) {

            Role role = new Role();

            role.setRoleName(roleName);
            role.setDescription(description);

            roleRepository.save(role);

            System.out.println(roleName + " role created.");
        }
    }
}