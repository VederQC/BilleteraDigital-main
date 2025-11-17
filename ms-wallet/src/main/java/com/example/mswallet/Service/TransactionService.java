package com.example.mswallet.Service;

import com.example.mswallet.Dto.*;
import com.example.mswallet.Entity.Transaction;
import com.example.mswallet.Entity.Wallet;
import com.example.mswallet.Exceptions.ResourceNotFoundException;
import com.example.mswallet.Feign.CategoryFeignClient;
import com.example.mswallet.Feign.EventFeignClient;
import com.example.mswallet.Feign.GoalFeignClient;
import com.example.mswallet.Repository.TransactionRepository;
import com.example.mswallet.Repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final CategoryFeignClient categoryClient;
    private final EventFeignClient eventClient;
    private final GoalFeignClient goalClient;   // 🟢 NUEVO

    @Autowired
    public TransactionService(
            TransactionRepository transactionRepository,
            WalletRepository walletRepository,
            CategoryFeignClient categoryClient,
            EventFeignClient eventClient,
            GoalFeignClient goalClient   // 🟢 NUEVO
    ) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.categoryClient = categoryClient;
        this.eventClient = eventClient;
        this.goalClient = goalClient;  // 🟢 NUEVO
    }

    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO request) {

        // 1. Verificar Wallet
        Wallet wallet = walletRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet no encontrada"));

        // 2. Categoría y Subcategoría
        // 2. Categoría y Subcategoría (permitir null)
        CategoryDTO category = null;
        SubcategoryDTO subcategory = null;

        if (request.getCategoryId() != null) {
            try {
                category = categoryClient.getCategoryById(request.getCategoryId());
            } catch (Exception e) {
                log.warn("Categoría no encontrada: {}", request.getCategoryId());
            }
        }

        if (request.getCategoryId() != null && request.getSubcategoryId() != null) {
            try {
                subcategory = categoryClient.getSubcategoryById(
                        request.getCategoryId(),
                        request.getSubcategoryId()
                );
            } catch (Exception e) {
                log.warn("Subcategoría no encontrada: {}", request.getSubcategoryId());
            }
        }


        // 3. Evento (opcional)
        EventDTO event = null;
        if (request.getEventId() != null) {
            event = eventClient.getEventById(request.getEventId());
        }

        // 4. Crear Transacción
        Transaction transaction = new Transaction();
        transaction.setWalletId(wallet.getId());
        transaction.setUserId(request.getUserId());
        transaction.setCategoryId(request.getCategoryId());
        transaction.setSubcategoryId(request.getSubcategoryId());
        transaction.setEventId(request.getEventId());
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());

        transaction = transactionRepository.save(transaction);

        // 5. Actualizar balance de Wallet
        if (request.getType() == Transaction.TransactionType.INCOME) {
            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        } else {
            wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        }
        walletRepository.save(wallet);

        // ============================
        //   6.1 APORTAR A UNA META
        // ============================
        if (request.getGoalId() != null && request.getType() == Transaction.TransactionType.EXPENSE) {

            goalClient.updateGoalAmount(
                    request.getGoalId(),
                    request.getAmount()
            );

            log.info("Meta {} actualizada con {}", request.getGoalId(), request.getAmount());
        }

        // 6. Actualizar Evento (si existe)
        // 6. Actualizar Evento (si existe)
        if (request.getEventId() != null && request.getType() == Transaction.TransactionType.EXPENSE) {

            EventUpdateSpentDTO updateSpent = new EventUpdateSpentDTO();

            // CORRECCIÓN IMPORTANTE
            transaction.setEventId(request.getEventId());

            updateSpent.setEventId(request.getEventId());
            updateSpent.setAmount(request.getAmount());

            event = eventClient.updateEventSpent(request.getEventId(), updateSpent);

            log.info("Evento actualizado: {} - Gastado: {}", event.getName(), event.getSpent());
        }


        // 7. Respuesta final
        return buildTransactionResponse(transaction, category, subcategory, event);
    }

    private TransactionResponseDTO buildTransactionResponse(
            Transaction transaction,
            CategoryDTO category,
            SubcategoryDTO subcategory,
            EventDTO event
    ) {
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(transaction.getId());
        response.setWalletId(transaction.getWalletId());
        response.setUserId(transaction.getUserId());
        response.setCategory(category);
        response.setSubcategory(subcategory);
        response.setEvent(event);
        response.setType(transaction.getType());
        response.setAmount(transaction.getAmount());
        response.setDescription(transaction.getDescription());
        response.setTransactionDate(transaction.getTransactionDate());
        return response;
    }

    public List<TransactionResponseDTO> getUserTransactions(Long userId, LocalDate startDate, LocalDate endDate) {

        List<Transaction> transactions;

        if (startDate != null && endDate != null) {
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(23, 59, 59);
            transactions = transactionRepository.findByUserIdAndTransactionDateBetween(userId, start, end);
        } else {
            transactions = transactionRepository.findByUserId(userId);
        }

        return transactions.stream()
                .map(this::enrichTransaction)
                .collect(Collectors.toList());
    }

    private TransactionResponseDTO enrichTransaction(Transaction transaction) {
        CategoryDTO category = categoryClient.getCategoryById(transaction.getCategoryId());
        SubcategoryDTO subcategory = categoryClient.getSubcategoryById(
                transaction.getCategoryId(),
                transaction.getSubcategoryId()
        );

        EventDTO event = null;
        if (transaction.getEventId() != null) {
            try {
                event = eventClient.getEventById(transaction.getEventId());
            } catch (Exception e) {
                log.warn("No se pudo obtener evento {}: {}", transaction.getEventId(), e.getMessage());
            }
        }

        return buildTransactionResponse(transaction, category, subcategory, event);
    }
}
