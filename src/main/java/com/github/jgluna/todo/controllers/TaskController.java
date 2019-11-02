package com.github.jgluna.todo.controllers;

import com.github.jgluna.todo.database.TaskRepository;
import com.github.jgluna.todo.domain.PaginationBuilder;
import com.github.jgluna.todo.domain.Task;
import com.github.jgluna.todo.domain.TaskApplicationException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  @Autowired private TaskRepository repo;

  @GetMapping(value = "/{id}")
  public Task getTaskById(@PathVariable long id) {
    return repo.findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task " + id + " not found"));
  }

  @GetMapping()
  public List<Task> getAllTasks(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "100") Integer offset,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
    Pageable pagination;
    try {
      pagination =
          new PaginationBuilder()
              .withPage(page)
              .withOffset(offset)
              .sortedBy(sortBy)
              .withSortDirection(sortDirection)
              .build();
    } catch (TaskApplicationException e) {
      throw new ResponseStatusException(e.getStatus(), e.getMessage());
    }
    return repo.findAll(pagination).toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Long createNewTask(@RequestBody Task newTask) {
    Task task = repo.save(newTask);
    return task.getId();
  }

  @DeleteMapping("/{id}")
  public void deleteTask(@PathVariable long id) {
    repo.deleteById(id);
  }
}
