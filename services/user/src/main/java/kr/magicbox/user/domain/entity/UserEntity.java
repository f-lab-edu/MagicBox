package kr.magicbox.user.domain.entity;

import jakarta.persistence.*;
import kr.magicbox.user.domain.constants.UserPolicyConstants;
import kr.magicbox.user.domain.enums.UserRole;
import kr.magicbox.user.domain.enums.UserStatus;
import kr.magicbox.user.domain.enums.OAuth2Provider;
import kr.magicbox.user.domain.exception.InvalidFieldException;
import kr.magicbox.user.global.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Getter
    @Column(unique = true, nullable = false, length = UserPolicyConstants.nicknameMaxLength)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Getter
    private Instant lastLoginAt;

    @Column(nullable = false)
    private Boolean isActive;

    @Getter
    @Column(columnDefinition = "TEXT", nullable = false)
    private String profile;

    @Column(nullable = false)
    private Duration totalUsageTime;

    @Column(nullable = false)
    private Boolean isReviewVisible;

    @Getter
    @Column(nullable = false, unique = true, updatable = false)
    private String oauth2Id;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private OAuth2Provider oauth2Provider;

    @Builder
    public UserEntity(String nickname, String email, UserStatus status, UserRole role, String profile, String oauth2Id, OAuth2Provider oauth2Provider) {
        validateFields(nickname, email, status, role, profile, oauth2Id, oauth2Provider);

        this.nickname = nickname;
        this.email = email;
        this.status = status;
        this.role = role;
        this.profile = profile;
        this.oauth2Id = oauth2Id;
        this.oauth2Provider = oauth2Provider;
        this.isActive = false;
        this.lastLoginAt = Instant.now();
        this.totalUsageTime = Duration.ZERO;
        this.isReviewVisible = true;
    }

    private void validateFields(String nickname, String email, UserStatus status, UserRole role, String profile, String oauth2Id, OAuth2Provider oauth2Provider) {
        if(nickname == null || nickname.isEmpty())
            throw new InvalidFieldException("닉네임은 필수 값입니다.");

        if(nickname.length() < UserPolicyConstants.nicknameMinLength || nickname.length() > UserPolicyConstants.nicknameMaxLength)
            throw new InvalidFieldException("닉네임은 " + UserPolicyConstants.nicknameMinLength + "자 이상 " + UserPolicyConstants.nicknameMaxLength + "자 이내여야 합니다.");
        
        if(email == null || email.isEmpty())
            throw new InvalidFieldException("이메일은 필수 값입니다.");

        if(status == null)
            throw new InvalidFieldException("상태는 필수 값입니다.");
        
        if(role == null)
            throw new InvalidFieldException("역할은 필수 값입니다.");
        
        if(profile == null || profile.isEmpty())
            throw new InvalidFieldException("프로필은 필수 값입니다.");
        
        if(oauth2Id == null || oauth2Id.isEmpty())
            throw new InvalidFieldException("OAuth2 ID는 필수 값입니다.");
        
        if(oauth2Provider == null)
            throw new InvalidFieldException("OAuth2 제공자는 필수 값입니다.");
    }

    public void updateProfile(String nickname, String profile) {
        updateNicknameIfValid(nickname);
        updateProfileIfValid(profile);
    }
    
    private void updateNicknameIfValid(String nickname) {
        if(nickname == null || nickname.isEmpty()) {
            return;
        }
        
        if(nickname.length() < UserPolicyConstants.nicknameMinLength || nickname.length() > UserPolicyConstants.nicknameMaxLength) {
            throw new InvalidFieldException("닉네임은 " + UserPolicyConstants.nicknameMinLength + "자 이상 " + UserPolicyConstants.nicknameMaxLength + "자 이내여야 합니다.");
        }
        
        this.nickname = nickname;
    }
    
    private void updateProfileIfValid(String profile) {
        if(profile == null || profile.isEmpty()) {
            return;
        }
        
        this.profile = profile;
    }

    public void startSession() {
        this.isActive = true;
        this.lastLoginAt = Instant.now();
    }

    public void endSession() {
        if (Boolean.TRUE.equals(this.isActive) && this.lastLoginAt != null) {
            Duration sessionTime = Duration.between(this.lastLoginAt, Instant.now());
            this.totalUsageTime = this.totalUsageTime.plus(sessionTime);
        }
        this.isActive = false;
    }

    public void enableReviewVisibility() {
        this.isReviewVisible = true;
    }

    public void unableReviewVisibility() {
        this.isReviewVisible = false;
    }

    public Boolean canShowReview() {
        return this.isReviewVisible;
    }
}