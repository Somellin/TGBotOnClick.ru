package ru.extreme.bot.clickbot.service.click;

import ru.extreme.bot.clickbot.exception.ServiceException;
import ru.extreme.bot.clickbot.model.ClickProfile;
import ru.extreme.bot.clickbot.model.ProfileAccount;

import java.util.List;
import java.util.Map;

public interface ClickProfileService {

    /**
     * Добавление нового профиля click.ru
     *
     * @param token токен
     * @return новый профиль
     */
    ClickProfile addNewProfile(String token) throws ServiceException;

    /**
     * Получение всех профилей
     *
     * @return лист профилей click.ru
     */
    List<ClickProfile> findAllProfiles();

    /**
     * Получить профиль по id
     *
     * @param profileId идентификатор профиля
     * @return наименование удаленного профиля
     */
    ClickProfile getClickProfileById(Long profileId);

    /**
     * Удаление профиля click.ru
     *
     * @param profileId - идентификатор профиля
     * @return наименование удаленного профиля
     */
    String deleteProfileById(Long profileId);

    /**
     * Проверка профиля на существование
     */
    boolean checkProfileExist(String token) throws Exception;

    /**
     * Обновление имеющихся профилей в базе
     */
    List<ClickProfile> updateAllProfilesData() throws ServiceException, InterruptedException;

    /**
     * Получение мапы профилей и аккаунтов
     */
    Map<ClickProfile, List<ProfileAccount>> getProfileAccountsMap();

    /**
     * Получение мапы профилей и аккаунтов по id профилей
     */
    Map<ClickProfile, List<ProfileAccount>> getProfileAccountsMapById(List<Long> profilesId);
}
