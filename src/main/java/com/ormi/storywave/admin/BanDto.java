package com.ormi.storywave.admin;

import com.ormi.storywave.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BanDto {

    private Integer id;
    private boolean banStatus;
    private LocalDateTime banDate;
    private Integer banPeriod;
    private String banReason;
    private String userId;

    // Entity -> Dto
    public static BanDto fromBan(Ban ban) {
        BanDto banDto = BanDto.builder()
                .id(ban.getId())
                .banStatus(ban.isBanStatus())
                .banDate(ban.getBanDate())
                .banPeriod(ban.getBanPeriod())
                .banReason(ban.getBanReason())
                .userId(ban.getUser().getUserId())
                .build();

        return banDto;
    }

    // Dto -> Entity
    public Ban toBan() {
        Ban ban = new Ban();
        ban.setId(this.id);
        ban.setBanStatus(this.banStatus);
        ban.setBanDate(this.banDate);
        ban.setBanPeriod(this.banPeriod);
        ban.setBanReason(this.banReason);

        return ban;
    }
}
