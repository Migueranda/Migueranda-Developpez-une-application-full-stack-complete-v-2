    package com.openclassrooms.mddapi.model.entities;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.Date;
    import java.util.List;
    import java.util.Set;

    @Entity
    @Table(name = "SUBJECT")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class Subject {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        private Long id;

        @Column(name = "title_subject")
        private String title;

        @Column(name = "description")
        private String description;

        @Temporal(TemporalType.DATE)
        private Date date;

        @ManyToMany(mappedBy = "subjects")
        @JsonIgnore
        private Set<UserEntity> users;

        @OneToMany(mappedBy = "theme")
        private List<PostEntity> posts;
    }
