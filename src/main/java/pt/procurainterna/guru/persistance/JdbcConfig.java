package pt.procurainterna.guru.persistance;

public class JdbcConfig {

  public final String url;
  public final String user;
  public final String password;
  public final String driverClassName;

  public JdbcConfig(String driverClassName, String password, String url, String user) {
    this.driverClassName = driverClassName;
    this.password = password;
    this.url = url;
    this.user = user;
  }

}
