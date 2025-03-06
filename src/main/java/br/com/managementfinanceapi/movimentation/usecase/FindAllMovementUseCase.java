package br.com.managementfinanceapi.movimentation.usecase;

import br.com.managementfinanceapi.movimentation.domain.dtos.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.managementfinanceapi.movimentation.domain.dtos.SearchMovement;
import br.com.managementfinanceapi.movimentation.gateways.FindAllMovementGateway;
import br.com.managementfinanceapi.movimentation.repository.MovementRepository;

@Service
public class FindAllMovementUseCase implements FindAllMovementGateway {

  private static final Logger log = LoggerFactory.getLogger(FindAllMovementUseCase.class);
  private final MovementRepository movementRepository;

  public FindAllMovementUseCase(MovementRepository movementRepository) {
    this.movementRepository = movementRepository;
  }

  @Override
  public Page<TransactionDto> findAll(SearchMovement filters, Pageable page) {
    return this.movementRepository.findAllByUserIdAndDateBetween(
        filters.userId(),
        filters.startDate().atTime(0, 0),
        filters.endDate().atTime(23, 59),
        page
    ).map(TransactionDto::new);
  }

  /* public Page<Transaction> findAllInMonth(MonthFilter filters, Pageable page) {

    return this.movementRepository.findAllByUserIdAndDateBetween(
        filters.userId(),
        filters.startDate(),
        filters.endDate(),
        page
    );
  }

  private LocalDate firstDayInMonth(int month) {
    
  } */
}