package kr.magicbox.creator.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.magicbox.creator.application.dto.command.ApplyCertificationCommand;
import kr.magicbox.creator.domain.enums.MagicGenre;
import kr.magicbox.creator.domain.vo.UserId;
import lombok.Builder;

import java.util.Set;

@Builder
public record ApplyCertificationRequest(
        @NotEmpty Set<@NotNull MagicGenre> genres,
        @NotBlank String portfolioUrl
) {

    public ApplyCertificationCommand toCommand(UserId userId) {
        return new ApplyCertificationCommand(
                userId,
                genres,
                portfolioUrl
        );
    }
}