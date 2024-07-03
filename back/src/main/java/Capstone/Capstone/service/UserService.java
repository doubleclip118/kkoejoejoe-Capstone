package Capstone.Capstone.service;

import Capstone.Capstone.dto.UserDTO;
import Capstone.Capstone.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO RegisterUser(UserDTO userDTO) {
        userRepository.save(userDTO)
    }


}
