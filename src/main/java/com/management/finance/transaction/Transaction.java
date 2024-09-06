package com.management.finance.transaction;

import com.management.finance.transaction.enums.TypeTransaction;
import com.management.finance.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private TypeTransaction type;
    @CreatedDate
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;

    @PrePersist
    public void createdAtValue() {
        this.createdAt = LocalDateTime.now();
    }
}
