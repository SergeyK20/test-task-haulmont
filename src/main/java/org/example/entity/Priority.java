package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public enum Priority {
    NORMALLY("Нормальный"), CITO("Срочный"), STATIM("Немедленный");

    private String russianNameOfPriority;

    Priority(String russianNameOfPriority) {
        this.russianNameOfPriority = russianNameOfPriority;
    }

    public String getRussianNameOfPriority() {
        return russianNameOfPriority;
    }

    public List<String> getListPriority() {
        List<String> list = new ArrayList<>();
        for (Priority element : Priority.values()) {
            list.add(element.getRussianNameOfPriority());
        }
        return list;
    }

    public Priority getPriorityByStringValue(String nameRussiaPriority) {
        for (Priority priority : Priority.values()) {
            if (priority.getRussianNameOfPriority().equals(nameRussiaPriority)) {
                return priority;
            }
        }
        return null;
    }

}
