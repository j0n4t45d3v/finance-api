package br.com.managementfinanceapi.application.port.out.category;

public interface ExistsCategoryRepositoryPort {
  
  boolean byId(Long id);
  boolean byName(String name);
  
}