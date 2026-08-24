package com.jonatas.finance.common;

public interface CreateService<TEntity> {
  TEntity execute(TEntity entity);
}
