package io.github.robertograham.departureapi.client.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.Set;

@Builder(builderMethodName = "newBuilder", builderClassName = "Builder")
@Value
public class TypeSet {

    @Singular
    @NonNull
    Set<Type> types;
}
