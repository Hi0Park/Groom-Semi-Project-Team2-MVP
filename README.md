# Groom-Semi-Project-Team2-MVP

- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 4.1.0
- **Build Tool:** Gradle - Groovy
- **Database:** MySQL
- **JPA & ORM:** Spring Data JPA
- **Testing:** JUnit5

### 프로젝트 패키지 구조

```text
com.shop.buyingmvp (최상위 패키지)
├── global                   # 프로젝트 공통 관리 (노현섭 님 메인 작업 영역)
│   ├── config               # 인프라, 보안, Swagger 등 공통 설정
│   ├── error                # 글로벌 예외 처리 (Exception Handler, ErrorCode)
│   └── common               # 공통 유틸, BaseEntity (생성/수정일자 등)
│
├── domain                   # 비즈니스 도메인별 패키지
│   ├── product              # 상품 도메인 (박소빈, 주정현 님 영역)
│   │   ├── controller       # 상품 API 컨트롤러
│   │   ├── service          # 상품 비즈니스 로직 인터페이스 및 구현체
│   │   ├── repository       # 상품 JPA 레포지토리
│   │   ├── dto              # 상품 관련 Request / Response DTO
│   │   └── entity           # 상품 JPA 엔티티 (Product)
│   │
│   ├── order                # 주문/구매 도메인 (박선우 님 메인 영역)
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── dto
│   │   └── entity           # 주문 엔티티 (Order, OrderItem)
│   │
│   ├── cancel               # 구매 취소 도메인 (김승진 님 메인 영역)
│   │   ├── controller
│   │   ├── service
│   │   └── dto
│   │
│   └── stock                # 재고/입고 히스토리 도메인 (노현섭 님 메인 영역)
│       ├── controller
│       ├── service
│       ├── repository
│       ├── dto
│       └── entity           # 재고 히스토리 엔티티 (StockHistory)
│
└── BuyingMvpApplication.java # 메인 스프링부트 실행 클래스
```