package com.example.mswallet.Service;

import com.example.mswallet.Dto.BankIncomeRequestDTO;
import com.example.mswallet.Dto.TransactionResponseDTO;
import com.example.mswallet.Entity.Transaction;
import com.example.mswallet.Entity.UserBankIncome;
import com.example.mswallet.Entity.Wallet;
import com.example.mswallet.Exceptions.ResourceNotFoundException;
import com.example.mswallet.Repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BankIncomeService {

    private final UserBankIncomeRepository incomeRepo;
    private final WalletRepository walletRepo;
    private final TransactionRepository txRepo;
    private final BankRepository bankRepository; // 👈 ahora validamos banco

    @Transactional
    public TransactionResponseDTO addIncomeToWallet(BankIncomeRequestDTO dto) {

        // 0. Validar que el banco exista
        bankRepository.findById(dto.getBankId())
                .orElseThrow(() -> new ResourceNotFoundException("Banco no encontrado"));

        // 1. Registrar ingreso bancario (histórico)
        UserBankIncome income = new UserBankIncome();
        income.setUserId(dto.getUserId());
        income.setBankId(dto.getBankId());
        income.setAmount(dto.getAmount());
        income.setDescription(dto.getDescription());
        income.setCreatedAt(LocalDateTime.now());
        incomeRepo.save(income);

        // 2. Buscar Wallet
        Wallet wallet = walletRepo.findByUserId(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet no encontrada"));

        // 3. Aumentar saldo de la Wallet
        wallet.setBalance(wallet.getBalance().add(dto.getAmount()));
        walletRepo.save(wallet);

        // 4. Crear transacción tipo INCOME
        Transaction tx = new Transaction();
        tx.setUserId(dto.getUserId());
        tx.setWalletId(wallet.getId());
        tx.setAmount(dto.getAmount());
        tx.setType(Transaction.TransactionType.INCOME);
        tx.setDescription("Ingreso desde banco: " + dto.getDescription());
        tx.setTransactionDate(LocalDateTime.now());
        txRepo.save(tx);

        // 5. Respuesta
        TransactionResponseDTO res = new TransactionResponseDTO();
        res.setId(tx.getId());
        res.setUserId(dto.getUserId());
        res.setWalletId(wallet.getId());
        res.setAmount(dto.getAmount());
        res.setType(Transaction.TransactionType.INCOME);
        res.setDescription(tx.getDescription());
        res.setTransactionDate(tx.getTransactionDate());

        return res;
    }
}
