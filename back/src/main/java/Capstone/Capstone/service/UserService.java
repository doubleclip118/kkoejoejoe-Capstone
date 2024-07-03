package Capstone.Capstone.service;

import Capstone.Capstone.domain.User;
import Capstone.Capstone.dto.UserDTO;
import Capstone.Capstone.repository.UserRepository;
import Capstone.Capstone.utils.error.UserRegistrationException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO RegisterUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());

        if (existingUser.isPresent()) {
            throw new UserRegistrationException("Username already exists");
        }
        User savedUser = userRepository.save(userDTO.UserconvertToEntity(userDTO));
        return savedUser.UserconvertToDTO(savedUser);

    }

    public UserDTO UserLogin(UserDTo)


}
