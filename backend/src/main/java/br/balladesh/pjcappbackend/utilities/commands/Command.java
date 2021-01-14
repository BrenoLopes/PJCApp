package br.balladesh.pjcappbackend.utilities.commands;

import br.balladesh.pjcappbackend.utilities.Result;

public interface Command<T, U extends Exception> {
  Result<T, U> execute();
}
