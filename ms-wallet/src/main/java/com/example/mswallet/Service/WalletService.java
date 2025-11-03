package com.example.mswallet.Service;

import com.example.mswallet.Dto.WalletRequestDTO;
import com.example.mswallet.Dto.WalletResponseDTO;
import com.example.mswallet.Entity.Wallet;
import com.example.mswallet.Exceptions.ResourceNotFoundException;
import com.example.mswallet.Feign.UserFeignClient;
import com.example.mswallet.Repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserFeignClient userFeignClient;

    @Transactional
    public WalletResponseDTO createWallet(WalletRequestDTO request) {
        var user = userFeignClient.getUserById(request.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException("Usuario no encontrado en ms-auth");
        }

        walletRepository.findByUserId(request.getUserId()).ifPresent(w -> {
            throw new IllegalStateException("El usuario ya tiene una wallet creada");
        });

        Wallet wallet = new Wallet();
        wallet.setUserId(request.getUserId());
        wallet.setCurrency(request.getCurrency());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCreatedAt(LocalDateTime.now());

        wallet = walletRepository.save(wallet);

        WalletResponseDTO response = new WalletResponseDTO();
        response.setId(wallet.getId());
        response.setUserId(wallet.getUserId());
        response.setBalance(wallet.getBalance());
        response.setCurrency(wallet.getCurrency());
        response.setCreatedAt(wallet.getCreatedAt());

        return response;
    }
    public WalletResponseDTO getWalletById(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet no encontrada"));
        return mapToResponse(wallet);
    }

    public WalletResponseDTO getWalletByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet no encontrada"));
        WalletResponseDTO response = new WalletResponseDTO();
        response.setId(wallet.getId());
        response.setUserId(wallet.getUserId());
        response.setBalance(wallet.getBalance());
        response.setCurrency(wallet.getCurrency());
        response.setCreatedAt(wallet.getCreatedAt());
        return response;
    }
    @Transactional
    public void deleteWallet(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet no encontrada"));
        walletRepository.delete(wallet);
    }
    private WalletResponseDTO mapToResponse(Wallet wallet) {
        return WalletResponseDTO.builder()
                .id(wallet.getId())
                .userId(wallet.getUserId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .createdAt(wallet.getCreatedAt())
                .build();
    }



}
