package com.oversoul.repository;

import com.oversoul.entity.books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<books, Long>{
}
