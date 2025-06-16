package br.com.managementfinanceapi.infra.bean.category;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.application.core.usecase.category.CreateCategoryUseCase;
import br.com.managementfinanceapi.application.core.usecase.category.SearchCategoryUseCase;
import br.com.managementfinanceapi.application.port.in.category.CreateCategoryPort;
import br.com.managementfinanceapi.application.port.in.category.SearchCategoryPort;
import br.com.managementfinanceapi.application.port.out.category.ExistsCategoryRepositoryPort;
import br.com.managementfinanceapi.application.port.out.category.SaveCategoryRepositoryPort;
import br.com.managementfinanceapi.application.port.out.category.SearchCategoryRepositoryPort;

@Component
public class CategoryBean {

  @Bean
  public CreateCategoryPort createCategory(
      SaveCategoryRepositoryPort saveCategoryRepositoryPort,
      ExistsCategoryRepositoryPort existsCategoryRepositoryPort
  ) {
    return new CreateCategoryUseCase(saveCategoryRepositoryPort, existsCategoryRepositoryPort);
  }

  @Bean
  public SearchCategoryPort searchCategory(
    SearchCategoryRepositoryPort searchCategoryRepositoryPort
  ) {
    return new SearchCategoryUseCase(searchCategoryRepositoryPort);
  }

}