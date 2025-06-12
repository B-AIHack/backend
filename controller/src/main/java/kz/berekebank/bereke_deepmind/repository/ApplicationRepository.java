package kz.berekebank.bereke_deepmind.repository;

import kz.berekebank.bereke_deepmind.repository.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {

  @Query("""
    select exists (select 1 from Application where contractNumber = :contractNumber)
    """)
  boolean existsByContractNumber(String contractNumber);

}
