package com.ormi.storywave.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class BanService {

    private final BanRepository banRepository;

    public BanService(BanRepository banRepository) {
        this.banRepository = banRepository;
    }

    public BanDto getBanByUserId(String userId) {
        return banRepository.findByUser_UserId(userId)
                .map(BanDto::fromBan)
                .orElse(null);
    }

}
