package kz.berekebank.bereke_deepmind.repository.models;

import jakarta.persistence.Column;
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
@Table(name = "uploaded_file")
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String filename;

  private String contentType;

  @Column(name = "data", nullable = false)
  private byte[] data;

  @ManyToOne
  private Application application;

  private boolean deleted;

}
