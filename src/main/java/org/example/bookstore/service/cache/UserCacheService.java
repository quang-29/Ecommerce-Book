package org.example.bookstore.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.enums.Roles;
import org.example.bookstore.model.UserEntity;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.dto.UserSaveDto;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCacheService {

    private static final String CUSTOMER_KEY_PREFIX = "CUSTOMER";
    private static final String ADMIN_KEY_PREFIX = "ADMIN";
    private static final String SHOP_OWNER_KEY_PREFIX = "SHOP_OWNER";
    private static final long CACHE_TTL_DAYS = 1;
    private final ObjectMapper objectMapper;


    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void buildCacheAllUserOfStore() {
        log.info("Starting rebuild cache for all users");
        List<UserEntity> listUsers = userRepository.findAll();
        if (listUsers.isEmpty()) {
            log.info("No user data found to build cache");
            return;
        }
        saveIntoCacheStore(listUsers);
    }

    public UserCacheDTO initUserInCache(UserSaveDto userSaveDto){
        return UserCacheDTO.builder()
                .firstName(userSaveDto.getFirstName())
                .lastName(userSaveDto.getLastName())
                .username(userSaveDto.getUserName())
                .email(userSaveDto.getEmail())
                .phoneNumber(userSaveDto.getPhoneNumber())
                .roles(userSaveDto.getRoles())
                .isActive(true)
                .isDeleted(false)
                .isDisabled(false)
                .createdTime(System.currentTimeMillis())
                .avatarUrl(null)
                .build();
    }

    // Save cache for 1 user
    public void createUserCatche(UserSaveDto userSaveDto) {
        if (userSaveDto == null || userSaveDto.getId() == null || userSaveDto.getRoles() == null) {
            log.error("Cannot create cache with null payload or missing required fields");
            return;
        }

        String userId = userSaveDto.getId();
        String key = rolePrefix(userSaveDto.getRoles());
        try {
            UserCacheDTO userCacheDTO = initUserInCache(userSaveDto);
            redisTemplate.opsForHash().put(key, userId, userCacheDTO);
            redisTemplate.expire(key, CACHE_TTL_DAYS, TimeUnit.DAYS);
            log.info("Create user cache success userId: {}", userId);
        } catch (RedisConnectionFailureException | RedisSystemException ex) {
            log.warn("Cannot create user cache for userId {} in Redis", userId, ex);
        }
    }

    public void updateUserCache(UserSaveDto userSaveDto) {
        if (userSaveDto == null || userSaveDto.getId() == null || userSaveDto.getRoles() == null) {
            log.error("Cannot update cache with null payload or missing required fields");
            return;
        }

        String userId = userSaveDto.getId();
        String key = rolePrefix(userSaveDto.getRoles());
        try {
            Object cached = redisTemplate.opsForHash().get(key, userId);
            UserCacheDTO userCacheDTO;
            if (cached != null) {
                // Cache already exists => update it with the fresh values
                UserCacheDTO dtoFromRedis = cached instanceof UserCacheDTO existing
                        ? existing
                        : objectMapper.convertValue(cached, UserCacheDTO.class);
                dtoFromRedis.setFirstName(userSaveDto.getFirstName());
                dtoFromRedis.setLastName(userSaveDto.getLastName());
                dtoFromRedis.setPhoneNumber(userSaveDto.getPhoneNumber());
                dtoFromRedis.setEmail(userSaveDto.getEmail());
                dtoFromRedis.setAvatarUrl(userSaveDto.getAvatarUrl());
                userCacheDTO = dtoFromRedis;
            } else {
                // Cache doesn't exist yet => initialize it
                userCacheDTO = initUserInCache(userSaveDto);
            }
            redisTemplate.opsForHash().put(key, userId, userCacheDTO);
            redisTemplate.expire(key, CACHE_TTL_DAYS, TimeUnit.DAYS);
            log.info("Update user cache success for userId: {}", userId);
        } catch (RedisConnectionFailureException | RedisSystemException | IllegalArgumentException ex) {
            log.warn("Cannot update user cache for userId {} in Redis", userId, ex);
        }
    }

    public void saveIntoCacheStore(List<UserEntity> listUsers) {
        listUsers.forEach(this::cache);
    }

    public void cache(UserEntity user) {
        UserCacheDTO userCacheDTO = toCacheDTO(user);
        String key = rolePrefix(user.getRoles());
        try {
            redisTemplate.opsForHash().put(key, user.getId().toString(), userCacheDTO);
            redisTemplate.expire(key, CACHE_TTL_DAYS, TimeUnit.DAYS);
        } catch (RedisConnectionFailureException | RedisSystemException ex) {
            log.warn("Cannot cache user {} to Redis", user.getId(), ex);
        }
    }

    public Optional<UserCacheDTO> getUser(Long userId) {
        for (Roles role : Roles.values()) {
            Optional<UserCacheDTO> cached = getUser(role, userId);
            if (cached.isPresent()) {
                return cached;
            }
        }
        return Optional.empty();
    }

    public Optional<UserCacheDTO> getUser(Roles role, Long userId) {
        try {
            Object cached = redisTemplate.opsForHash().get(rolePrefix(role), userId.toString());
            if (cached == null) {
                return Optional.empty();
            }
            if (cached instanceof UserCacheDTO userCacheDTO) {
                return Optional.of(userCacheDTO);
            }
            return Optional.of(objectMapper.convertValue(cached, UserCacheDTO.class));
        } catch (RedisConnectionFailureException | RedisSystemException | IllegalArgumentException ex) {
            log.warn("Cannot read user {} ({}) from Redis, fallback to database", userId, role, ex);
            return Optional.empty();
        }
    }

    public void evict(UserEntity user) {
        try {
            redisTemplate.opsForHash().delete(rolePrefix(user.getRoles()), user.getId().toString());
        } catch (RedisConnectionFailureException | RedisSystemException ex) {
            log.warn("Cannot evict user {} from Redis", user.getId(), ex);
        }
    }

    private UserCacheDTO toCacheDTO(UserEntity user) {
        UserCacheDTO userCacheDTO = new UserCacheDTO();
        userCacheDTO.setUserId(user.getId());
        userCacheDTO.setUsername(user.getUsername());
        userCacheDTO.setFirstName(user.getFirstName());
        userCacheDTO.setLastName(user.getLastName());
        userCacheDTO.setPhoneNumber(user.getPhoneNumber());
        userCacheDTO.setEmail(user.getEmail());
        userCacheDTO.setAvatarUrl(user.getAvatarUrl());
        userCacheDTO.setRoles(user.getRoles());
        return userCacheDTO;
    }

    private String rolePrefix(Roles role) {
        return switch (role) {
            case ADMIN -> ADMIN_KEY_PREFIX;
            case USER -> CUSTOMER_KEY_PREFIX;
            case SHOP_OWNER -> SHOP_OWNER_KEY_PREFIX;
        };
    }
}
