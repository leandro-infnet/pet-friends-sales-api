package br.com.petfriends.sales_api.controllers;

import br.com.petfriends.sales_api.command.services.PedidoCommandService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
public class OrderCommandController {

    private final PedidoCommandService commandService;

    public OrderCommandController(PedidoCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<String>> criarPedido(@RequestBody PedidoDTO dto) {
        return commandService.criarPedido(dto.getClienteId(), dto.getValorTotal())
                .thenApply(id -> ResponseEntity.status(HttpStatus.CREATED).body(id));
    }

    @Data
    static class PedidoDTO {
        private String clienteId;
        private BigDecimal valorTotal;
    }
}
