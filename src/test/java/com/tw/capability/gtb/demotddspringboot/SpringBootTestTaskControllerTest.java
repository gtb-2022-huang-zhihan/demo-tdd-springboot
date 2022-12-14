package com.tw.capability.gtb.demotddspringboot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureJsonTesters
class SpringBootTestTaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JacksonTester<List<Task>> tasksJson;
    @Autowired
    private TaskRepository taskRepository;


    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
    }

    @Test
    void should_return_empty_tasks() {
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/tasks", List.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getBody()).isEmpty();
    }

    @Test
    void should_return_multiple_tasks() throws IOException {
        List<Task> tasks = List.of(
                new Task("task01", true),
                new Task("task02", false));
        taskRepository.saveAll(tasks);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/tasks", String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        String fetchedTasks = responseEntity.getBody();
        assertThat(tasksJson.parseObject(fetchedTasks)).isEqualTo(tasks);
    }

    @Test
    void should_return_to_be_done_tasks_given_completed_false() throws IOException {
        Task toBeDoneTask = new Task("task01", false);
        taskRepository.save(toBeDoneTask);
        Task completedTask = new Task("task02", true);
        taskRepository.save(completedTask);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/tasks?completed=false", String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        List<Task> fetchedTasks = tasksJson.parseObject(responseEntity.getBody());
        assertThat(fetchedTasks).hasSize(1);
        assertThat(fetchedTasks.get(0).getName()).isEqualTo(toBeDoneTask.getName());
        assertThat(fetchedTasks.get(0).getCompleted()).isEqualTo(toBeDoneTask.getCompleted());
    }

    @Test
    void should_return_to_be_done_tasks_given_completed_true() throws IOException {
        Task toBeDoneTask = new Task("task01", false);
        taskRepository.save(toBeDoneTask);
        Task completedTask = new Task("task02", true);
        taskRepository.save(completedTask);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/tasks?completed=true", String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        List<Task> fetchedTasks = tasksJson.parseObject(responseEntity.getBody());
        assertThat(fetchedTasks).hasSize(1);
        assertThat(fetchedTasks.get(0).getName()).isEqualTo(completedTask.getName());
        assertThat(fetchedTasks.get(0).getCompleted()).isEqualTo(completedTask.getCompleted());
    }

    @Test
    void should_return_created_task_when_add_task() {
        Task task = new Task("task01", false);

        ResponseEntity<Task> responseEntity = restTemplate.postForEntity("/tasks", task, Task.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        Task createdTask = responseEntity.getBody();
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getId()).isPositive();
        assertThat(createdTask.getName()).isEqualTo(task.getName());
        assertThat(createdTask.getCompleted()).isEqualTo(task.getCompleted());
    }

    @Test
    void should_return_bad_request_given_completed_is_null_when_add_task() {
        Task task = new Task("task01", null);
        ResponseEntity<ErrorResult> responseEntity = restTemplate.postForEntity("/tasks", task, ErrorResult.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("completed: ?????????null");
    }
}
