    package com.openclassrooms.mddapi.repositories;

    import com.openclassrooms.mddapi.model.entities.Subject;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.List;

    public interface SubjectRepository extends JpaRepository <Subject, Long> {
        List<Subject> findByUsers_Id(Long user_id);

    }
