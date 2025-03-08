package br.com.managementfinanceapi.movimentation.usecase;

import br.com.managementfinanceapi.category.domain.Category;
import br.com.managementfinanceapi.movimentation.gateways.AddTransactionGateway;
import br.com.managementfinanceapi.movimentation.domain.Transaction;
import br.com.managementfinanceapi.movimentation.domain.dtos.AddTransaction;
import br.com.managementfinanceapi.movimentation.repository.TransactionRepository;
import br.com.managementfinanceapi.user.domain.User;
import br.com.managementfinanceapi.user.domain.dto.UserResponse;
import br.com.managementfinanceapi.user.gateways.FindOneUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AddTransactionUseCase implements AddTransactionGateway {

  private static final Logger log = LoggerFactory.getLogger(AddTransactionUseCase.class);
  private final TransactionRepository transactionRepository;
  private final FindOneUser findOneUser;

  public AddTransactionUseCase(TransactionRepository transactionRepository, FindOneUser findOneUser) {
    this.transactionRepository = transactionRepository;
    this.findOneUser = findOneUser;
  }

  @Override
  public void add(AddTransaction body) {
    log.info("Adding movement: {}", body);
    UserResponse userResponse = this.findOneUser.byId(body.userId());
    Category category = new Category();
    category.setId(body.categoryId());
    Transaction transaction = new Transaction(body, new User(userResponse), category);
    this.transactionRepository.save(transaction);
  }
}