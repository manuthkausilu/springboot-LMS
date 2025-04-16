    package com.example.Sekai_Academy.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.CreationTimestamp;

    import java.time.LocalDateTime;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "enrollments")
    public class Enrollment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long enrollmentId;

        @ManyToOne
        @JoinColumn(name = "student_id", nullable = false)
        private User student;

        @ManyToOne
        @JoinColumn(name = "course_id", nullable = false)
        private Course course;

        @CreationTimestamp
        private LocalDateTime  enrollmentDate;

        @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL)
        private Payment payment;


    }
