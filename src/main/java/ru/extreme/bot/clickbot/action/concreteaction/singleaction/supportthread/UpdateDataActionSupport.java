package ru.extreme.bot.clickbot.action.concreteaction.singleaction.supportthread;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.extreme.bot.clickbot.exception.ServiceException;
import ru.extreme.bot.clickbot.service.click.ClickProfileService;

import static ru.extreme.bot.clickbot.exception.ExceptionVariables.UPDATE_DATA_EXCEPTION;

@Component
@RequiredArgsConstructor
public class UpdateDataActionSupport {
    private final ClickProfileService clickProfileService;

    public void updateAllProfilesData() throws ServiceException {
        try {
            clickProfileService.updateAllProfilesData();
        } catch (Exception e) {
            throw new ServiceException(UPDATE_DATA_EXCEPTION.getCode(), e);
        }
    }
}
