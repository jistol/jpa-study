# 10. 객체지향 쿼리 언어 #
JPA 공식지원
----
- JPQL 
- Criteria : JPQL을 편하게 작성하는 빌더 클래스
- Native SQL : 직접 SQL문 작성

JPA 비공식 지원
----
- QueryDSL : Criteria와 비슷, 비공식 오픈소스 프레임워크


JPQL
----
- 테이블이 아닌 객체를 대상으로 검색하는 객체지향 쿼리
- SQL추상화, 의존성 없음
- JPQL분석 -> SQL문생성 -> DB조회 -> Entity객체 생성및 반환

JPQL문법
----
- 쿼리에 table/class명이 아닌 Entity명을 사용
- SELECT / DELETE / UPDATE 만 존재 (INSERT는 persist() 메서드 이용)
- Entity와 속성은 대소문자 구분
- FROM / SELECT 등 키워드는 대소문자 미구분
- AS를 이용하여 별칭(식별변수:Identification variable)을 필수적으로 사용해야함
- hibernate사용시 JPQL의 확장인 HQL을 지원

반환객체
----
- TypeQuery : 반환객체 타입이 명확할 경우 (ex: SELECT m FROM Member AS m)
- Query : 반환객체 타입이 불명확할 경우 (ex : SELECT m.name, m.age FROM Member AS m)
- Query 객체는 SELECT 구문의 반환 객체가 1개일 경우 `Object`를, 2개이상 일 경우 `Object[]`를 반환
- getResultList() : 결과 반환, 결과가 없을 경우 빈 컬렉션 반환
- getSingleResult() : 결과가 정확히 하나일 경우, 없거나 하나 이상일 경우 예외발생

파라메터 바인딩
----
- 이름기준 바인딩 : query.setParameter("username", username); -> SELECT m FROM Member m WHERE m.username = :username
- 위치기준 바인딩 : query.setParameter(1, username); -> SELECT m FROM Member m WHERE m.username = ?1

프로젝션
----
- 조회할 대상을 지정하는 것
- Entity 프로젝션 : 결과 값은 entity로 받음 (SELECT m FROM Member m / SELECT m.team FROM Member m)
- Embedded Type 프로젝션 : 결과 값은 임베디드 타입으로 받음 
> 임베디드 타입은 entity가 아니기 때문에 직접 조회 불가. Entity를 조회하여 하위 값인 임베디드 객체를 받아야 한다
> ex : SELECT a FROM Address a (X) -> SELECT m.address FROM Member m (O)
- Scala Type 프로젝션 : 결과값이 숫자/문자/날짜 같은 기본 데이터 타입, 통계쿼리도 스칼라타입을 이용
- 여러값 조회 : 꼭 필요한 데이터들만 선택하여 조회. TypeQuery를 사용할수 없고 Query로 사용한다.
- new 명령어 : 선택한 데이터를 새로운 객체로 변환하여 받는 방식
> 패키지 명을 포함한 전체 클래스 명을 입력
> 순서와 타입이 일치하는 생성자 필요
> ex : SELECT new io.jistol.dto.UserDTO(m.name, m.age) FROM Member m

페이징API
----
- setFirstResult : 조회 시작 위치 (0부터 시작)
- setMaxResults : 조회할 데이터 수

집합 / 정렬
----
- COUNT, MAX, MIN, AVG, SUM
- NULL값은 무시되어 통계에 잡히지 않음
- 값이 없을 경우 결과 값은 NULL. COUNT의 경우 0.
- DISTINCT를 이용한 중복 제거 가능. 임베디드타입은 미지원. ex: SELECT COUNT( DISTINCT m.age ) FROM Member m
- 통계쿼리 사용가능 : GROUP BY, HAVING
- 정렬 사용 가능 : ORDER BY (ASC / DESC)

조인
----
- INNER JOIN의 경우 INNER 생략가능 (다대일)
> ex) SELECT t FROM Member m JOIN m.team t (O), SELECT t FROM Member m JOIN Team t (X)
- LEFT OUTER JOIN의 경우 OUTER 생략가능
- 컬렉션 조인 : 일대다/다대다 관계로 조인조건에 컬렉션 값 연관필드를 사용
> ex) SELECT t, m FROM Team t LEFT JOIN t.members m

페치조인
----
- 연관 entity나 컬렉션을 한번에 같이 조회 (JOIN FETCH)
- SQL 호출 횟수를 줄여줌
- 엔티티에 직접 적용한 로딩 전략(글로벌 로딩전략)보다 페치조인을 우선한다
- entity에서는 지연로딩을 사용하고 최적화 필요시 페치조인을 적용하는것이 효과적
- 페치조인은 별칭부여 불가, SELECT/WHERE/SubQuery절에 사용불가 (hibernate는 별칭을 허용함)
- 둘 이상의 컬렉션을 페치 할 수 없음(카테시안 곱이 만들어짐)
- 지연로딩이 일어나지 않음
> ex) SELECT m FROM Member m JOIN FETCH m.team
- 컬렉션 페치 조인 : 일대다/다대다 관계로 페치조인. 결과가 증가 할 수 있다.
> 페이징 API 사용불가
> hibernate의 경우 경고노출후 메모리에서 페이징한다
- 페치조인 DISTINCT시 SQL에 의한 DB단 중복제거와 어플리케이션단 객체 매핑간 중복제거가 실행된다
> ex) SELECT DISTINCT t FROM Team t JOIN FETCH t.members
> SQL은 t.*, m.* 로 조회하여 중복 제거를 안한다
> 어플리케이션은 t를 Team에 할당하는 과정에서 중복제거한다
- 객체그래프를 유지할때 효과적

Criteria
----
- JPQL 생성시 쿼리가 아닌 빌더 형식으로 생성
- 컴파일 시점에 오류 발견 가능
- 동적쿼리 작성이 편하다
- 복잡하고 불편

QueryDSL
----
- JPQL 빌더, 코드기반에 단순하고 사용하기 쉬움
- 오픈소스로 JPA외에 JDO, 몽고DB, Lucene등 문법도 지원

JDBC 직접 사용
---
- em.unwrap() 메서드를 통해 JDBC Session을 직접 얻어 사용 가능
- 영속성 컨텍스트와 불일치 이슈 발생 (강제 flush필)

