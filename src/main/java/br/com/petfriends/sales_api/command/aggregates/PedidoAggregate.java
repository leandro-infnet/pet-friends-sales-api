package br.com.petfriends.sales_api.command.aggregates;

import br.com.petfriends.sales_api.coreapi.commands.CriarPedidoCommand;
import br.com.petfriends.sales_api.coreapi.events.PedidoCriadoEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate
public class PedidoAggregate {

    @AggregateIdentifier
    private String id;

    private BigDecimal valorTotal;
    private String status;

    public PedidoAggregate() {
    }

    @CommandHandler
    public PedidoAggregate(CriarPedidoCommand command) {
        // Validação de Invariante: Não existe pedido grátis.
        if (command.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pedido deve ser maior que zero.");
        }

        AggregateLifecycle.apply(new PedidoCriadoEvent(
                command.getId(),
                command.getClienteId(),
                command.getValorTotal()));
    }

    @EventSourcingHandler
    public void on(PedidoCriadoEvent event) {
        this.id = event.getId();
        this.valorTotal = event.getValorTotal();
        this.status = event.getStatus();
    }
}
