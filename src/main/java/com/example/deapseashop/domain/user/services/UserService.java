package com.example.deapseashop.domain.user.services;

import com.example.deapseashop.domain.item.ItemEntity;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

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
        // 트랜잭션 내부이므로 findUser 객체는 1차캐시에 담겼을 것.
        // 실제로 이 지점에서 userRepository.findById(findUser.getId()) 수행해도 추가 쿼리 없음.
        // findUser 엔티티가 1차캐시에 있다는 것을 의미함
//        userRepository.findById(findUser.getId());

        // 상품들 제거하는 쿼리가 몇 번 나가는지 보기
        // 아마 findUser 에서 id 값을 얻어서
        // delete from items where item.user_id = :user_id 이런 식으로 한 번만 나가지 않을까
        // 지워야 할 아이템이 여러 건이면 in 절로 하면 좋지 않을까
        // 근데 그보다 가장 좋은 건 user_id 를 가지고 delete 에서 user_id 를 조건으로 해서 한 번에 없애는 게 베스트일듯. 쿼리 한 번이니까
        // select ~ from items where i.user_id=? 으로 지워야 할 item id 값들을 얻고나서
        // 그리고 하나씩 delete from items where id=?
        // 즉, item 을 3개 지운다면 select + 3 delete => 4 query
        log.info("deleteBySeller() start");
        itemRepository.deleteBySeller(findUser);
        log.info("deleteBySeller() end");

        // 1차캐시에 있는 엔티티를 파라미터로 넣은 덕분인지 여기서는 user 찾는 쿼리가 안 나갔다.
        userRepository.delete(findUser);
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
