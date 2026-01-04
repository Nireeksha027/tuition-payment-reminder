package com.example.tuitionreminder.repository;

import com.example.tuitionreminder.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByPaidFalseAndJoinDateBefore(LocalDate date);
}
