package br.balladesh.pjcappbackend.utilities.predicates;

public class HasNull {
  private final boolean hasNull;

  private HasNull(boolean hasNull) {
    this.hasNull = hasNull;
  }

  public static HasNull withParams(Object ...o) {
    for (Object _o : o) {
      if (_o != null) continue;
      return new HasNull(true);
    }

    return new HasNull(false);
  }

  public boolean check() {
    return this.hasNull;
  }
}
