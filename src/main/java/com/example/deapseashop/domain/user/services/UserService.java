package com.example.deapseashop.domain.user.services;

import com.example.deapseashop.domain.item.ItemRepository;
import com.example.deapseashop.domain.user.dtos.UserJoinRequest;
import com.example.deapseashop.domain.user.entities.UserEntity;
import com.example.deapseashop.domain.user.enums.UserRole;
import com.example.deapseashop.exceptions.DuplicateEmailAndUsernameException;
import com.example.deapseashop.exceptions.DuplicateEmailException;
import com.example.deapseashop.domain.user.repositories.UserRepository;
import com.example.deapseashop.exceptions.DuplicateUsernameException;
import com.example.deapseashop.exceptions.UserNotFoundException;
import com.example.deapseashop.utils.ShaUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final EntityManager em;

    @Transactional
    public Long join(UserJoinRequest userJoinRequest) {

        UserEntity userEntity = new UserEntity(
                userJoinRequest.getEmail(),
                ShaUtils.encryptSHA256(userJoinRequest.getPassword()),
                userJoinRequest.getUsername(),
                UserRole.USER);

        // TODO 이메일 검증, 비밀번호 검증 로직 작성하기
        // 이메일 및 이름 중복 검사 후 save()
        validateDuplicateUser(userEntity);
        userRepository.save(userEntity);
        return userEntity.getId();
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("회원을 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteUser(String email) {
        log.info("deleteUser() start");
        UserEntity findUser = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("회원을 찾을 수 없습니다."));

        itemRepository.deleteAllInBatch(findUser.getSellingItems());
//        myBatisItemRepository.deleteBySeller(findUser.getId());
//        log.info("findUser.getSellingItems().size()={}",findUser.getSellingItems().size());
//        itemRepository.deleteAllBySeller(findUser);
//        itemRepository.deleteBySeller(findUser);
//        findUser.getSellingItems().clear();
//        em.flush();

        // findUser 가 1차캐시에 있어서 user 찾는 쿼리가 안 나간다.
//        userRepository.delete(findUser);
        userRepository.deleteAllInBatch(List.of(findUser));
    }


    private void validateDuplicateUser(UserEntity userEntity) {
        List<UserEntity> userList = userRepository.findByEmailOrUsername(userEntity.getEmail(), userEntity.getUsername());

        List<String> nameList = userList.stream()
                .map(UserEntity::getEmail)
                .toList();
        boolean isEmailDuplicated = nameList.contains(userEntity.getEmail());
        List<String> usernameList = userList.stream()
                .map(user -> user.getUsername())
                .toList();
        boolean isUsernameDuplicated = usernameList.contains(userEntity.getUsername());

        if (isEmailDuplicated && isUsernameDuplicated) {
            throw new DuplicateEmailAndUsernameException("이메일과 이름 모두 이미 사용중입니다.");
        } else if (isEmailDuplicated) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다.");
        } else if (isUsernameDuplicated) {
            throw new DuplicateUsernameException("이미 사용중인 이름입니다.");
        }
    }



}
