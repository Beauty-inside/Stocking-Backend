# Stocking Backend

## 🔗 Link

 - [Stocking RS](http://13.209.5.25:8080/)

<br>

## 📃 개요

Stocking 프로젝트 Backend 레포지토리

<br>

## 📋 문서

 - __API 인터페이스 정의서__ : [Stocking 인터페이스.xml](https://docs.google.com/spreadsheets/d/182aLbTaK65A3b54N6PWPdKt8OSbjQdrZvL_DBAccawo/edit?usp=sharing)  

 - __ER 다이어그램__ : [Stocking ERD](https://www.erdcloud.com/d/ZQjY97KMQrEthMPyn)  

 - __스토리보드__ : [카카오오븐](https://ovenapp.io/view/DOhZ6TnDKWFjINtQKjnj2RAulxojOZCb#3QyvB)

<br>

## 🔨 구조

```
bis.stock.back
├── domain
│   ├── user
│   │   ├── User.java
│   │   ├── UserController.java
│   │   ├── UserService.java
│   │   └── UserRepository.java
│   ├── stock
│   │   ├── Stock.java
│   │   ├── StockController.java
│   │   ├── StockService.java
│   │   └── StockRepository.java
│   └── ...
│       └── ...
└── global
    ├── config
    │   ├── WebSecurityConfig.java
    │   └── ...
    └── exception
        └── SomeCustomException.java
```

<br>

## 💻 명령어

```bash
$ git pull https://github.com/Beauty-inside/stocking-backend.git

$ ./gradlew
```
