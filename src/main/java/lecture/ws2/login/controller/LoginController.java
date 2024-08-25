package lecture.ws2.login.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lecture.ws2.login.exception.InvalidCredentialsException;
import lecture.ws2.login.model.User;
import lecture.ws2.login.model.request.LoginRequest;
import lecture.ws2.login.model.response.LoginResponse;
import lecture.ws2.login.repository.UserRepository;


@RestController
@RequestMapping("/auth")
public class LoginController {
    
    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository){
        this.userRepository = userRepository;

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws Exception{
        User user = userRepository.findByUsername(loginRequest.getUsername());

        if(user == null || !user.getUsername().equals(loginRequest.getUsername()) || !user.getPassword().equals(loginRequest.getPassword())){
            throw new InvalidCredentialsException("Invalid password for username: " + loginRequest.getUsername());

        }

        LoginResponse response = new LoginResponse();
        response.setSessionId(UUID.randomUUID().toString());
        
        return response;

    }

}
