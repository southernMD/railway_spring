package org.railway.service;

import lombok.RequiredArgsConstructor;
import org.railway.entity.User;
import org.railway.entity.VerificationCode;
import org.railway.service.impl.UserRepository;
import org.railway.service.impl.VerificationCodeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCode;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(emailOrUsername)
                .orElseGet(() -> userRepository.findByEmail(emailOrUsername)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getUserType() == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                "{noop}" + user.getPassword(),
                authorities
        );
    }


    /**
     * 根据邮箱获取验证码信息
     *
     * @param email 邮箱地址
     * @return 匹配的验证码实体对象
     * @throws RuntimeException 如果验证码不存在则抛出异常
     */
    public VerificationCode getVerificationCodeByEmail(String email) {
        return verificationCode.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("验证码不存在"));
    }

    /**
     * 更新或插入指定邮箱的验证码和过期时间
     *
     * @param email      邮箱地址
     * @param code       新的验证码
     * @param expireTime 新的过期时间
     */
    public void updateCodeAndExpireTime(String email, String code, LocalDateTime expireTime) {
        int updated = verificationCode.updateCodeAndExpireTimeByEmail(email, code, expireTime);

        if (updated == 0) {
            VerificationCode newCode = new VerificationCode();
            newCode.setEmail(email);
            newCode.setCode(code);
            newCode.setExpireTime(expireTime);
            newCode.setIsUsed(0);
            verificationCode.save(newCode);
        }
    }

    /**
     * 将指定邮箱的验证码标记为已使用
     *
     * @param email 邮箱地址
     */
    public void markVerificationCodeAsUsed(String email) {
        verificationCode.updateIsUsedByEmail(email);
    }

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * 检查邮箱是否是否已被注册
     *
     * @param email 邮箱地址
     * @return 是否存在
     */
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * 保存新用户
     *
     * @param user 用户实体
     * @return 用户实体
     */
    public User saveUser(User user) {
        // 可选：密码加密处理（推荐）
        // BCrypt 加密示例：
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
       return userRepository.save(user);
    }

    /**
     * 查询用户更具email
     *
     * @param email 邮箱
     * @return 用户实体
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    /**
     * 查询用户更具username
     *
     * @param username 用户名
     * @return 用户实体
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
