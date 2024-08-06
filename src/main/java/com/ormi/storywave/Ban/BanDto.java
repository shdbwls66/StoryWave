/*
package com.ormi.storywave.Ban;

import com.ormi.storywave.users.User;
import com.ormi.storywave.users.UserDto;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BanDto {
    private Integer id;
    private boolean banStatus;
    private LocalDateTime banDate;
    private Integer banPeriod;
    private String banReason;
    private User users;



    public static BanDto fromBan(Ban ban) {
        BanDto banDto =
                BanDto.builder()
                        .id(ban.getId())
                        .banStatus(ban.isBanStatus())
                        .banDate(ban.getBanDate())
                        .banPeriod(ban.getBanPeriod())
                        .banReason(ban.getBanReason())
                        .users(ban.getUsers())
                        .build();
        return banDto;
    }

    // DTO -> Entity
    public Ban toBan() {
        Ban ban = new Ban();
        ban.setId(this.id);
        ban.setBanStatus(this.banStatus);
        ban.setBanDate(this.banDate);
        ban.setBanPeriod(this.banPeriod);
        ban.setBanReason(this.banReason);
        ban.setUsers(this.users);
        return ban;
    }
}


*/
