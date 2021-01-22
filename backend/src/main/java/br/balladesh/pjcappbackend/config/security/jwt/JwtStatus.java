package br.balladesh.pjcappbackend.config.security.jwt;

public enum JwtStatus {
  VALID,
  CORRUPTED,
  EXPIRED,
  NOT_PRESENT
}
