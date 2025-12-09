package br.com.petfriends.sales_api.controllers;

import br.com.petfriends.sales_api.coreapi.models.FindAllPedidosQuery;
import br.com.petfriends.sales_api.coreapi.models.FindPedidoByIdQuery;
import br.com.petfriends.sales_api.query.pedido.PedidoProjection;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
public class OrderQueryController {

    private final QueryGateway queryGateway;

    public OrderQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public CompletableFuture<List<PedidoProjection>> listarTodos() {
        return queryGateway.query(
                new FindAllPedidosQuery(),
                ResponseTypes.multipleInstancesOf(PedidoProjection.class));
    }

    @GetMapping("/{id}")
    public CompletableFuture<PedidoProjection> buscarPorId(@PathVariable String id) {
        return queryGateway.query(
                new FindPedidoByIdQuery(id),
                ResponseTypes.instanceOf(PedidoProjection.class));
    }
}
