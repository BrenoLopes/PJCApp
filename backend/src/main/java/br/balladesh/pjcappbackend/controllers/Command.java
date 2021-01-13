package br.balladesh.pjcappbackend.controllers;

import br.balladesh.pjcappbackend.utilities.Result;

public interface Command<T, U extends Exception> {
  Result<T, U> execute();
}
