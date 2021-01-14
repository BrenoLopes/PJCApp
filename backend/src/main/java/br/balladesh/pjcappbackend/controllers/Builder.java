package br.balladesh.pjcappbackend.controllers;

import br.balladesh.pjcappbackend.utilities.Result;

public interface Builder<T, U extends Exception> {
  Result<T, U> build();
}
