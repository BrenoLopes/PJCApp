package br.balladesh.pjcappbackend.utilities;

public class Result<T, U extends Exception> {
  private T data;
  private U exception;

  private Result(T data, U exception) {
    this.data = data;
    this.exception = exception;
  }

  private Result(U exception) {
    this.exception = exception;
  }

  public static<T, U extends Exception> Result<T, U> fromError(U exception) {
    return new Result<>(null, exception);
  }

  public static<T, U extends Exception> Result<T, Exception> fromError(String message) {
    return new Result<>(null, new Exception(message));
  }

  public static<T, U extends Exception> Result<T, U> from(T data) {
    return new Result<>(data, null);
  }

  public boolean haveData() {
    return this.data != null;
  }

  public T getData() {
    return data;
  }

  public U getException() {
    return exception;
  }
}
