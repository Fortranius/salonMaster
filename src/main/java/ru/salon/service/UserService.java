package ru.salon.service;

import org.springframework.stereotype.Service;
import ru.salon.model.User;

@Service
public class UserService {

    private static final User ADMIN = User.builder()
            .login("admin").password("admin").role("ADMIN").build();
    private static final User COORDINATOR = User.builder()
            .login("coord").password("coord").role("COORDINATOR").build();

    public boolean login(String login, String password) {
        return getUser(login, password) != null;
    }

    public User getUser(String login, String password) {
        if (ADMIN.getLogin().equals(login) && ADMIN.getPassword().equals(password)) return ADMIN;
        if (COORDINATOR.getLogin().equals(login) && COORDINATOR.getPassword().equals(password)) return COORDINATOR;
        return null;
    }
}
