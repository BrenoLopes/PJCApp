package br.balladesh.pjcappbackend.utilities.defaults;

public class Defaults {
  public static final String DEFAULT_BOOLEAN = "" + false;
  public static final String DEFAULT_LONG    = "" + Long.MIN_VALUE;
  public static final String DEFAULT_INT     = "" + Integer.MIN_VALUE;
  public static final String DEFAULT_STR     = "";

  public static long getDefaultLong() {
    return Long.parseLong(DEFAULT_LONG);
  }

  public static int getDefaultInt() {
    return Integer.parseInt(DEFAULT_INT);
  }

  public static boolean getDefaultBoolean() {
    return Boolean.parseBoolean(DEFAULT_BOOLEAN);
  }
}
