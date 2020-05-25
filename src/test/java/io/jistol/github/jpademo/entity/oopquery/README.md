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

경로 표현식
----
- 점(.)을 통해 객체 그래프를 탐색
- 상태필드 경로 탐색 : 경로 탐색의 끝, 탐색 불가
- 단일값 연관경로 (@ManyToOne, @OneToOne) : 묵시적 조인, 계속 탐색 가능
> 명시적 조인 : SELECT t.name FROM Member m JOIN m.team t
> 묵시적 조인 : SELECT m.team.name FROM Member m
> WHERE 절 : SELECT m FROM Member m WHERE m.team.name = :name
- 컬렉션값 연관경로 (@OneToMany, @ManyToMany) : 묵시적 조인, 탐색 불가, 별칭 획득시 탐색 가능
> SELECT t.members FROM Team t (O)
> SELECT t.members.username FROM Team t (X)
> SELECT m.username FROM Team t JOIN t.members m (O)
> @Deprecated -> 컬렉션 크기를 구할 수 있는 size 기능 존재 (Integer) : SELECT t.members.size FROM Team t


서브 쿼리
----
- WHERE, HAVING절에서 사용 가능 (SELECT/FROM절에서 사용 불가)
- [NOT] EXISTS (subquery)
- {ALL | ANY | SOME} (subquery)
> 비교 연산자와 같이 사용
> SELECT m FROM Member m WHERE m.age > ALL (SELECT t.age FROM Team t)
- [NOT] IN (subquery)

조건식
----
- 문자 : 작은따옴표(')
- 숫자 : L (Long), D (Double), F (Float)
- 날짜 : date {d 'yyyy-mm-dd'}, time {t 'hh-mm-ss'}, datetime {ts 'yyyy-mm-dd hh:mm:ss.f'}
- boolean
- Enum : 패키지명을 포함한 전체이름 사용
- Entity Type : ex) TYPE(m) = Member
- BETWEEN, IN , LIKE, NULL 비교 사용
> LIKE : % (아무값이나 0개이상), _ (아무값이나 1개이상)
- 컬렉션 식 : {collection} IS EMPTY, {value} MEMBER OF {collection}

CASE 식
----
- CASE WHEN <..> THEN <..> ELSE <..>
- CASE <T> WHEN <..> THEN <..> ELSE <..>
- COALESCE : 차례대로 조회하여 null이 아니면 반환
- NULLIF(A, B) : 두 값이 같으면 null, 다르면 첫번째 값  

다형성 쿼리
----
- 상속관계 일 경우 부모를 조회하면 자식도 같이 조회된다
- InheritanceType.SINGLE_TABLE : 단일 테이블로 조회됨
- InheritanceType.JOINED : 자식 테이블이 모두 JOIN되어 조회
- TYPE : 상속 주고에서 조회대상을 특정 자식으로 한정할 때 사용
> SELECT i FROM Item i WHERE TYPE(i) IN (Book, Movie)
- TREAT : 자바의 타입캐스팅과 비슷. 특정 자식 타입을 다룰때 사용
> SELECT i FROM Item i WHERE TREAT(i as Book).author = 'kim'
- 사용자 정의 함수 : 생략

기타 정리
----
- enum은 = 비교연산만 지원
- embedded타입은 비교를 지원하지 않음

Entity 직접사용
----
- entity 객체 사용시 SQL문에서는 기본키 값으로 사용됨
> count(m) == count(m.id)
- 연관객체 조회시 외래키를 사용하면 묵시적 조인이 일어나지 않음
> SELECT m FROM Member m WHERE m.team = :team --> 외래키를 사용하여 조인이 일어나지 않음
> SELECT m FROM Member m WHERE m.team_id = :teamId 와 같다

NamedQuery : 정적쿼리
---- 
- 미리 정의한 쿼리에 이름을 부여하여 사용
- 어플리케이션 로딩 시점에 미리 문법 체크및 파싱
- @NamedQuery / XML에 직접 정의가능
- em.createNamedQuery(...)로 사용

