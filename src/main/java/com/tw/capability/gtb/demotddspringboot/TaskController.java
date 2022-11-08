package com.tw.capability.gtb.demotddspringboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @GetMapping
    public List<Task> fetchTasks() {
        return Collections.emptyList();
    }
}
