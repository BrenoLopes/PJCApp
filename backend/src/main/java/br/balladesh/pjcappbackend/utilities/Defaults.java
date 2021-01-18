package br.balladesh.pjcappbackend.utilities;

public class Defaults {
  public static final String DEFAULT_BOOLEAN = "" + false;
  public static final String DEFAULT_LONG    = "" + Long.MIN_VALUE;
  public static final String DEFAULT_INT     = "" + Integer.MIN_VALUE;
  public static final String DEFAULT_STR     = "@Default" + 646890207; // Hash from "@THIS_IS_DEFAULT_STR".hashCode();

  public static long getDefaultLong() {
    return Long.parseLong(DEFAULT_LONG);
  }

  public static int getDefaultInt() {
    return Integer.parseInt(DEFAULT_INT);
  }
}
