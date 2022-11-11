package com.tw.capability.gtb.demotddspringboot;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<Task> fetchTasks(@RequestParam(required = false) Boolean completed) {
        return taskService.findTasks(completed);
    }

    public List<Task> findTasks(Boolean completed) {
        return taskService.findTasks(completed);
    }
}
