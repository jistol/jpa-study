# 12. 스프링 데이터 JPA #
- 데이터 접근 계층 개발지 반복되는 CRUD 문제를 인터페이스 작성만으로 완료
- JpaRepository:JPA only -> PagingAndSortingRepository -> CrudRepository -> Repository

쿼리메소드
----
- 메소드 이름으로 쿼리 생성
> ex) findByNameAndAddressCity(...)
> SELECT m FROM Member m WHERE m.name = ?1 AND m.address.city = ?0
- 임베디드 타입의 경우 위와 같이 타입변수명의 필드를 붙여 사용(AddressCity)
- 바인딩변수는 1부터 시작
- NamedQuery를 쿼리 메소드 명으로 사용 가능
```java
//Member.java
@NamedQuery(
        name = "Member.findByInfo",
        query = "SELECT m FROM Member m WHERE m.name = :name AND m.age = :age"
)

// MemberRepository.java
List<Member> findByInfo(@Param("name") String name, @Param("age") int age);
```
- @Query 어노테이션을 통해 직접 쿼리작성 가능
> 기본 JPQL로 작성해야하며 Native로 하려면 nativeQuery=true 설정     
> 바인딩 변수는 JPQL/Native 모두 0부터 시작(책과 다름)      
- @Modifying을 통해 벌크성 수정쿼리 실행
> 수정후 영속성컨텍스트 초기화 하려면 clearAutomatically=true 설정    
- 반환타입 : 없을 경우 컬렉션은 빈 컬렉션, 단건은 null 반환    
- 정렬 : Sort 파라메터 사용 
- 페이징 : Pageable 파라메터 사용 (내부에 Sort포함) / 반환타입 Page<T> 사용
> 반환타입을 사용시 count를 계산하기 위해 쿼리를 한번 더 한다     
> PageRequest 객체를 통해 Pageable 파라메터를 생성할 수 있다    
- 기타 : @QueryHint (JPA구현체에 대한 힌트), @Lock    

Specification (명세)
----
- Criteria의 검색조건 조합기능
- 컴포지트 패턴으로 조합 가능

Web확장 기능
----
- JPA관련 Converter와 ArgumentResolver를 지원
- @EnableSpringDataWebSupport로 설정하거나 SpringDataWebConfiguration을 빈으로 등록
- Converter : HTTP파라메터로 넘어온 ID를 이용하여 Entity객체를 찾아 바인딩
> OSIV 미사용 : 준영속상태
> OSIV 사용 : 영속상태이나 Controller와 View에서는 flush를 하지 않음
```java
@RequrestMapping("member")
public String member(@RequestParam("id") Member member) { ... }
```
- ArgumentResolver를 통해 페이징과 정렬 객체를 받을수 있다 
> page : 0부터 시작, size, sort : 정렬속성(asc|desc)
- 접두사(@Qualifier)를 이용하여 여러 페이징 정보를 넘길 수 있다
```java
// /member?member_page=0&order_page=1
public String list(
    @Qualifier("member") Pageable memberPageable,
    @Qualifier("order") Pageable orderPageable,
) { ... }
```
- @PageableDefault 어노테이션을 통해 기본값 변경 가능 (기본값 page=0,size=20)
- JPA 공통 인터페이슨느 SimpleJpaRepository 클래스로 구현된다
- save() : 새 Entity는 저장(persist), 기존 Entity는 병합(merge)
> 식별자가 null 객체이거나 기본타입 숫자 0일 경우 새로운 Entity로 판단한다    

스프링 데이터 JPA와 QueryDSL통합
----
- 기존 repository에 QueryDslPredicateExecutor<T>를 상속받아 확장된 기능 사용
> find문에 Predicate절을 사용 할 수 있다
> join, fetch등은 사용불가(묵시적 조인사용가능)
- QueryDslRepositorySupport를 상속받아 직접 JPAQuery객체를 사용 할 수 있다   




