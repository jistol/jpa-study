# 7장. 고급맵핑 #

상속 관계 매핑
----
- 객체의 상속관계를 DB에서 표현하는 방법
- DB에는 상속관계가 없음. Super - Sub 타입관계모델
- 조인전략/단일테이블전략/구현클래스별 테이블전략 존재

조인전략 (InheritanceType.JOINED)
----
- 각 Entity를 테이블로 만들고 부모객체의 PK를 받아 PK+FK로 사용
- 조회시 조인하여 사용 되며 구분컬럼(DTYPE)으로 구분
- JPA표준명세에는 구분컬럼을 사용하도록 되어 있으나 hibernate는 구분컬럼 없이도 동작 (FlatXXX 객체 참고)
- 부모객체 컬럼명을 기본키로 사용하나 @PrimaryKeyJoinColumn으로 컬럼명 변경가능
- 장점 : 정규화/외래키 참조 무결성/효율적인 저장공간사용
- 단점 : 조인으로 인한 성능저하 / 조회 쿼리 복잡 / INSERT SQL을 두번 실행

[/src/test/java/io/jistol/github/jpademo/entity/advancemapping/join](/src/test/java/io/jistol/github/jpademo/entity/advancemapping/join)

### 구분컬럼을 포함하여 동작 ###
```text
Hibernate: create sequence hibernate_sequence start with 1 increment by 1
Hibernate: create table album (artist varchar(255), item_id bigint not null, primary key (item_id))
Hibernate: create table book (author varchar(255), isbn varchar(255), book_id bigint not null, primary key (book_id))
Hibernate: create table item (dtype varchar(31) not null, item_id bigint not null, name varchar(255), price integer not null, primary key (item_id))
Hibernate: create table movie (actor varchar(255), director varchar(255), item_id bigint not null, primary key (item_id))

Hibernate: insert into item (name, price, dtype, item_id) values (?, ?, 'A', ?)
Hibernate: insert into album (artist, item_id) values (?, ?)
Hibernate: insert into item (name, price, dtype, item_id) values (?, ?, 'M', ?)
Hibernate: insert into movie (actor, director, item_id) values (?, ?, ?)
Hibernate: insert into item (name, price, dtype, item_id) values (?, ?, 'B', ?)
Hibernate: insert into book (author, isbn, book_id) values (?, ?, ?)
```

### 구분컬럼 없이 동작 ###
```text
Hibernate: create table flat_book (author varchar(255), flat_item_id bigint not null, primary key (flat_item_id))
Hibernate: create table flat_item (flat_item_id bigint not null, name varchar(255), primary key (flat_item_id))

Hibernate: insert into flat_item (name, flat_item_id) values (?, ?)
Hibernate: insert into flat_book (author, flat_item_id) values (?, ?)
```

단일 테이블 전략 (InheritanceType.SINGLE_TABLE)
----
- 테이블 하나에 모두 저장, 구분컬럼을 통해 어떤 Sub Entity가 저장됬는지 구분한다
- Sub Entity가 매핑한 컬람은 모두 null을 허용해야 한다
- 구분컬럼이 반드시 있어야 한다
- @DiscriminatorValue를 지정하지 않으면 기본적으로 Entity이름을 사용
- 장점 : 조인이 없어 조회성능 빠름 / 쿼리단순
- 단점 : sub Entity가 매핑한 컬럼은 모두 nullable / 테이블이 커져 조회성능이 더 느려질 수 있음

[/src/test/java/io/jistol/github/jpademo/entity/advancemapping/singletable](/src/test/java/io/jistol/github/jpademo/entity/advancemapping/singletable)

```text
Hibernate: create table item (dtype varchar(31) not null, item_id bigint not null, name varchar(255), price integer not null, artist varchar(255), author varchar(255), isbn varchar(255), actor varchar(255), director varchar(255), primary key (item_id))

Hibernate: insert into item (name, price, artist, dtype, item_id) values (?, ?, ?, 'Album', ?)
Hibernate: insert into item (name, price, actor, director, dtype, item_id) values (?, ?, ?, ?, 'M', ?)
Hibernate: insert into item (name, price, author, isbn, dtype, item_id) values (?, ?, ?, ?, 'B', ?)
```

구현 클래스마다 테이블 전략
----
- 구현한 Entity마다 super-sub type 통합된 테이블을 생성한다
- 구분 컬럼을 사용하지 않음
- 일반적으로 추천하지 않는다
- 장점 : 서브타입 구분처리시 효과적 / not null 사용 가능
- 단점 : 여러 sub테이블 조회시 UNION사용 -> 느림 / sub테이블 통합쿼리하기 어려움

```text
Hibernate: create table album (item_id bigint not null, name varchar(255), price integer not null, artist varchar(255), primary key (item_id))
Hibernate: create table book (item_id bigint not null, name varchar(255), price integer not null, author varchar(255), isbn varchar(255), primary key (item_id))
Hibernate: create table movie (item_id bigint not null, name varchar(255), price integer not null, actor varchar(255), director varchar(255), primary key (item_id))

Hibernate: insert into album (name, price, artist, item_id) values (?, ?, ?, ?)
Hibernate: insert into movie (name, price, actor, director, item_id) values (?, ?, ?, ?, ?)
Hibernate: insert into book (name, price, author, isbn, item_id) values (?, ?, ?, ?, ?)
```

@MappedSuperclass
----
- 매핑 정보를 상속할 목적으로만 사용
- @AttributeOverride : 부모 매핑정보 재정의
- @AssociationOverride : 부모 연관관계 재정의시 사용
- em.find() / JPQL에서 사용 불가

```text
Hibernate: insert into member (member_name, member_id) values (?, ?)
Hibernate: insert into user (username, id) values (?, ?)
```

[/src/test/java/io/jistol/github/jpademo/entity/advancemapping/mappedsuperclass](/src/test/java/io/jistol/github/jpademo/entity/advancemapping/mappedsuperclass)

복합키와 식별 관계 매핑
----
- 식별관계 : (부모 기본키 + 외래키)를 기본키로 사용하는 자식 테이블
- 비식별관계 : 부모 기본키를 참조용(FK)로만 사용
- 필수적 비식별 관계(Mandatory) : 외래키 NotNull
- 선택적 비식별 관계(Optional) : 외래키 Nullable

@IdClass
----
- 관계형 데이터베이스에 가까운 방법
- 식별자 클래스의 속성명과 Entity에서 사용하는 식별자의 속성명이 같아야 한다
- Serializable 인터페이스를 구현해야한다
- equals, hashcode를 구현해야한다
- 기본 생성자 필요
- public class

[/src/test/java/io/jistol/github/jpademo/entity/advancemapping/idclass](/src/test/java/io/jistol/github/jpademo/entity/advancemapping/idclass)


```text
Hibernate: insert into parent (name, parent_id1, parent_id2) values (?, ?, ?)
Hibernate: insert into child (parent_id1, parent_id2, id) values (?, ?, ?)
```