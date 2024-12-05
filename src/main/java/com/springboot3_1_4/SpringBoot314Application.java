package com.springboot3_1_4;

import com.springboot3_1_4.model.User;

import com.springboot3_1_4.service.UserService;
import com.springboot3_1_4.util.JsonFormatter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;

import java.util.Objects;


@SpringBootApplication
public class SpringBoot314Application {

    private final UserService userService;

    public SpringBoot314Application(UserService userService) {
        this.userService = userService;
    }

    private String sessionId; // идентификатор сессии
    private String code; // код, который нужно "собрать" из ответов API

    // заголовки и тело ответа для каждого метода
    private ResponseEntity<String> users;
    private ResponseEntity<String> savedUser;
    private ResponseEntity<String> updatedUser;
    private ResponseEntity<String> deletedUser;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringBoot314Application.class, args);
        SpringBoot314Application application = context.getBean(SpringBoot314Application.class);
        application.run();
    }

    // всё запускаем в отдельном методе
    public void run() {
        System.out.println("----------------------------------------------------------------------------------------");
        // 1. Получить список всех пользователей
        // 2. Когда вы получите ответ на свой первый запрос, вы должны сохранить свой session id, который получен через cookie.
        // Вы получите его в заголовке ответа set-cookie. Поскольку все действия происходят в рамках одной сессии, все дальнейшие запросы должны использовать полученный session id ( необходимо использовать заголовок в последующих запросах )
        users = userService.getUsers();
        String formattedUsers = JsonFormatter.formatJson(users.getBody());
        System.out.println("Users: " + formattedUsers);
        sessionId = userService.getSessionId(); // получили идентификатор сессии
        System.out.println();

        // 3. Сохранить пользователя с id = 3, name = James, lastName = Brown, age = на ваш выбор.
        // В случае успеха вы получите первую часть кода.
        User user = new User(3L, "James", "Brown", (byte) 30);
        savedUser = userService.saveUser(user);
        System.out.println("Session ID: " + sessionId);
        System.out.println("Saved User: " + savedUser.getBody());
        code = Objects.requireNonNull(savedUser.getBody()).substring(savedUser.getBody().length() - 6);
        System.out.println();

        // 4. Изменить пользователя с id = 3. Необходимо поменять name на Thomas, а lastName на Shelby.
        // В случае успеха вы получите еще одну часть кода.
        User userUpdatedData = new User(3L, "Thomas", "Shelby", (byte) 30);
        updatedUser = userService.updateUser(3L, userUpdatedData);
        System.out.println("Session ID: " + sessionId);
        System.out.println("Updated User: " + updatedUser.getBody());
        code += Objects.requireNonNull(updatedUser.getBody()).substring(updatedUser.getBody().length() - 6);
        System.out.println();

        // 5. Удалить пользователя
        deletedUser = userService.deleteUser(3L);
        System.out.println("Session ID: " + sessionId);
        System.out.println("Deleted User: " + deletedUser.getBody());
        code += Objects.requireNonNull(deletedUser.getBody()).substring(deletedUser.getBody().length() - 6);
        System.out.println();

        // В результате выполненных операций вы должны получить итоговый код,
        // сконкатенировав все его части. Количество символов в коде = 18
        System.out.println("Final Code: " + code);
    }
}
