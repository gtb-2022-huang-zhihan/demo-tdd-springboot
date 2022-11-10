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
}
