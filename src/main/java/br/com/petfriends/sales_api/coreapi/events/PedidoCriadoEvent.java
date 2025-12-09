package br.com.petfriends.sales_api.coreapi.events;

import br.com.petfriends.sales_api.coreapi.base.BaseEvent;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class PedidoCriadoEvent extends BaseEvent<String> {

    private final String clienteId;
    private final BigDecimal valorTotal;
    private final String status;

    public PedidoCriadoEvent(String id, String clienteId, BigDecimal valorTotal) {
        super(id);
        this.clienteId = clienteId;
        this.valorTotal = valorTotal;
        this.status = "CRIADO";
    }
}
