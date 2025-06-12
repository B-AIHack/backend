package kz.berekebank.bereke_deepmind.repository;

import kz.berekebank.bereke_deepmind.repository.models.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
}
