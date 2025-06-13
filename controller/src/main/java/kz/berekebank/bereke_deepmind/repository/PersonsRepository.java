package kz.berekebank.bereke_deepmind.repository;

import kz.berekebank.bereke_deepmind.repository.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonsRepository extends JpaRepository<Person, Long> {

  @Query("""
        SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
        FROM Person p
        WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :fio, '%'))
    """)
  boolean contains(@Param("fio") String fio);
}
