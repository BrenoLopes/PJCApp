package br.balladesh.pjcappbackend.utilities.factories;

import br.balladesh.pjcappbackend.utilities.Result;

public interface Factory<T, U extends Exception> {
  Result<T, U> create();
}
