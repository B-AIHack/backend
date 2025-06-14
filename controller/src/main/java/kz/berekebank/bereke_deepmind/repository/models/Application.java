package kz.berekebank.bereke_deepmind.repository.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kz.berekebank.bereke_deepmind.repository.models.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.List;

@With
@Data
@Entity
@Builder
@Table(name = "application")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Application {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String contractNumber;

  @Enumerated(EnumType.STRING)
  private ApplicationStatus status;

  @OneToMany
  @JoinColumn(name = "application_id")
  private List<UploadedFile> files;

  private String contractDate;

  private String buyer;

  private String seller;

  private String operationType;

  private String contractAmount;

  private String currency;

  private String repatriationTerm;

  private String counterpartyName;

  private String counterpartyCountry;

  private String counterpartyBank;

  private String buyerInn;

  private String sellerInn;

  private String parsingResult;

  private boolean denied;

  private boolean approved;

  private LocalDateTime createdAt;

  private boolean deleted;

}
