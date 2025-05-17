package br.com.managementfinanceapi.adapter.common.mapper;

public interface Mapper<E, D> {
  E toEntity (D domain);
  D toDomain (E entity);
}
