package br.com.managementfinanceapi.adapter.out.mapper;

public interface Mapper<E, D> {
  E toEntity (D domain);
  D toDomain (E entity);
}