package br.com.petfriends.sales_api.coreapi.base;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import lombok.Getter;

@Getter
public abstract class BaseCommand<T> {

    @TargetAggregateIdentifier
    private final T id;

    public BaseCommand(T id) {
        this.id = id;
    }
}
