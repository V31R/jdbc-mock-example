# JDBC mock driver example
***
[Драйвер](https://github.com/V31R/jdbc-mock-driver)
***
## Прямое использование драйвера через java.sql
Подключаем зависимость:
Для `Maven` в `pom.xml`:
```
<dependency>
    <groupId>org.mock.jdbc</groupId>
    <artifactId>HttpDriver</artifactId>
    <version>1.0</version>
</dependency>
```

Берём `url`, к которому будут идти `http` запросы. К примеру:
```
jdbc:wm://localhost:8080/sql-mock
```
И это `url` необходимо передать `DriverManager` для получения экземпляра класса `Connection`, получаем:
```
Connection connection = DriverManager.getConnection("jdbc:wm://localhost:8080/sql-mock");
```
После чего можно получить экземпляры `Statement` или `PreparedStatement` для дальнейшей работы.
К примеру:
```
Connection connection = DriverManager.getConnection("jdbc:wm://localhost:8080/sql-mock");
Statement statement = connection.createStatement();
ResultSet resultSet = statement.executeQuery("select * from table");
```
***
## Использование с Spring Boot

Указвываем в `application-XXX.properties` файле это:
```
spring.datasource.url=jdbc:wm://localhost:8080/sql-mock
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=org.example.HttpDriver

spring.jpa.database-platform = org.hibernate.dialect.MySQL8Dialect
```
И можно использовать `JPA` и `CRUD` репозитории. 

Примечание: `org.hibernate.dialect` может быть любым.
***