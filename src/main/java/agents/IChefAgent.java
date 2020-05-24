package agents;

import exceptions.TaskNotDecomposableException;
import models.ItemsEnum;
import models.TaskEnum;

public interface IChefAgent {

    double calculateMainTaskCost(ItemsEnum item);

    double calculateSubTaskCost(TaskEnum task) throws TaskNotDecomposableException;
}
