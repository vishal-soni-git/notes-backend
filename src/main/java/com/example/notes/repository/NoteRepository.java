package com.example.notes.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notes.model.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
  Optional<Note> findByShareToken(String token);
}
