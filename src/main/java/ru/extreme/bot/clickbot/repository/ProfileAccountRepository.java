package ru.extreme.bot.clickbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.extreme.bot.clickbot.model.ProfileAccount;

import java.util.List;

/**
 * Репозиторий для аккаунтов профилей с click.ru
 */
@Repository
public interface ProfileAccountRepository extends JpaRepository<ProfileAccount, Long> {

    @Query("select pa from PROFILE_ACCOUNT pa where pa.profileId = ?1 and pa.service in ?2")
    List<ProfileAccount> findProfileAccountsByProfileId(Long profileId, List<String> services);

    @Query("select pa.profileId from PROFILE_ACCOUNT pa where pa.service in ?1 and pa.balance < 5000")
    List<Long> getProfilesIDWhereAccBalanceLess5000(List<String> strings);
}
