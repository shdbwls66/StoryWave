/*
package com.ormi.storywave.Ban;

import com.ormi.storywave.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ban")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="ban_status")
    private boolean banStatus;

    @Column(name="ban_date")
    private LocalDateTime banDate;

    @Column(name="ban_period")
    private Integer banPeriod;

    @Column(name="ban_reason")
    private String banReason;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User users;

}
*/
