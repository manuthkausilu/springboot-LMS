package com.example.Sekai_Academy.repo;


import com.example.Sekai_Academy.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepo extends JpaRepository<Event, Long> {

    @Query("SELECT COUNT(e) FROM Event e")
    long countAllEvents();
}

