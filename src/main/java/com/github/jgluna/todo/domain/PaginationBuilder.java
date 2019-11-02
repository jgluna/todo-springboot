package com.github.jgluna.todo.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

public class PaginationBuilder {
  public enum SORT_DIRECTION {
    asc,
    desc
  }

  private Integer page;
  private Integer offset;
  private String sortedByField;
  private SORT_DIRECTION sortDirection;

  public PaginationBuilder withPage(Integer page) {
    this.page = page;
    return this;
  }

  public PaginationBuilder withOffset(Integer offset) {
    this.offset = offset;
    return this;
  }

  public PaginationBuilder sortedBy(String sortedByField) {
    this.sortedByField = sortedByField;
    return this;
  }

  public PaginationBuilder withSortDirection(String direction) throws TaskApplicationException {
    try {
      this.sortDirection = SORT_DIRECTION.valueOf(direction);
    } catch (Exception ex) {
      throw new TaskApplicationException(
          HttpStatus.BAD_REQUEST, "sortDirection must be either asc or desc, defaults to asc");
    }
    return this;
  }

  public Pageable build() {
    Sort sotConfig = Sort.unsorted();
    if (this.sortedByField != null) {
      switch (this.sortDirection) {
        case asc:
          sotConfig = Sort.by(sortedByField).ascending();
          break;
        case desc:
          sotConfig = Sort.by(sortedByField).descending();
          break;
      }
    }
    return PageRequest.of(page, offset, sotConfig);
  }
}
