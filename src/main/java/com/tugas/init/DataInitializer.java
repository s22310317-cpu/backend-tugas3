package com.tugas.init;

import com.tugas.entity.Role;
import com.tugas.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if roles already exist
        if (roleRepository.findByName("USER").isEmpty()) {
            log.info("Creating default roles...");
            roleRepository.save(new Role("USER"));
            roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("MODERATOR"));
            log.info("Default roles created successfully");
        } else {
            log.info("Roles already exist");
        }
    }
}
