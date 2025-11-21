package com.example.mswallet.Service;

import com.example.mswallet.Dto.BankIncomeRequestDTO;
import com.example.mswallet.Dto.BankIncomeResponseDTO;
import com.example.mswallet.Dto.BankTransferDTO;
import com.example.mswallet.Dto.TransactionResponseDTO;

import com.example.mswallet.Entity.Transaction;
import com.example.mswallet.Entity.UserBankBalance;
import com.example.mswallet.Entity.UserBankIncome;
import com.example.mswallet.Entity.Wallet;

import com.example.mswallet.Exceptions.ResourceNotFoundException;

import com.example.mswallet.Repository.UserBankBalanceRepository;
import com.example.mswallet.Repository.UserBankIncomeRepository;
import com.example.mswallet.Repository.WalletRepository;
import com.example.mswallet.Repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankServiceLogic {

    private final UserBankBalanceRepository balanceRepo;
    private final UserBankIncomeRepository incomeRepo;
    private final WalletRepository walletRepo;
    private final TransactionRepository txRepo;

    // 1) AGREGAR DINERO AL BANCO
    @Transactional
    public UserBankIncome addIncome(BankIncomeRequestDTO dto) {

        // 1. Registrar historial
        UserBankIncome income = UserBankIncome.builder()
                .userId(dto.getUserId())
                .bankId(dto.getBankId())
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        incomeRepo.save(income);

        // 2. Actualizar SALDO TOTAL del banco
        UserBankBalance balance = balanceRepo.findByUserIdAndBankId(dto.getUserId(), dto.getBankId())
                .orElseGet(() -> UserBankBalance.builder()
                        .userId(dto.getUserId())
                        .bankId(dto.getBankId())
                        .balance(dto.getAmount())
                        .build());

        balance.setBalance(balance.getBalance().add(dto.getAmount()));
        balanceRepo.save(balance);

        return income; // devuelve historial
    }

    // 2) OBTENER HISTORIAL DE UN BANCO
    public List<BankIncomeResponseDTO> getIncomeHistory(Long userId, Long bankId) {
        return incomeRepo.findByUserIdAndBankId(userId, bankId)
                .stream()
                .map(i -> BankIncomeResponseDTO.builder()
                        .id(i.getId())
                        .userId(i.getUserId())
                        .bankId(i.getBankId())
                        .amount(i.getAmount())
                        .description(i.getDescription())
                        .createdAt(i.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 3) TRANSFERIR A WALLET
    @Transactional
    public TransactionResponseDTO transferToWallet(BankTransferDTO dto) {

        // 1. Validar saldo del banco
        UserBankBalance balance = balanceRepo.findByUserIdAndBankId(dto.getUserId(), dto.getBankId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe saldo en este banco"));

        if (balance.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new IllegalStateException("Fondos insuficientes en el banco");
        }

        // 2. Restar del banco
        balance.setBalance(balance.getBalance().subtract(dto.getAmount()));
        balanceRepo.save(balance);

        // 3. Sumar a la wallet
        Wallet wallet = walletRepo.findByUserId(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet no encontrada"));

        wallet.setBalance(wallet.getBalance().add(dto.getAmount()));
        walletRepo.save(wallet);

        // 4. Registrar como transacción INCOME
        Transaction tx = new Transaction();
        tx.setUserId(dto.getUserId());
        tx.setWalletId(wallet.getId());
        tx.setAmount(dto.getAmount());
        tx.setType(Transaction.TransactionType.INCOME);
        tx.setDescription("Transferencia desde banco");
        tx.setTransactionDate(LocalDateTime.now());
        txRepo.save(tx);

        // 5. Respuesta
        TransactionResponseDTO res = new TransactionResponseDTO();
        res.setId(tx.getId());
        res.setUserId(tx.getUserId());
        res.setWalletId(tx.getWalletId());
        res.setAmount(tx.getAmount());
        res.setType(tx.getType());
        res.setDescription(tx.getDescription());
        res.setTransactionDate(tx.getTransactionDate());
        return res;
    }
}
