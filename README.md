```yaml
server:
  error:
    include-binding-errors: always
    include-message: always
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/스키마명?useSSL=false&useUnicode=true&characterEncoding=utf8
    username: 아이디
    password: 패스워드
  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        format_sql: true
    show-sql: true

  mail:
    host: smtp.gmail.com
    port: 587
    username: 아이디
    password: 패스워드
    properties:
      mail.smtp:
        ssl:
          trust: "*"
        auth: true
        starttls:
          enable: true
        connectiontimeout: 5000
        timeout: 3000
        wirtetimeout: 5000
```