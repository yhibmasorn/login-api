package lecture.ws2.login.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import lecture.ws2.login.model.User;

@Repository
public class UserRepository {
    
    private Map<String, User> users = new HashMap<>();

    public UserRepository() {
        users.put("user1", new User("user1", "password1"));
        users.put("user1", new User("user1", "password1"));
    }

    public User findByUsername(String username){
        return users.get(username);

    }

}
