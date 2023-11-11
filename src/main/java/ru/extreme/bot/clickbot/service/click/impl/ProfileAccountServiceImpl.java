package ru.extreme.bot.clickbot.service.click.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.extreme.bot.clickbot.enums.Services;
import ru.extreme.bot.clickbot.model.ProfileAccount;
import ru.extreme.bot.clickbot.repository.ProfileAccountRepository;
import ru.extreme.bot.clickbot.service.click.ProfileAccountService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileAccountServiceImpl implements ProfileAccountService {

    private final ProfileAccountRepository profileAccountsRepository;

    @Override
    public List<ProfileAccount> findProfileAccountsByProfileId(Long profileId) {
        return profileAccountsRepository.findProfileAccountsByProfileId(profileId, Services.mainServices());
    }

    @Override
    public List<Long> getProfilesIDWhereAccBalanceLess5000() {
        return profileAccountsRepository.getProfilesIDWhereAccBalanceLess5000(Services.mainServices());
    }
}
