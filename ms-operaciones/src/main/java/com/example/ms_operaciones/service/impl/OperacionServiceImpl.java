package com.example.ms_operaciones.service.impl;

import com.example.ms_operaciones.dto.ItemRequest;
import com.example.ms_operaciones.dto.NuevoAsientoRequest;
import com.example.ms_operaciones.dto.OperacionSimpleRequest;
import com.example.ms_operaciones.dto.TransactionToWalletDTO;
import com.example.ms_operaciones.dto.VentaRequest;
import com.example.ms_operaciones.entity.Document;
import com.example.ms_operaciones.entity.DocumentDetail;
import com.example.ms_operaciones.entity.OperationType;
import com.example.ms_operaciones.feign.ContabilidadClient;
import com.example.ms_operaciones.feign.WalletFeignClient;
import com.example.ms_operaciones.repository.DocumentRepository;
import com.example.ms_operaciones.service.OperacionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperacionServiceImpl implements OperacionService {

    private final DocumentRepository documentRepository;
    private final ContabilidadClient contabilidadClient;
    private final WalletFeignClient walletFeignClient;

    // ============================================================
    //      OPERACIONES PRINCIPALES
    // ============================================================

    @Override
    public Document registrarVenta(VentaRequest request) {
        return procesarOperacionConItems(request, OperationType.VENTA, "INCOME");
    }

    @Override
    public Document registrarCompra(VentaRequest request) {
        return procesarOperacionConItems(request, OperationType.COMPRA, "EXPENSE");
    }

    @Override
    public Document registrarCobro(OperacionSimpleRequest request) {

        Document doc = Document.builder()
                .tipo(OperationType.COBRO)
                .clienteId(request.getClienteId())
                .subtotal(request.getMonto())
                .igv(BigDecimal.ZERO)
                .total(request.getMonto())
                .fecha(LocalDateTime.now())
                .estado("COBRADO")
                .build();

        Document saved = documentRepository.save(doc);

        enviarTransaccionWallet(request.getClienteId(),
                request.getMonto(),
                "COBRO " + saved.getId(),
                "INCOME");

        contabilidadClient.generarAsiento(new NuevoAsientoRequest(saved));
        return saved;
    }

    @Override
    public Document registrarPago(OperacionSimpleRequest request) {

        Document doc = Document.builder()
                .tipo(OperationType.PAGO)
                .clienteId(request.getClienteId())
                .subtotal(request.getMonto())
                .igv(BigDecimal.ZERO)
                .total(request.getMonto())
                .fecha(LocalDateTime.now())
                .estado("PAGADO")
                .build();

        Document saved = documentRepository.save(doc);

        enviarTransaccionWallet(request.getClienteId(),
                request.getMonto(),
                "PAGO " + saved.getId(),
                "EXPENSE");

        contabilidadClient.generarAsiento(new NuevoAsientoRequest(saved));
        return saved;
    }

    private Document procesarOperacionConItems(VentaRequest request, OperationType tipo, String tipoWallet) {

        BigDecimal subtotal = BigDecimal.ZERO;
        List<DocumentDetail> detalles = new ArrayList<>();

        for (ItemRequest item : request.getItems()) {
            BigDecimal totalItem = item.getPrecioUnitario()
                    .multiply(new BigDecimal(item.getCantidad()));

            subtotal = subtotal.add(totalItem);

            detalles.add(DocumentDetail.builder()
                    .producto(item.getProducto())
                    .cantidad(item.getCantidad())
                    .precioUnitario(item.getPrecioUnitario())
                    .totalItem(totalItem)
                    .build());
        }

        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));
        BigDecimal total = subtotal.add(igv);

        Document documento = Document.builder()
                .tipo(tipo)
                .clienteId(request.getClienteId())
                .subtotal(subtotal)
                .igv(igv)
                .total(total)
                .fecha(LocalDateTime.now())
                .estado("REGISTRADO")
                .build();

        detalles.forEach(d -> d.setDocument(documento));
        documento.setDetalles(detalles);

        Document saved = documentRepository.save(documento);

        enviarTransaccionWallet(saved.getClienteId(),
                total,
                tipo.name() + " DocID: " + saved.getId(),
                tipoWallet);

        contabilidadClient.generarAsiento(new NuevoAsientoRequest(saved));

        return saved;
    }

    private void enviarTransaccionWallet(Long userId, BigDecimal monto, String descripcion, String tipo) {

        TransactionToWalletDTO tx = new TransactionToWalletDTO();
        tx.setUserId(userId);
        tx.setAmount(monto);
        tx.setDescription(descripcion);
        tx.setType(tipo);

        walletFeignClient.registrarDesdeOperacion(tx);
    }

    // ============================================================
    //      LISTADOS COMPLETOS
    // ============================================================

    @Override
    public List<Document> listarTodo() {
        return documentRepository.findAll();
    }

    @Override
    public Document obtenerPorId(Long id) {
        return documentRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Document> listarPorCliente(Long clienteId) {
        return documentRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Document> listarPorTipo(String tipo) {
        return documentRepository.findByTipo(OperationType.valueOf(tipo.toUpperCase()));
    }

    @Override
    public List<Document> listarPorFechas(LocalDate start, LocalDate end) {
        return documentRepository.findByFechaBetween(
                start.atStartOfDay(),
                end.atTime(23, 59, 59)
        );
    }

    @Override
    public List<Document> listarPorClienteYFechas(Long clienteId, LocalDate start, LocalDate end) {
        return documentRepository.findByClienteIdAndFechaBetween(
                clienteId,
                start.atStartOfDay(),
                end.atTime(23, 59, 59)
        );
    }
}