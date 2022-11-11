package com.tw.capability.gtb.demotddspringboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    @Test
    void should_return_empty_tasks() {
        when(taskRepository.findAll()).thenReturn(emptyList());

        List<Task> tasks = taskService.findTasks(null);

        assertThat(tasks).isEmpty();
    }

    @Test
    void should_return_multiple_tasks() {
        List<Task> tasks = List.of(
                new Task("task01", true),
                new Task("task02", false));

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> foundTasks = taskService.findTasks(null);

        assertThat(foundTasks).isEqualTo(tasks);
        verify(taskRepository).findAll();
    }

    @Test
    void should_return_to_be_done_tasks_given_completed_false() {
        List<Task> tasks = List.of(
                new Task("task01", true),
                new Task("task02", false));
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> foundTasks = taskService.findTasks(false);


        assertThat(foundTasks).isEqualTo(List.of(tasks.get(1)));
        verify(taskRepository).findAll();
    }

    @Test
    void should_return_to_be_done_tasks_given_completed_true() {
        List<Task> tasks = List.of(
                new Task("task01", true),
                new Task("task02", false));
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> foundTasks = taskService.findTasks(true);


        assertThat(foundTasks).isEqualTo(List.of(tasks.get(0)));
        verify(taskRepository).findAll();
    }
}
