package ru.extreme.bot.clickbot.service.click;

import ru.extreme.bot.clickbot.model.ProfileAccount;

import java.util.List;

public interface ProfileAccountService {

    List<ProfileAccount> findProfileAccountsByProfileId(Long profileId);

    List<Long> getProfilesIDWhereAccBalanceLess5000();

}
