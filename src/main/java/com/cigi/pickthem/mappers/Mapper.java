package com.cigi.pickthem.mappers;

public interface Mapper<A, B> {

    B toDto(A a);

    A toEntity(B b);
}
