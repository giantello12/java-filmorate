package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user.getFriends().contains(friendId)) {
            throw new ValidationException(
                    "Пользователи " + userId + " и " + friendId + " уже дружат");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getUserFriends(long userId) {
        User user = storage.getUserById(userId);
        return user.getFriends().stream()
                .map(storage::getUserById) // Преобразуем ID → User
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        Set<Long> commonFriendIds = new HashSet<>(user.getFriends());

        commonFriendIds.retainAll(otherUser.getFriends());
        List<User> commonFriends = new ArrayList<>();

        for (Long friendId : commonFriendIds) {
            commonFriends.add(getUserById(friendId));
        }
        return commonFriends;
    }

    private User getUserById(long id) {
        return storage.getAllUsers().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Пользователь с данным ID не найден"));
    }
}
