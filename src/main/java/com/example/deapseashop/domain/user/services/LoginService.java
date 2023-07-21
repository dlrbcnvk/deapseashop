package com.example.deapseashop.domain.user.services;

import com.example.deapseashop.domain.user.dtos.UserDto;
import com.example.deapseashop.domain.user.dtos.UserLoginRequest;
import com.example.deapseashop.domain.user.entities.UserEntity;
import com.example.deapseashop.exceptions.InvalidEmailException;
import com.example.deapseashop.exceptions.InvalidPasswordException;
import com.example.deapseashop.domain.user.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    @Transactional
    public UserDto login(UserLoginRequest userLoginRequest) {
        userLoginRequest.encryptPwd();

        UserEntity user;
        try {
            user = userRepository.findByEmail(userLoginRequest.getEmail()).orElseThrow();
            /**
             * 단건조회 이메일로 찾을 수 없을 경우 NoSuchElementException 발생
             * 로그인할때 말고 다른 경우, 다른 엔티티의 단건조회에서도 동일하게 NoSuchElementException 발생
             * 로그인할 때 단건조회하는 경우에 한정하여 InvalidEmailException 으로 바꿔서 던져주기로 함
             */
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            throw new InvalidEmailException("존재하지 않는 이메일입니다.");
        }

        if (!user.getPassword().equals(userLoginRequest.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 틀렸습니다.");
        }


        return new UserDto(
                user.getEmail(),
                user.getUsername(),
                user.getUserRole()
        );
    }
}
