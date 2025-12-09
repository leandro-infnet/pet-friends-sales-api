package br.com.petfriends.sales_api.coreapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindPedidoByIdQuery {
    private String id;
}
