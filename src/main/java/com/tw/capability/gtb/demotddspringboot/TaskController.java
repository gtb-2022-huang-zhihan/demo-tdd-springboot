package com.tw.capability.gtb.demotddspringboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private List<Task> tasks = Collections.emptyList();

    @GetMapping
    public List<Task> fetchTasks() {
        return tasks;
    }

    public void save(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void deleteAll() {
        tasks = Collections.emptyList();
    }
}
