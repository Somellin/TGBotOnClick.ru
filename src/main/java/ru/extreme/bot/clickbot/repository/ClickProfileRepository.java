package ru.extreme.bot.clickbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.extreme.bot.clickbot.model.ClickProfile;

import java.util.List;
import java.util.Set;

/**
 * Репозиторий для профилей с click.ru
 */
@Repository
public interface ClickProfileRepository extends JpaRepository<ClickProfile, Long> {

    @Query("select count(cp) > 0 from CLICK_PROFILE cp where cp.encodeToken = ?1")
    boolean checkProfileExist(String encodedToken);

    ClickProfile getClickProfileById(Long profileId);

    @Query("select cp.encodeToken from CLICK_PROFILE cp")
    Set<String> findAllProfileEncodedTokens();

    @Query("select cp from CLICK_PROFILE cp where cp.id in ?1")
    List<ClickProfile> findAllProfileInIds(List<Long> profilesId);
}
