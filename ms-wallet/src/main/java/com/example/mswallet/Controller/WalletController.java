package com.example.mswallet.Controller;

import com.example.mswallet.Dto.WalletRequestDTO;
import com.example.mswallet.Dto.WalletResponseDTO;
import com.example.mswallet.Service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@RequestBody WalletRequestDTO request) {
        WalletResponseDTO response = walletService.createWallet(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseDTO> getWalletById(@PathVariable Long id) {
        WalletResponseDTO response = walletService.getWalletById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WalletResponseDTO> getWalletByUserId(@PathVariable Long userId) {
        WalletResponseDTO response = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long userId) {
        walletService.deleteWallet(userId);
        return ResponseEntity.noContent().build();
    }
}
