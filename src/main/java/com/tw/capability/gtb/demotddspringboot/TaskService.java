package com.tw.capability.gtb.demotddspringboot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    @Autowired
    private final TaskRepository taskRepository;

    public List<Task> findTasks(Boolean completed) {
        List<Task> tasks = taskRepository.findAll();
        if (Objects.isNull(completed)) {
            return tasks;
        }
        if (Boolean.TRUE.equals(completed)) {
            return tasks.stream().filter(Task::getCompleted).collect(Collectors.toList());
        }
        return tasks.stream().filter(task -> !task.getCompleted()).collect(Collectors.toList());
    }
}
