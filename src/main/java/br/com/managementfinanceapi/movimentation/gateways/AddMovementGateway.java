package br.com.managementfinanceapi.movimentation.gateways;

import br.com.managementfinanceapi.movimentation.domain.dtos.AddMovement;

public interface AddMovementGateway {
  void add(AddMovement body);
}
