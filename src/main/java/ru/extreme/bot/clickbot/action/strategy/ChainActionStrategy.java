package ru.extreme.bot.clickbot.action.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.extreme.bot.clickbot.action.ChainAction;
import ru.extreme.bot.clickbot.action.actionenum.ChainActionCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация стратегии для обработки цепных команд
 */
@Component
public class ChainActionStrategy {
    private final Map<String, ChainAction> actionStrategyMap = new HashMap<>();

    @Autowired
    private void fillStrategyMap(List<ChainAction> strategies) {
        for (ChainAction strategy : strategies) {
            actionStrategyMap.put(strategy.getCode(), strategy);
        }
    }

    public ChainAction getStrategy(ChainActionCode strategyCode) {
        return actionStrategyMap.getOrDefault(strategyCode.getCode(), null);
    }
}
