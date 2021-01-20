package br.balladesh.pjcappbackend.utilities.predicates;

public class NonNull {
  private final boolean hasNull;

  private NonNull(boolean hasNull) {
    this.hasNull = hasNull;
  }

  public static NonNull withParams(Object ...o) {
    for (Object _o : o) {
      if (_o != null) continue;
      return new NonNull(true);
    }

    return new NonNull(false);
  }

  public boolean check() {
    return this.hasNull;
  }
}
