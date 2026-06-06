package com.UCLL.TODO.service;

import com.UCLL.TODO.controller.dto.UserRegistration;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.exception.EmailAddressNotUniqueException;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.model.User;
import com.UCLL.TODO.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAllUsers();
        return users.stream().map(user -> mapToUserResponse(user)).toList();
    }

    @Override
    public UserResponse getUserById(long id) {
        return mapToUserResponse(this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public UserResponse getUserByEmail(String userEmail) throws UserNotFoundException {
        return mapToUserResponse(this.userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail)));
    }

    @Override
    public UserResponse createUser(UserRegistration user) throws EmailAddressNotUniqueException {
        User user1 =  new User(
                user.firstName(),
                user.lastName(),
                user.email(),
                encoder.encode(user.password()));
        try {
            return mapToUserResponse(this.userRepository.saveUser(user1));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAddressNotUniqueException(user.email());
        }
    }

    @Override
    public UserResponse updateUserById(long id, UserRegistration userUpdate) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setFirstName(userUpdate.firstName());
        user.setLastName(userUpdate.lastName());
        user.setEmail(userUpdate.email());
        user.setHashedPassword(encoder.encode(userUpdate.password()));

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
