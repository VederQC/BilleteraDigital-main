package com.example.ms_operaciones.service.impl;



import com.example.ms_operaciones.dto.ItemRequest;
import com.example.ms_operaciones.dto.NuevoAsientoRequest;
import com.example.ms_operaciones.dto.VentaRequest;
import com.example.ms_operaciones.entity.Document;
import com.example.ms_operaciones.entity.DocumentDetail;
import com.example.ms_operaciones.entity.OperationType;
import com.example.ms_operaciones.feign.ContabilidadClient;
import com.example.ms_operaciones.repository.DocumentRepository;
import com.example.ms_operaciones.service.OperacionService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperacionServiceImpl implements OperacionService {

    private final DocumentRepository documentRepository;
    private final ContabilidadClient contabilidadClient;

    @Override
    public Document registrarVenta(VentaRequest request) {

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
                .tipo(OperationType.VENTA)
                .clienteId(request.getClienteId())
                .subtotal(subtotal)
                .igv(igv)
                .total(total)
                .fecha(LocalDateTime.now())
                .estado("EMITIDO")
                .build();

        detalles.forEach(d -> d.setDocument(documento));
        documento.setDetalles(detalles);

        Document saved = documentRepository.save(documento);

        contabilidadClient.generarAsiento(
                new NuevoAsientoRequest(saved)
        );

        return saved;
    }
}
