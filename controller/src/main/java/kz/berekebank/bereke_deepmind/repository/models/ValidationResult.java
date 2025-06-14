package kz.berekebank.bereke_deepmind.repository.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Data
@Entity
@Table(name = "validation_result")
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String result;

  @ManyToOne
  private Application application;

}
