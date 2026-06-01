package com.UCLL.TODO.service;

import com.UCLL.TODO.controller.dto.UserRegistration;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.model.User;
import com.UCLL.TODO.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse getUserByEmail(String userEmail) throws UserNotFoundException {
        return mapToUserResponse(this.userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail)));
    }

    @Override
    public UserResponse createUser(UserRegistration user) {
        User user1 =  new User(
                user.firstName(),
                user.lastName(),
                user.email(),
                user.password());
        return mapToUserResponse(this.userRepository.saveUser(user1));
    }

    @Override
    public UserResponse updateUserById(long id, UserRegistration userUpdate) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setFirstName(userUpdate.firstName());
        user.setLastName(userUpdate.lastName());
        user.setEmail(userUpdate.email());
        user.setPassword(userUpdate.password());

        return mapToUserResponse(userRepository.saveUser(user));
    }

    @Override
    public void deleteUserById(long id) throws UserNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteUserById(id);
    }

    protected UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    @Override
    public User mapToUser(UserResponse userResponse) {
        User user = new User();
        user.setUserId(userResponse.userId());
        return user;
    }
}
