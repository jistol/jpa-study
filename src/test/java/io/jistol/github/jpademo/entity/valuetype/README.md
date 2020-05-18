# 9. 값 타입 #
- 식별자가 없고 생명주기를 엔티티에 의존
- 공유시 위험, 불변객체 사용
- 식별자가 필요하고 지속적인 값 추적/구분/관리가 필요할 경우 Entity 사용 할 것
- 기본값 타입 : primitive / wrapper class / String
- 임베디드 타입 : 복합 값 타입
- 컬렉션 값 타입 : 기본값이나 임베디드 객체를 컬렉션으로 관리하는 타입  

임베디드 타입(복합값 타입)
----
- @Embedded / @Embeddable 설정을 통해 별도 클래스를 복합값으로 추가할 수 있다
- Entity정보를 좀 더 응집력 있게 관리 가능
- 객체는 컴포지션 하지만 실제 DB에는 각각의 컬럼으로 매핑된다
- 임베디드 타입 내부에 연관관계나 임베디드 타입을 추가할 수 있음
- 임베디드 타입이 null 일 경우 매핑한 컬럼 값은 모두 null이 된다

### @AttributeOverride ###
- 임베디드로 인한 컬럼명이 중복 될 경우 @AttributeOverride로 재지정가능
- 너무 많이 쓸 경우 코드가 지저분해짐
- 임베디드 타입이 임베디드 타입을 가지고 있어도 Entity상에서 설정되어야 한다

[/src/test/java/io/jistol/github/jpademo/entity/valuetype/embedded](/src/test/java/io/jistol/github/jpademo/entity/valuetype/embedded)    

```text
Hibernate: create table member (id bigint not null, company_city varchar(255), company_street varchar(255), company_plusfour varchar(255), company_zip varchar(255), city varchar(255), street varchar(255), plus_four varchar(255), zip varchar(255), name varchar(255), area_code varchar(255), local_number varchar(255), end_date date, start_date date, provider_name varchar(255), primary key (id))
Hibernate: create table phone_number_provider (name varchar(255) not null, primary key (name))
``` 

값 타입 주의점
----
- 값 타입 공유참조시 부작용이 발생, 불변객체를 사용할 것
- 값 타입 비교시 모든 컬럼값을 재정의한 equals()메소드를 사용할 것

값 타입 컬렉션
----
- @ElementCollection을 지정하여 값타입을 컬렉션 형태로 만들수 있음
- 별도 테이블 매핑이 필요하여 @CollectionTable을 사용하여 테이블 지정
- 기본값/임베디드객체 모두 사용가능
- 영속성전이(cascade) 및 고아 객체 제거(orphan remove)기능을 필수로 함
- 값 변경시 전체삭제/전체저장 하므로 데이터가 많을 경우 일대다 Entity관계를 고려해야함

```text
Hibernate: create table address (member_id bigint not null, city varchar(255), street varchar(255), zipcode varchar(255))
Hibernate: create table favorite_foods (member_id bigint not null, food_name varchar(255))
Hibernate: create table member (id bigint not null, city varchar(255), street varchar(255), zipcode varchar(255), primary key (id))
```


