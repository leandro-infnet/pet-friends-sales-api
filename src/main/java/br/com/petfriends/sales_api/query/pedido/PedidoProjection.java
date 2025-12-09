package br.com.petfriends.sales_api.query.pedido;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoProjection {

    @Id
    private String id;
    private String clienteId;
    private BigDecimal valorTotal;
    private String status;
}
