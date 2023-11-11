package ru.extreme.bot.clickbot.service.click.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.extreme.bot.clickbot.clickclient.ClickClient;
import ru.extreme.bot.clickbot.enums.EndPoint;
import ru.extreme.bot.clickbot.enums.Services;
import ru.extreme.bot.clickbot.exception.ServiceException;
import ru.extreme.bot.clickbot.model.ClickProfile;
import ru.extreme.bot.clickbot.model.ProfileAccount;
import ru.extreme.bot.clickbot.repository.ClickProfileRepository;
import ru.extreme.bot.clickbot.repository.ProfileAccountRepository;
import ru.extreme.bot.clickbot.service.click.ClickProfileService;
import ru.extreme.bot.clickbot.utils.JsonConverter;
import ru.extreme.bot.clickbot.utils.TokenEncoder;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClickProfileServiceImpl implements ClickProfileService {

    private final ClickProfileRepository clickProfileRepository;

    private final ProfileAccountRepository profileAccountsRepository;

    private final ClickClient client;

    @Override
    public ClickProfile addNewProfile(String token) throws ServiceException {
        String jsonProfile = client.getJsonProfileInstance(EndPoint.USER.getName(), token);
        ClickProfile newProfile = JsonConverter.convertProfileJson(jsonProfile);

        saveUpdateProfileInfo(newProfile, token);

        return newProfile;
    }

    @Override
    public List<ClickProfile> findAllProfiles() {
        return clickProfileRepository.findAll();
    }

    @Override
    public ClickProfile getClickProfileById(Long profileId) {
        return clickProfileRepository.getClickProfileById(profileId);
    }

    @Override
    public String deleteProfileById(Long id) {
        Optional<ClickProfile> chatUser = clickProfileRepository.findById(id);

        String profileName = "Профиль не найден";

        if (chatUser.isPresent()) {
            ClickProfile profile = chatUser.get();
            profileName = profile.getDescription();

            clickProfileRepository.delete(profile);
        }

        return profileName;
    }

    @Override
    public boolean checkProfileExist(String token) throws Exception {
        try {
            String encodedToken = TokenEncoder.encodeToken(token);
            return clickProfileRepository.checkProfileExist(encodedToken);
        } catch (Exception e) {
            throw new Exception("Проверьте правильность токена и попробуйте еще раз.");
        }

    }

    @Override
    public List<ClickProfile> updateAllProfilesData() throws ServiceException, InterruptedException {
        List<String> profilesTokens = getProfilesTokens();

        for (String profileToken : profilesTokens) {
            String jsonProfile = client.getJsonProfileInstance(EndPoint.USER.getName(), profileToken);
            ClickProfile updateProfile = JsonConverter.convertProfileJson(jsonProfile);

            Thread.sleep(2000);
            saveUpdateProfileInfo(updateProfile, profileToken);
        }

        return clickProfileRepository.findAll();
    }

    @Override
    public Map<ClickProfile, List<ProfileAccount>> getProfileAccountsMap() {
        Map<ClickProfile, List<ProfileAccount>> profileAccountsMap = new HashMap<>();
        List<ClickProfile> profiles = clickProfileRepository.findAll();

        for (ClickProfile profile : profiles) {
            List<ProfileAccount> accounts = profileAccountsRepository.findProfileAccountsByProfileId(profile.getId(), Services.mainServices());
            profileAccountsMap.put(profile, accounts);
        }

        return profileAccountsMap;
    }

    @Override
    public Map<ClickProfile, List<ProfileAccount>> getProfileAccountsMapById(List<Long> profilesId) {
        Map<ClickProfile, List<ProfileAccount>> profileAccountsMap = new HashMap<>();
        List<ClickProfile> profiles = clickProfileRepository.findAllProfileInIds(profilesId);

        for (ClickProfile profile : profiles) {
            List<ProfileAccount> accounts = profileAccountsRepository.findProfileAccountsByProfileId(profile.getId(), Services.mainServices());
            profileAccountsMap.put(profile, accounts);
        }

        return profileAccountsMap;
    }

    private void fillAccountsProfileId(List<ProfileAccount> profileAccounts, Long profileId) {
        for (ProfileAccount account : profileAccounts) {
            account.setProfileId(profileId);
        }
    }

    private List<String> getProfilesTokens() {
        Set<String> encodedTokens = clickProfileRepository.findAllProfileEncodedTokens();
        List<String> decodedTokens = new ArrayList<>();

        for (String encodedToken : encodedTokens) {
            if (encodedToken == null) {
                continue;
            }
            decodedTokens.add(TokenEncoder.decodeToken(encodedToken));
        }

        return decodedTokens;
    }

    private synchronized void saveUpdateProfileInfo(ClickProfile profile, String token) throws ServiceException {
        profile.setEncodeToken(TokenEncoder.encodeToken(token));
        clickProfileRepository.save(profile);

        Long profileId = profile.getId();
        String jsonAccounts = client.getJsonAccountsInstance(EndPoint.ACCOUNTS.getName(), token, profileId);
        Collection<? extends ProfileAccount> profileAccounts = JsonConverter.convertAccountsJson(jsonAccounts);

        fillAccountsProfileId(new ArrayList<>(profileAccounts), profileId);

        profileAccountsRepository.saveAll(profileAccounts);
    }

}
