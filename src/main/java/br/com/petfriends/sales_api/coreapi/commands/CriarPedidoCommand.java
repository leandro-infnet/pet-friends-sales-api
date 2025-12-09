package br.com.petfriends.sales_api.coreapi.commands;

import br.com.petfriends.sales_api.coreapi.base.BaseCommand;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class CriarPedidoCommand extends BaseCommand<String> {

    private final String clienteId;
    private final BigDecimal valorTotal;

    public CriarPedidoCommand(String id, String clienteId, BigDecimal valorTotal) {
        super(id);
        this.clienteId = clienteId;
        this.valorTotal = valorTotal;
    }
}
