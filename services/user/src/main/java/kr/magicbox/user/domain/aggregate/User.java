package kr.magicbox.user.domain.aggregate;

import kr.magicbox.user.domain.vo.Nickname;
import kr.magicbox.user.domain.vo.UserId;
import kr.magicbox.user.domain.enums.UserRole;
import kr.magicbox.user.domain.enums.UserStatus;
import kr.magicbox.user.domain.enums.OAuth2Provider;
import kr.magicbox.user.domain.exception.InvalidFieldException;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Getter
public class User {
    private final UserId id;
    private Nickname nickname;
    private final String email;
    private UserStatus status;
    private final UserRole role;
    private Instant lastLoginAt;
    private String profile;
    private Duration totalUsageTime;
    private Boolean isReviewVisible;
    private Boolean isActive;
    private final String oauth2Id;
    private final OAuth2Provider oauth2Provider;

    @Builder
    public User(UserId id, Nickname nickname, String email, UserStatus status, 
                UserRole role, String profile, String oauth2Id, 
                OAuth2Provider oauth2Provider) {
        validateFields(nickname, email, status, role, profile, oauth2Id, oauth2Provider);
        
        this.id = id;
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

    public Long getId() {
        return this.id.value();
    }

    public String getNickname() {
        return this.nickname.value();
    }
    
    private void validateFields(Nickname nickname, String email, UserStatus status, 
                               UserRole role, String profile, String oauth2Id, 
                               OAuth2Provider oauth2Provider) {
        if (nickname == null) {
            throw new InvalidFieldException("닉네임은 필수 값입니다.");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidFieldException("이메일은 필수 값입니다.");
        }

        if (status == null) {
            throw new InvalidFieldException("상태는 필수 값입니다.");
        }
        
        if (role == null) {
            throw new InvalidFieldException("역할은 필수 값입니다.");
        }
        
        if (profile == null || profile.trim().isEmpty()) {
            throw new InvalidFieldException("프로필은 필수 값입니다.");
        }
        
        if (oauth2Id == null || oauth2Id.trim().isEmpty()) {
            throw new InvalidFieldException("OAuth2 ID는 필수 값입니다.");
        }
        
        if (oauth2Provider == null) {
            throw new InvalidFieldException("OAuth2 제공자는 필수 값입니다.");
        }
    }

    public void updateProfile(Nickname nickname, String profile) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (profile != null && !profile.trim().isEmpty()) {
            this.profile = profile;
        }
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

    public void disableReviewVisibility() {
        this.isReviewVisible = false;
    }

    public boolean canShowReview() {
        return Boolean.TRUE.equals(this.isReviewVisible);
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(this.isActive);
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void delete() {
        this.status = UserStatus.DELETED;
    }
}
