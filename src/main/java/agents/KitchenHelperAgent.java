package agents;

import com.google.common.collect.Sets;
import main.MainController;
import main.MainControllerImpl;
import models.Coordinate;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;

public class KitchenHelperAgent extends BaseAgent {
    public KitchenHelperAgent(@NotNull MainController mainController, @NotNull Coordinate coordinate) {
        super(mainController, Sets.newHashSet(TaskEnum.WASH_DISH),
                coordinate);
    }
}
