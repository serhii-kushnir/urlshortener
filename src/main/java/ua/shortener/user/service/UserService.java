package ua.shortener.user.service;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import ua.shortener.link.Link;
import ua.shortener.user.User;
import ua.shortener.user.dto.EditUserDTO;
import ua.shortener.user.dto.UserDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public final class UserService implements  UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Stream.of(user.getRole())
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .toList());
    }

    public Optional<User> findUserByEmail(final String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<Link> getLinksByUserId(final Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(User::getLinks).orElse(Collections.emptyList());
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(final long id) {
        return userRepository.findById(id);
    }

    public User editUser(final User user, final EditUserDTO editUserDTO) {
        editUserDTO.editUser(user);
        return userRepository.save(user);
    }

    public void deleteUser(final long id) {
        userRepository.deleteById(id);
    }
    

    public UserDTO getUserInfoById(final long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAt());

        return userDTO;
    }
}