Criteria
----
- JPQL 생성시 쿼리가 아닌 빌더 형식으로 생성
- 컴파일 시점에 오류 발견 가능
- 동적쿼리 작성이 편하다
- 복잡하고 불편

criteria 쿼리 생성절차
----
1. EntityManager / EntityManagerFactory로부터 CriteriaBuilder를 생성
- 빌더는 쿼리(CriteriaQuery)를 생성 할 수 있다.
- 빌더는 조건문(Predicate)를 생성 할 수 있다.

2. 빌더를 통해 쿼리(CriteriaQuery)를 생성
- Type지정 쿼리 : cb.createQuery(T.class)
- 미지정 쿼리 : cb.createQuery() -> Object[]타입
- Tuple타입 쿼리 : cb.createTupleQuery()

3. 조회 (cq:CriteriaQuery, cb:CriteriaBuilder)
- 단건 조회 : cq.select(m) // JPQL : select m 
- 멀티 조회1 : cq.multiselect(m.get("name"), m.get("age")) // JPQL : select name, age
- 멀티 조회2 : cq.select(cb.array(m.get("name"), m.get("age"))) 
- DISTINCT : cq.select(...).distinct(true)
- GROUP BY : cq.groupBy(m.get("team"), m.get("name"))
- HAVING : cq.having(cb.gt(minAge, 10))
- ORDER BY : cb.desc(...), cb.asc(...)
- NEW : cq.select(cb.construct(MemberDTO.class, m.get("name"))) // select new io.jistol.MemberDTO(m.name)
- TUPLE : Map과 비슷한 구조, 이름기반이여서 Object[]보다 안전
```java
    public void tupleSelectTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery(); // eq : cb.createQuery(Tuple.class);
        Root<Member> m = cq.from(Member.class);
        Predicate where = cb.greaterThan(m.<Integer>get("age"), 10);
        cq.multiselect(
                m.get("name").alias("n"),
                m.get("age").alias("a")
        ).where(where);

        List<Tuple> tuples = em.createQuery(cq).getResultList();
        tuples.stream().forEach(tuple -> {
            log.warn("name : {}, age : {}", tuple.get("n", String.class), tuple.get("a", Integer.class));
        });
    }
```
4. 조인
- Root.join : m.join("team", JoinType.INNER) // JoinType.LEFT
- fetch join : m.fetch("team", JoinType.LEFT)
5. 서브쿼리
- 메인쿼리에서 파생 (cq.subquery(T.class))
- 설정후 다시 메인쿼리에서 사용 (cb.select(...).where(cb.equals(subquery, 123)))
- subquery.correlate(...)메서드를 통해 Entity간 연관성 설정간으
6. IN : cb.in(...)
7. CASE 식 
- cb.selectCase() 사용
- when / otherwise로 설정 
8. 파라메터 정의 : 바인드변수 -> cb.parameter(T.class, "...")
9. 네이티브 함수 호출 : cb.function()
10. JPAMetaModelEntityProcessor를 통해 메타모델 API적용가능

QueryDSL
----
- JPQL 빌더, 코드기반에 단순하고 사용하기 쉬움
- 오픈소스로 JPA외에 JDO, 몽고DB, Lucene등 문법도 지원
- com.querydsl 패키지는 4.* 의 기본 패키지로 기존 개발자인 mysema에서 querydsl team이 개발을 진행함에 따라 변경됨
> com.mysema 패키지는 3.* 의 기본 패키지
> [The difference between com.mysema.query and com.querydsl?
](https://stackoverflow.com/questions/32469814/the-difference-between-com-mysema-query-and-com-querydsl)
- AnnotationProcessing 기능 이용하여 Q클래스를 자동으로 생성. 
> 이에 따른 별도 plugin 세팅이 필요하다
> 

JDBC 직접 사용
---
- em.unwrap() 메서드를 통해 JDBC Session을 직접 얻어 사용 가능
- 영속성 컨텍스트와 불일치 이슈 발생 (강제 flush필)

