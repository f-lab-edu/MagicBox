package kr.magicbox.user.application.port.in;

import kr.magicbox.user.domain.vo.Nickname;

public interface BanUserUseCase {

    void banUser(Nickname nickname);
}