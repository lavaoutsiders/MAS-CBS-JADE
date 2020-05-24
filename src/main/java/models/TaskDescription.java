package models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class TaskDescription implements Serializable, Description {

    private final TaskEnum task;

    private final Coordinate coordinate;

    public TaskDescription(@NotNull TaskEnum task, @NotNull Coordinate coordinate) {
        this.task = task;
        this.coordinate = coordinate;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public TaskEnum getTask() {
        return task;
    }

    public String toString() {
        return "task description - task : " + task.toString() + " coordinate " + coordinate.toString();
    }
}
