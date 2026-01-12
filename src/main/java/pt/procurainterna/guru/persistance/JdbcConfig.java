package pt.procurainterna.guru.persistance;

public class JdbcConfig {

  public transient final String url;
  public transient final String user;
  public transient final String password;
  public transient final String driverClassName;

  public JdbcConfig(String driverClassName, String password, String url, String user) {
    this.driverClassName = driverClassName;
    this.password = password;
    this.url = url;
    this.user = user;
  }

}
