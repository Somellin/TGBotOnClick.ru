package ru.extreme.bot.clickbot.action.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.extreme.bot.clickbot.action.SingleAction;
import ru.extreme.bot.clickbot.action.actionenum.SingleActionCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация стратегии для обработки одиночных команд
 */
@Component
public class SingleActionStrategy {
    private final Map<String, SingleAction> actionStrategyMap = new HashMap<>();

    @Autowired
    private void fillStrategyMap(List<SingleAction> strategies) {
        for (SingleAction strategy : strategies) {
            actionStrategyMap.put(strategy.getCode(), strategy);
        }
    }

    public SingleAction getStrategy(SingleActionCode strategyCode) {
        return actionStrategyMap.getOrDefault(strategyCode.getCode(), null);
    }
}
