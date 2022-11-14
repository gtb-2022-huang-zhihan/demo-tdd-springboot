package com.tw.capability.gtb.demotddspringboot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        entityManager.clear();
    }

    @Test
    void should_return_empty_list() {
        List<Task> tasks = taskRepository.findAll();

        assertThat(tasks).isEmpty();
    }

    @Test
    void should_return_multiple_tasks() {
        entityManager.persist(new Task("task01", true));
        entityManager.persist(new Task("task02", false));

        List<Task> foundTasks = taskRepository.findAll();

        assertThat(foundTasks).hasSize(2);
        assertThat(foundTasks.get(0).getName()).isEqualTo("task01");
        assertThat(foundTasks.get(0).getCompleted()).isTrue();
        assertThat(foundTasks.get(1).getName()).isEqualTo("task02");
        assertThat(foundTasks.get(1).getCompleted()).isFalse();

        //                .containsOnly(
//                        new Task(1L, "task01", true),
//                        new Task(2L, "task02", false)
//                );

    }

    @Test
    void should_return_saved_task_when_save_task() {
        Task task = new Task("task01", false);
        entityManager.persist(task);

        Task savedTask = taskRepository.save(task);

        assertThat(savedTask).isEqualTo(task);
    }
}
