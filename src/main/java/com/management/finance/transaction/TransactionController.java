package com.management.finance.transaction;

import com.management.finance.transaction.dto.AddTransaction;
import com.management.finance.transaction.dto.EditTransaction;
import com.management.finance.transaction.dto.ReturnTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/{userId}")
    public ResponseEntity<Void> addTransaction(@PathVariable Long userId, @RequestBody AddTransaction transactionDTO) {
        transactionService.add(userId, transactionDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReturnTransaction>> getTransactions(@PathVariable Long userId) {
        List<ReturnTransaction> transactions = transactionService.getAll(userId);
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteById(transactionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{transactionId}/{userId}")
    public ResponseEntity<Void> editTransaction(
            @PathVariable Long transactionId,
            @PathVariable Long userId,
            @RequestBody EditTransaction transactionDTO) {
        transactionService.editTransaction(transactionId, userId, transactionDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/report/{userId}")
    public ResponseEntity<byte[]> generateReport(@PathVariable Long userId) {
        byte[] reportData = transactionService.report(userId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=report.pdf")
                .body(reportData);
    }
}
