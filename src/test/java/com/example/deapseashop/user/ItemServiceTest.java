package com.example.deapseashop.user;

import com.example.deapseashop.domain.item.*;
import com.example.deapseashop.domain.user.dtos.UserJoinRequest;
import com.example.deapseashop.domain.user.entities.UserEntity;
import com.example.deapseashop.domain.user.repositories.UserRepository;
import com.example.deapseashop.domain.user.services.UserService;
import com.example.deapseashop.exceptions.DuplicateEmailException;
import com.example.deapseashop.exceptions.DuplicateItemException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.Random;
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

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public ItemRepository itemRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    @Rollback
    void 상품등록_1개_성공() {
        // given
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);
        UserEntity findUser = userService.findByEmail(userRequest.getEmail());
        // 로그인했고 본인이라고 가정

        ItemRegisterDto itemDto = new ItemRegisterDto("오브젝트", 40000, 30, findUser.getEmail());

        // when
        log.info("--- qu eries 3");
        Long savedItemId = itemService.register(itemDto);
        log.info("--- queries 6");
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
        UserEntity findUser = userService.findByEmail(userRequest.getEmail());
        // 로그인했고 본인이라고 가정

        ItemRegisterDto itemDto1 = new ItemRegisterDto("오브젝트", 40000, 30, findUser.getEmail());
        ItemRegisterDto itemDto2 = new ItemRegisterDto("모던 자바 인 액션", 35000, 12, findUser.getEmail());
        ItemRegisterDto itemDto3 = new ItemRegisterDto("쿠버네티스 인 액션", 45000, 60, findUser.getEmail());
        ItemRegisterDto itemDto4 = new ItemRegisterDto("realmysql", 30000, 20, findUser.getEmail());
        ItemRegisterDto itemDto5 = new ItemRegisterDto("누구에게도 상처받을 필요는 없다", 15000, 1000, findUser.getEmail());

        // when
        itemService.register(itemDto1);
        itemService.register(itemDto2);
        itemService.register(itemDto3);
        itemService.register(itemDto4);
        itemService.register(itemDto5);

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
        UserEntity findUser = userService.findByEmail(userRequest.getEmail());
        // 로그인했고 본인이라고 가정

        ItemRegisterDto itemDto1 = new ItemRegisterDto("오브젝트", 40000, 30, findUser.getEmail());
        ItemRegisterDto itemDto2 = new ItemRegisterDto("오브젝트", 40000, 30, findUser.getEmail());

        Long savedItemId1 = itemService.register(itemDto1);

        //when
        assertThrows(DuplicateItemException.class, () -> {
            itemService.register(itemDto2);
        });
    }

    @Test
    @Rollback
    void 상품삭제() {
        // given
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);
        UserEntity findUser = userService.findByEmail(userRequest.getEmail());
        // 로그인했고 본인이라고 가정

        ItemRegisterDto itemDto1 = new ItemRegisterDto("오브젝트", 40000, 30, findUser.getEmail());
        ItemRegisterDto itemDto2 = new ItemRegisterDto("모던 자바 인 액션", 35000, 12, findUser.getEmail());
        ItemRegisterDto itemDto3 = new ItemRegisterDto("쿠버네티스 인 액션", 45000, 60, findUser.getEmail());
        itemService.register(itemDto1);
        itemService.register(itemDto2);
        itemService.register(itemDto3);

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
        UserEntity findUser = userService.findByEmail(userRequest.getEmail());
        // 로그인했고 본인이라고 가정

        ItemRegisterDto itemDto = new ItemRegisterDto("오브젝트", 40000, 30, findUser.getEmail());
        itemService.register(itemDto);

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

    @Test
    @Rollback
    void 상품전제조회_익명사용자() {
        // given
        UserJoinRequest userRequest = userJoinRequest();
        userService.join(userRequest);

        int count = 5;
        for (int i = 0; i < count; i++) {
            ItemRegisterDto itemDto = new ItemRegisterDto("오브젝트" + i, 40000, 30, userRequest.getEmail());
            itemService.register(itemDto);
        }

        // when
        List<ItemDto> itemDtoList = itemService.findAllWithSellerName();

        assertThat(itemDtoList.size()).isEqualTo(count);
        assertThat(itemDtoList.get(2).getSellerName()).isEqualTo(userRequest.getUsername());
    }

    @Test
    @Rollback
    void 상품전제조회_회원판매상품제외() {
        // given
        UserJoinRequest userRequest1 = userJoinRequest();
        userService.join(userRequest1);
        UserJoinRequest userRequest2 = UserJoinRequest.builder()
                .email("abc@abc.com")
                .password("pass")
                .username("kim")
                .build();
        userService.join(userRequest2);
        int count = 5;
        for (int i = 0; i < count; i++) {
            ItemRegisterDto itemDto = new ItemRegisterDto("쿠버네티스" + i, 40000, 30, userRequest1.getEmail());
            itemService.register(itemDto);
            itemDto = new ItemRegisterDto("오브젝트으으으으" + i, 40000, 30, userRequest2.getEmail());
            itemService.register(itemDto);
        }

        // when
        // userRequest1 로그인 되어있다고 가정
        List<ItemDto> itemDtoList1 = itemService.findAllWithSellerNameNotLoginUser(userRequest1.getEmail());

        // userRequest2 로그인 되어있다고 가정
        List<ItemDto> itemDtoList2 = itemService.findAllWithSellerNameNotLoginUser(userRequest2.getEmail());

        assertThat(itemDtoList1.size()).isEqualTo(count);
        List<ItemDto> filteredItemDtoList1 = itemDtoList1.stream()
                .filter(itemDto -> itemDto.getItemName().startsWith("오브젝트으으으으")).toList();
        assertThat(filteredItemDtoList1.size()).isEqualTo(count);

        assertThat(itemDtoList2.size()).isEqualTo(count);
        List<ItemDto> filteredItemDtoList2 = itemDtoList2.stream()
                .filter(itemDto -> itemDto.getItemName().startsWith("쿠버네티스")).toList();
        assertThat(filteredItemDtoList2.size()).isEqualTo(count);

    }

    private UserJoinRequest userJoinRequest() {
        return UserJoinRequest.builder()
                .email("test@aaa.com")
                .password("pass")
                .username("cho")
                .build();
    }
}
