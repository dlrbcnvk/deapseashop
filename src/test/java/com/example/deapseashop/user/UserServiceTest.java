package com.example.deapseashop.user;

import com.example.deapseashop.domain.item.ItemRegisterDto;
import com.example.deapseashop.domain.item.ItemRepository;
import com.example.deapseashop.domain.item.ItemService;
import com.example.deapseashop.domain.user.dtos.UserJoinRequest;
import com.example.deapseashop.domain.user.entities.UserEntity;
import com.example.deapseashop.domain.user.repositories.UserRepository;
import com.example.deapseashop.domain.user.services.UserService;
import com.example.deapseashop.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Autowired
    public UserService userService;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public ItemRepository itemRepository;
    @Autowired
    public ItemService itemService;

    @AfterEach
    public void after() {
        itemRepository.deleteAll();
        itemRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        log.info("delete all items and users after each");
    }

    @BeforeEach
    public void before() {
        itemRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        log.info("delete all items and users before each");
    }
    @Test
    @Rollback
    void 회원가입() {
        // given
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);

        // when
        UserEntity findUser = userService.findByEmail(userRequest.getEmail());

        // then
        assertThat(findUser).isNotNull();
        assertThat(findUser.getEmail()).isEqualTo(userRequest.getEmail());

        log.info("password={}", findUser.getPassword());
    }

    @Test
    @Rollback
    void 회원가입_실패_이메일중복() {
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);

        UserJoinRequest userRequest2 = UserJoinRequest.builder()
                .email(userRequest.getEmail())
                .password("pass")
                .username("zzz")
                .build();

        assertThrows(DuplicateEmailException.class, () -> {
            userService.join(userRequest2);
        });
    }

    @Test
    @Rollback
    void 회원가입_실패_이름중복() {
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);

        UserJoinRequest userRequest2 = UserJoinRequest.builder()
                .email("bbb@bbb.com")
                .password("pass")
                .username("cho")
                .build();

        assertThrows(DuplicateUsernameException.class, () -> {
            userService.join(userRequest2);
        });
    }

    @Test
    @Rollback
    void 회원가입_실패_이메일_이름_모두중복() {
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);

        UserJoinRequest userRequest2 = UserJoinRequest.builder()
                .email(userRequest.getEmail())
                .password("pass")
                .username(userRequest.getUsername())
                .build();

        assertThrows(DuplicateEmailAndUsernameException.class, () -> {
            userService.join(userRequest2);
        });
    }

    private UserJoinRequest userJoinRequest() {
        return UserJoinRequest.builder()
                .email("test@aaa.com")
                .password("pass")
                .username("cho")
                .build();
    }

    @Test
    @Rollback
    void 회원삭제_상품없이() {
        // given
        UserJoinRequest userJoinRequest = userJoinRequest();
        userService.join(userJoinRequest);

        // when
        userService.deleteUser(userJoinRequest.getEmail());

        // then
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(userJoinRequest.getEmail()));
    }

    @Test
    @Rollback
    void 회원삭제_상품3개있음() {
        // given
        UserJoinRequest userJoinRequest = userJoinRequest();
        userService.join(userJoinRequest);

        ItemRegisterDto itemDto = new ItemRegisterDto("오브젝트", 40000, 30, userJoinRequest.getEmail());
        itemService.register(itemDto);
        ItemRegisterDto itemDto2 = new ItemRegisterDto("쿠버네티스", 40000, 30, userJoinRequest.getEmail());
        itemService.register(itemDto2);
        ItemRegisterDto itemDto3 = new ItemRegisterDto("자바자바자바", 40000, 30, userJoinRequest.getEmail());
        itemService.register(itemDto3);

        // when
        log.info("deleteUser() start");
        userService.deleteUser(userJoinRequest.getEmail());
        log.info("deleteUser() end");
        // then
        assertThrows(ItemNotFoundException.class,
                () -> itemService.findByItemNameAndEmail(itemDto.getItemName(), userJoinRequest().getEmail()));
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(userJoinRequest.getEmail()));
    }

    @Test
    @Rollback
    void 회원삭제_상품3개있음_논리삭제() {
        // given
        UserJoinRequest userJoinRequest = userJoinRequest();
        userService.join(userJoinRequest);

        ItemRegisterDto itemDto = new ItemRegisterDto("오브젝트", 40000, 30, userJoinRequest.getEmail());
        itemService.register(itemDto);
        ItemRegisterDto itemDto2 = new ItemRegisterDto("쿠버네티스", 40000, 30, userJoinRequest.getEmail());
        itemService.register(itemDto2);
        ItemRegisterDto itemDto3 = new ItemRegisterDto("자바자바자바", 40000, 30, userJoinRequest.getEmail());
        itemService.register(itemDto3);

        // when
        log.info("deleteUser() start");
        userService.deleteUserLogical(userJoinRequest.getEmail());
        log.info("deleteUser() end");
        // then
        assertThat(itemService.findByItemNameAndEmail(itemDto.getItemName(), userJoinRequest().getEmail()).getDeletedAt())
                .isNotNull();
        assertThat(userService.findByEmail(userJoinRequest.getEmail()).getDeletedAt())
                .isNotNull();
    }
}
