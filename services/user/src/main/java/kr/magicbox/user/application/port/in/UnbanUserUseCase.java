package kr.magicbox.user.application.port.in;

import kr.magicbox.user.domain.vo.Nickname;

public interface UnbanUserUseCase {

    void unbanUser(Nickname nickname);
}
