package br.com.managementfinanceapi.movimentation.usecase;

import br.com.managementfinanceapi.category.domain.Category;
import br.com.managementfinanceapi.movimentation.gateways.AddMovementGateway;
import br.com.managementfinanceapi.movimentation.domain.Transaction;
import br.com.managementfinanceapi.movimentation.domain.dtos.AddMovement;
import br.com.managementfinanceapi.movimentation.repository.MovementRepository;
import br.com.managementfinanceapi.user.domain.User;
import br.com.managementfinanceapi.user.domain.dto.UserResponse;
import br.com.managementfinanceapi.user.gateways.FindOneUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AddMovementUseCase implements AddMovementGateway {

  private static final Logger log = LoggerFactory.getLogger(AddMovementUseCase.class);
  private final MovementRepository movementRepository;
  private final FindOneUser findOneUser;

  public AddMovementUseCase(MovementRepository movementRepository, FindOneUser findOneUser) {
    this.movementRepository = movementRepository;
    this.findOneUser = findOneUser;
  }

  @Override
  public void add(AddMovement body) {
    log.info("Adding movement: {}", body);
    UserResponse userResponse = this.findOneUser.byId(body.userId());
    Category category = new Category();
    category.setId(body.categoryId());
    Transaction transaction = new Transaction(body, new User(userResponse), category);
    this.movementRepository.save(transaction);
  }
}