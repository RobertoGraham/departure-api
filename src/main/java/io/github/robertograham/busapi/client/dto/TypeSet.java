package io.github.robertograham.busapi.client.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.Set;

@Builder(builderClassName = "Builder", builderMethodName = "newBuilder")
@Value
public class TypeSet {

    @Singular
    @NonNull
    Set<Type> types;
}
