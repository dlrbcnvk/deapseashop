package com.example.deapseashop.domain.item;

import com.example.deapseashop.domain.user.entities.UserEntity;
import com.example.deapseashop.domain.user.repositories.UserRepository;
import com.example.deapseashop.exceptions.DuplicateItemException;
import com.example.deapseashop.exceptions.ItemNotFoundException;
import com.example.deapseashop.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public List<ItemEntity> findAll() {
        return itemRepository.findAll();
    }

    @Transactional
    public Long register(ItemRegisterDto itemDto, String email) {
        /**
         * 서비스 오기 전에 컨트롤러에서 본인이 맞는지 그리고 로그인되어있는지 확인해야 함
         * 영속성 컨텍스트 위해 한번 더 조회
         * 컨트롤러부터 트랜잭션을 시작하면 영속성이 계속 이어질 것이니 쿼리를 하나라도 더 줄일 수 있지 않을까 생각도 든다.
         * 고민을 좀 더 해볼 것
         */
        Optional<UserEntity> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException("회원을 찾을 수 없습니다.");
        }
        UserEntity user = findUser.get();

        ItemEntity item = new ItemEntity(itemDto.getItemName(), itemDto.getPrice(), itemDto.getQuantity(), user);

        // 본인이 올린 상품들 중 중복된 이름의 상품이 있는지 확인해야함
        Optional<ItemEntity> duplicatedItem = itemRepository.findByItemNameAndSeller(item.getItemName(), user);
        if (duplicatedItem.isPresent()) {
            throw new DuplicateItemException("같은 이름의 상품이 이미 등록되어 있습니다.");
        }
        itemRepository.save(item);

        List<ItemEntity> sellingItems = user.getSellingItems();
        log.info("At ItemService.register() after save(), sellingItems.size()={}", sellingItems.size());

        return item.getId();
    }

    private ItemEntity findByItemNameAndSeller(String itemName, UserEntity seller) {
        // seller 가 이미 1차캐시에 있다고 가정 (호출한 메서드로부터 트랜잭션 진행중이라고 가정)
        Optional<ItemEntity> findItem = itemRepository.findByItemNameAndSeller(itemName, seller);
        if (findItem.isEmpty()) {
            throw new ItemNotFoundException("상품이 존재하지 않습니다.");
        }
        return findItem.get();
    }

    public ItemEntity findByItemNameAndEmail(String itemName, String email) {
        // seller 가 1차캐시에 없을 때, 즉 트랜잭션 없는 컨트롤러에서 바로 아이템 하나를 조회하려고 서비스로 넘어오는 경우
        // 페치 조인
        Optional<ItemEntity> findItem = itemRepository.findByItemNameAndSellerEmail(itemName, email);
        if (findItem.isEmpty()) {
            throw new ItemNotFoundException("상품이 존재하지 않습니다.");
        }
        return findItem.get();
    }



    public Optional<ItemEntity> findById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    public List<ItemEntity> findBySeller(UserEntity user) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(user.getEmail());
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("회원을 찾을 수 없습니다.");
        }
        return itemRepository.findBySeller(userOpt.get());
    }

    @Transactional
    public void deleteItem(String itemName, String email) {
        Optional<UserEntity> findUserOpt = userRepository.findByEmail(email);
        if (findUserOpt.isEmpty()) {
            throw new UserNotFoundException("회원이 존재하지 않습니다.");
        }
        ItemEntity findItem = findByItemNameAndSeller(itemName, findUserOpt.get());
        itemRepository.delete(findItem);
    }

    @Transactional
    public void updateItem(String itemName, String email, ItemUpdateDto itemUpdateDto) {
        Optional<UserEntity> findUserOpt = userRepository.findByEmail(email);
        if (findUserOpt.isEmpty()) {
            throw new UserNotFoundException("회원이 존재하지 않습니다.");
        }
        ItemEntity findItem = findByItemNameAndSeller(itemName, findUserOpt.get());
        findItem.updateItem(
                itemUpdateDto.getUpdateItemName(),
                itemUpdateDto.getUpdatePrice(),
                itemUpdateDto.getUpdateQuantity());
    }
}
