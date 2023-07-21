package com.example.deapseashop.user;

import com.example.deapseashop.domain.item.ItemEntity;
import com.example.deapseashop.domain.item.ItemRegisterDto;
import com.example.deapseashop.domain.item.ItemService;
import com.example.deapseashop.domain.item.ItemUpdateDto;
import com.example.deapseashop.domain.user.dtos.UserJoinRequest;
import com.example.deapseashop.domain.user.entities.UserEntity;
import com.example.deapseashop.domain.user.services.UserService;
import com.example.deapseashop.exceptions.DuplicateEmailException;
import com.example.deapseashop.exceptions.DuplicateItemException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
public class ItemServiceTest {

    @Autowired
    public UserService userService;

    @Autowired
    public ItemService itemService;

    @Test
    @Rollback
    void 상품등록_1개_성공() {
        // given
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);
        UserEntity findUser = userService.findByEmail(userRequest.getEmail()).orElseThrow();
        // 로그인했고 본인이라고 가정

        ItemRegisterDto itemDto = new ItemRegisterDto("오브젝트", 40000, 30);

        // when
        Long savedItemId = itemService.register(itemDto, findUser.getEmail());
        Optional<ItemEntity> findItemOpt = itemService.findById(savedItemId);
        ItemEntity findItem = findItemOpt.get();

        List<ItemEntity> sellingItems = itemService.findBySeller(findUser);

        // then
        assertThat(findItemOpt).isNotEmpty();
        assertThat(savedItemId).isEqualTo(findItem.getId());
        assertThat(findItem.getItemName()).isEqualTo(itemDto.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(itemDto.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(itemDto.getQuantity());

        assertThat(sellingItems.size()).isEqualTo(1);
    }

    @Test
    @Rollback
    void 상품등록_5개_성공() {
        // given
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);
        UserEntity findUser = userService.findByEmail(userRequest.getEmail()).orElseThrow();
        // 로그인했고 본인이라고 가정

        ItemRegisterDto itemDto1 = new ItemRegisterDto("오브젝트", 40000, 30);
        ItemRegisterDto itemDto2 = new ItemRegisterDto("모던 자바 인 액션", 35000, 12);
        ItemRegisterDto itemDto3 = new ItemRegisterDto("쿠버네티스 인 액션", 45000, 60);
        ItemRegisterDto itemDto4 = new ItemRegisterDto("realmysql", 30000, 20);
        ItemRegisterDto itemDto5 = new ItemRegisterDto("누구에게도 상처받을 필요는 없다", 15000, 1000);

        // when
        itemService.register(itemDto1, findUser.getEmail());
        itemService.register(itemDto2, findUser.getEmail());
        itemService.register(itemDto3, findUser.getEmail());
        itemService.register(itemDto4, findUser.getEmail());
        itemService.register(itemDto5, findUser.getEmail());

        List<ItemEntity> sellingItems = itemService.findBySeller(findUser);

        // then
        assertThat(sellingItems.size()).isEqualTo(5);
    }

    @Test
    @Rollback
    void 상품등록_상품이름중복_실패() {
        // given
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);
        UserEntity findUser = userService.findByEmail(userRequest.getEmail()).orElseThrow();
        // 로그인했고 본인이라고 가정

        ItemRegisterDto itemDto1 = new ItemRegisterDto("오브젝트", 40000, 30);
        ItemRegisterDto itemDto2 = new ItemRegisterDto("오브젝트", 40000, 30);

        Long savedItemId1 = itemService.register(itemDto1, findUser.getEmail());

        //when
        assertThrows(DuplicateItemException.class, () -> {
            itemService.register(itemDto2, findUser.getEmail());
        });
    }

    @Test
    @Rollback
    void 상품삭제() {
        // given
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);
        UserEntity findUser = userService.findByEmail(userRequest.getEmail()).orElseThrow();
        // 로그인했고 본인이라고 가정

        ItemRegisterDto itemDto1 = new ItemRegisterDto("오브젝트", 40000, 30);
        ItemRegisterDto itemDto2 = new ItemRegisterDto("모던 자바 인 액션", 35000, 12);
        ItemRegisterDto itemDto3 = new ItemRegisterDto("쿠버네티스 인 액션", 45000, 60);
        itemService.register(itemDto1, findUser.getEmail());
        itemService.register(itemDto2, findUser.getEmail());
        itemService.register(itemDto3, findUser.getEmail());

        // when
        itemService.deleteItem(itemDto1.getItemName(), findUser.getEmail());
        List<ItemEntity> sellingItems = itemService.findBySeller(findUser);

        List<String> itemNameList = sellingItems.stream()
                .map(itemEntity -> itemEntity.getItemName())
                .collect(Collectors.toList());

        // then
        assertThat(sellingItems.size()).isEqualTo(2);
        assertThat(itemNameList.contains(itemDto1.getItemName())).isFalse();
        assertThat(itemNameList.contains(itemDto2.getItemName())).isTrue();
        assertThat(itemNameList.contains(itemDto3.getItemName())).isTrue();
    }

    @Test
    @Rollback
    void 상품수정() {
        // given
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);
        UserEntity findUser = userService.findByEmail(userRequest.getEmail()).orElseThrow();
        // 로그인했고 본인이라고 가정

        ItemRegisterDto itemDto = new ItemRegisterDto("오브젝트", 40000, 30);
        itemService.register(itemDto, findUser.getEmail());

        int downPrice = itemDto.getPrice() - 1000;
        int upQuantity = itemDto.getQuantity() + 10;
        String newName = "오브젝트 2판";
        // when
        itemService.updateItem(itemDto.getItemName(), findUser.getEmail(), new ItemUpdateDto(
                newName, downPrice, upQuantity));

        List<ItemEntity> sellingItems = itemService.findBySeller(findUser);
        List<String> itemNameList = sellingItems.stream()
                .map(itemEntity -> itemEntity.getItemName())
                .collect(Collectors.toList());
        String findItemName = itemNameList.get(0);

        assertThat(findItemName).isEqualTo(newName);

        ItemEntity findItem = itemService.findByItemNameAndEmail(newName, findUser.getEmail());

        assertThat(findItem.getPrice()).isEqualTo(downPrice);
        assertThat(findItem.getQuantity()).isEqualTo(upQuantity);
    }

    private UserJoinRequest userJoinRequest() {
        return UserJoinRequest.builder()
                .email("test@aaa.com")
                .password("pass")
                .username("cho")
                .build();
    }
}
