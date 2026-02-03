package com.jonatas.finance.service;

public interface CreateService<TEntity> {
  TEntity execute (TEntity entity);
}
