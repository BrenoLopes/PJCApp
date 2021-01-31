package br.balladesh.pjcappbackend.utilities.predicates;

public class AllNull {
  private final boolean allNull;

  private AllNull(boolean allNull) {
    this.allNull = allNull;
  }

  public static AllNull withParams(Object ...o) {
    for (Object _o : o) {
      if (_o == null) continue;

      return new AllNull(false);
    }

    return new AllNull(true);
  }

  public boolean check() {
    return this.allNull;
  }
}
