package com.openclassrooms.starterjwt.UserRepository;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void testExistsByEmail() {
        // Créez un utilisateur et enregistrez-le dans la base de données
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(false);
        userRepository.save(user);

        // Vérifiez si l'utilisateur existe par e-mail
        boolean existsByEmail = userRepository.existsByEmail("test@example.com");

        // Vérifiez que l'utilisateur existe
        assertTrue(existsByEmail);
    }
}
