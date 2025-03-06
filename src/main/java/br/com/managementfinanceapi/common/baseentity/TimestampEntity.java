package br.com.managementfinanceapi.common.baseentity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass
public class TimestampEntity {
  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;
  @Column(name = "updated_at", insertable = false)
  private LocalDateTime updatedAt;

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @PreUpdate
  public void onUpdated() {
    this.updatedAt = LocalDateTime.now();
  }
}
