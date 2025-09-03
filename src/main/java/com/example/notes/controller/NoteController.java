package com.example.notes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notes.model.Note;
import com.example.notes.repository.NoteRepository;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/notes")
public class NoteController {
  private final NoteRepository repo;
  public NoteController(NoteRepository repo){ this.repo = repo; }

  @GetMapping public List<Note> all(){ return repo.findAll(); }

  @GetMapping("/{id}") public ResponseEntity<Note> get(@PathVariable Long id){
    return repo.findById(id).map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
  }
  @PostMapping public Note create(@RequestBody Note n){ return repo.save(n); }

  @PutMapping("/{id}") public ResponseEntity<Note> update(@PathVariable Long id, @RequestBody Note input){
    return repo.findById(id).map(n -> {
      n.setTitle(input.getTitle());
      n.setContent(input.getContent());
      return ResponseEntity.ok(repo.save(n));
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}") public ResponseEntity<Object> delete(@PathVariable Long id){
    return repo.findById(id).map(n -> { repo.delete(n); return ResponseEntity.noContent().build(); })
      .orElse(ResponseEntity.notFound().build());
  }
  
  @PostMapping("/{id}/share")
  public ResponseEntity<String> share(@PathVariable Long id){
    return repo.findById(id).map(n -> {
      n.generateShareToken();
      repo.save(n);
      return ResponseEntity.ok("/api/share/" + n.getShareToken());
    }).orElse(ResponseEntity.notFound().build());
  }
}
