package models;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public enum ItemsEnum implements Serializable {
    SUSHI_ROLL(Sets.newHashSet(TaskEnum.MAKE_SUSHI, TaskEnum.PLATING, TaskEnum.CUT_FISH, TaskEnum.KNEED_RICE)),
    SOUP(Sets.newHashSet(TaskEnum.WARM_SOUP, TaskEnum.PLATING)),
    SASHIMI(Sets.newHashSet(TaskEnum.CUT_FISH, TaskEnum.PLATING));

    private final Set<TaskEnum> requiredTasks;
    ItemsEnum(@NotNull Set<TaskEnum> requiredTasks) {
        this.requiredTasks = requiredTasks;
    }

    public Set<TaskEnum> getRequiredTasks() {
        return new HashSet<>(requiredTasks);
    }
}
