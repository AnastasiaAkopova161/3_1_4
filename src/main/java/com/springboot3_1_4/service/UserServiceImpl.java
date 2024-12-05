package com.springboot3_1_4.service;


import com.springboot3_1_4.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    // RestTemplate
    private final RestTemplate restTemplate;
    // URL of the API
    private String apiUrl;
    // контейнер для HTTP-заголовков
    private final HttpHeaders headers;
    // Session ID
    private String sessionId;
    // Constructor
    @Autowired
    public UserServiceImpl(RestTemplateService restTemplateService, @Value("${api.url}") String apiUrl) {
        this.restTemplate = restTemplateService.getRestTemplate();
        this.apiUrl = apiUrl;
        this.headers = createHttpHeaders();
    }

    private HttpHeaders createHttpHeaders() {
        return new HttpHeaders();
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    // Get Users - получение списка пользователей
    @Override
    public ResponseEntity<String> getUsers() {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        HttpMethod requestMethod = HttpMethod.GET;
        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "/users", requestMethod, entity, String.class);
        sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        System.out.println("Get Users Response: " + response.getBody());
        System.out.println("Session ID: " + sessionId);

        return response;
    }

    // Save User - сохранение пользователя
    @Override
    public ResponseEntity<String> saveUser(User user) {
        User newUser = new User(3L, "James", "Brown", (byte) 30);
        headers.set(HttpHeaders.COOKIE, sessionId);
        headers.set("Content-Type", "application/json");
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        return restTemplate.exchange(apiUrl + "/users", HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<String> updateUser(Long userId, User updatedUser) {
        // Изменение пользователя
        headers.set(HttpHeaders.COOKIE, sessionId);
        headers.set("Content-Type", "application/json");
        HttpEntity<User> entity = new HttpEntity<>(updatedUser, headers);

        return restTemplate.exchange(apiUrl + "/users", HttpMethod.PUT, entity, String.class);
    }

    @Override
    public ResponseEntity<String> deleteUser(Long userId) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "/users", HttpMethod.GET, entity, String.class);

        // Удаление пользователя
        headers.set(HttpHeaders.COOKIE, sessionId);
        response = restTemplate.exchange(apiUrl + "/users/" + userId, HttpMethod.DELETE, entity, String.class);

        return response;
    }
}


