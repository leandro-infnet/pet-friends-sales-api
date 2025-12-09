package br.com.petfriends.sales_api.query.pedido;

import br.com.petfriends.sales_api.coreapi.events.PedidoCriadoEvent;
import br.com.petfriends.sales_api.coreapi.models.FindAllPedidosQuery;
import br.com.petfriends.sales_api.coreapi.models.FindPedidoByIdQuery;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoProjector {

    private final PedidoRepository repository;

    public PedidoProjector(PedidoRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(PedidoCriadoEvent event) {
        PedidoProjection projection = new PedidoProjection(
                event.getId(),
                event.getClienteId(),
                event.getValorTotal(),
                event.getStatus());
        repository.save(projection);
    }

    @QueryHandler
    public List<PedidoProjection> handle(FindAllPedidosQuery query) {
        return repository.findAll();
    }

    @QueryHandler
    public PedidoProjection handle(FindPedidoByIdQuery query) {
        return repository.findById(query.getId())
                .orElse(null);
    }
}
