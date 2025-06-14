package kz.berekebank.bereke_deepmind.repository;

import kz.berekebank.bereke_deepmind.repository.models.Application;
import kz.berekebank.bereke_deepmind.repository.models.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {

  @Query("""
    select exists (select 1 from Application where contractNumber = :contractNumber)
    """)
  boolean existsByContractNumber(String contractNumber);

  @Query("""
    select a from Application a where a.status = 'PROCESSING' and a.sellerInn is not null
    """)
  List<Application> findAllProcessingAndBinNotNull();

  @Query("""
    select a from Application a where (a.status = 'NEW' or a.status ='PROCESSING') and a.seller is null
    """)
  List<Application> findAllNewOrProcessingAndBinIsNull();

  @Query("""
    select a from Application a where a.status = :status
    """)
  List<Application> findAllByStatus(ApplicationStatus status);
}
