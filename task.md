Regra de négocio:

Ao o usuario inserir na primeira vez o saldo o sistema não deve calcular as transações lançadas,
mas se o cliente já tiver um saldo cadastrado ele deve ser alterado sempre que for lançado
uma nova transação, mas detalhe deve se ter um backup do saldo para cada mês,
então para esse melhor rastreio vamos criar uma tabela com os saldos mês-a-mês para
o usuario ter uma melhor rastreabilidade do seu saldo.

Se o usuario ver que o saldo está errado ele pode verificar as transações
e se ele remover ou adicionar ou alterar o valor de alguma transação o sistema
deve recalcular o saldo desde o mês da transação alterada.
 fa