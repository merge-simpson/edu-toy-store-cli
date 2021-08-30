# 장난감 가게 CLI

## 목적(Purpose)
자바 8 재활 교육겸 활용도 개선 교육을 위한 토이 프로젝트입니다.

희망 진도에 따라 버전 1에서는 데이터베이스를 사용하지 않고 파일 시스템을 사용합니다.

다음 버전에서 개선하며 차이를 보기 위한 대조군으로서 기여하므로, 버전 1에는 다소 불편한 구조도 포함되어 있습니다.

진도상 정보의 과주입을 막기 위함과 다음 버전에서 사용할 방식의 의의에 조금 더 공감할 수 있도록 하기 위한 목적을 가집니다.

따라서 다음 버전은 레포지토리를 새로 생성하여 완성본 자체를 구분하며, 이후 이 레포지토리는 사소한 패치 외에는 변경되지 않습니다.

This project is a toy project for the education of Java 8.

And it starts without database because the students want.

One of the purpose of this version is the roll of the control group against the next version.

So there will be new repository for the next version to distinguish between each version, and there would be no update for this repository except small patch after the development of the next version starts.

## 환경 및 도구 요약
- **Java 8** 또는 그 이상(Oracle/Open JDK 1.8 or higher version)

## 주요 요구사항 요약(Ver. 1)

> **공통**(Common)
- 데이터 보존은 파일 시스템으로 구현한다.
- 정상 종료 시 파일에 저장한다.
- 추가/수정/삭제 시 파일에 반영한다.

> **상품**(Product)
- 상품 ID는 따로 두지 않고 상품 이름을 통해 관리한다.
- 따라서 상품 이름은 다른 상품과 겹쳐서는 안 된다.
  - 등록/수정 시 기존 보존 상품과 이름이 겹치면 예외 발생
- 오직 몇 가지 문법 적용을 목적으로 상품은 다음 관계로 인터페이스와 클래스를 구성한다.(추상 클래스 사용도 제한함. 추후 개선하며 차이 볼 것.)
  - Toy Interface(extends Serializable)
    - Doll implements Toy
    - BlockToy implements Toy
  - Toy 인터페이스는 어떠한 메서드도 포함하지 않도록 제한한다.(제약 조건 for 일부 문법 교육)
- 상품 클래스는 다음 속성과 각 제약 조건으로 구성한다.
  - 상품 이름(name): String
    - null을 허용하지 않는다.
  - 가격(price): int
    - 0 이상이어야 한다.
  - 재고(stock): int
    - 0 이상이어야 한다.
  - 할인율(discount): double
    - 0.0 이상 1.0 이하여야 한다.
- 모든 상품은 Builder 패턴을 적용하여 생성한다.

> **사이트맵**

- **HOME**
  - 상품 등록
  - 상품 조회
    - 전체 목록 출력
    - 상품 검색
  - 상품 수정
  - 상품 삭제
  - 구매
  - 종료
