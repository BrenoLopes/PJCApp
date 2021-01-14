package br.balladesh.pjcappbackend.entity.security;

import javax.persistence.*;

@Entity(name="users")
public class UserEntity {
  protected UserEntity() {}

  public UserEntity(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    String str = "{"
        + "id=%d,"
        + "name=\"%s\","
        + "email=\"%s\","
        + "password=\"%s\","
        + "}";

    return String.format(str, this.id, this.name, this.email, this.password);
  }
}
