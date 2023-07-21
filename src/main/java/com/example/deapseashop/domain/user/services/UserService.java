package com.example.deapseashop.domain.user.services;

import com.example.deapseashop.domain.item.ItemEntity;
import com.example.deapseashop.domain.user.dtos.UserJoinRequest;
import com.example.deapseashop.domain.user.entities.UserEntity;
import com.example.deapseashop.domain.user.enums.UserRole;
import com.example.deapseashop.exceptions.DuplicateEmailException;
import com.example.deapseashop.domain.user.repositories.UserRepository;
import com.example.deapseashop.utils.ShaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(UserJoinRequest userJoinRequest) {

        UserEntity userEntity = new UserEntity(
                userJoinRequest.getEmail(),
                ShaUtils.encryptSHA256(userJoinRequest.getPassword()),
                userJoinRequest.getUsername(),
                UserRole.USER);
        validateDuplicateUser(userEntity);
        userRepository.save(userEntity);
        return userEntity.getId();
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // TODO 회원 검증 로직 작성하기
    private void validateDuplicateUser(UserEntity userEntity) {
        Optional<UserEntity> findUser = userRepository.findByEmail(userEntity.getEmail());
        if (findUser.isPresent()) {
            throw new DuplicateEmailException("해당 이메일의 회원이 이미 존재합니다.");
        }
    }

}
