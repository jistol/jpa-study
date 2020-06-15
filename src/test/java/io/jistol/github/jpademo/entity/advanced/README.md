# 15장 고급 주제와 성능 최적화 #

예외처리
----
- JPA 예외는 모두 언체크 예외 (RuntimeException 상속)    
- 트랜잭션 롤백을 표시하는 예외 : 심각한 예외, commit시 RollbackException 발생    
- 트랜잭션 롤백을 표시하지 않는 예외 : 개발자 판단으로 commit/rollback    
- JPA예외를 추상화한 스프링프레임워크 예외는 PersistenceExceptionTranslationPostProcessor를 통해 설정 가능    
- 트랜잭션 롤백시 DB만 롤백, 객체는 그대로 이므로 영속성 컨텍스트를 그대로 사용하면 위험   
> 스프링은 AOP를 이용해 트랜잭션 롤백시 영속성컨텍스트도 함께 clear함   

엔티티비교
----
- 영속성컨텍스트가 같을 경우 동일성(==)비교가능
- 영속성컨텍스트가 다를 경우 동일성 비교 불가, 동등성(equals)비교 가능

프록시 심화주제
----
- 영속성 컨텍스트 : 한번 조회된 엔티티는 프록시/원본 상관없이 기존 것을 사용한다.
- 프록시 타입 비교 : 원본을 상속하므로 == 이 아닌 instanceof 사용할 것 
- 프록시 동등성 비교 : equals 메소드를 사용
> 재구현시 클래스 타입 비교는 instanceof, 멤버변수에 직접 접근하지 말고 getter 메소드를 사용할 것     
- 프록시 상속관계 : 프록시를 부모타입으로 조회시 문제 발생
> 부모타입 프록시로 생성하기 때문에 하위타입 다운 캐스팅이 불가     
> instanceof 연산 사용 불가     
> @ManyToOne(fetch = FetchType.LAZY) 의 경우 문제 발생    
- 프록시 상속관계 해결방법
1. JPQL로 대상객체 직접 조회
2. HibernateProxy를 이용하여 원본 엔티티를 찾는 기능 사용
3. 기능을 위한 별도 인터페이스 구현
4. Visitor 패턴 사용 
> 안전하게 원본 엔티티 접근 가능     
> instanceof / type casting 없이 코딩 가능     
> double dispatch로 인한 복잡성 증가    
> 객체 구조 변경시 모든 Visitor가 수정되야 함    
5. 부모객체에서 반환 메소드 정의 
```java
public Item getSelf() { return this; }
```    
> Visitor보다 단순하고 수정이 없으며 동일한 효과 가능    

N+1 이슈
----
- 즉시로딩(FetchType.EAGER)시에는 발생하지 않으나 직접 해당 엔티티만 쿼리할 경우 발생 가능   
> ex) SELECT p FROM Parent p     
> 자식객체는 부모객체의 ID별로 각각 조회됨 (N+1)     
- 지연로딩(FetchType.LAZY)시, 쿼리시엔 발생하지 않으나 각 엔티티를 순회하면서 조회시 발생   
```java
// 단 건 조회시에는 쿼리를 한번만 실행
parentList.get(0).getChildList(); // SELECT c FROM Child c WHERE c.parent_id = ?
// 전체 순회시 N+1 발생   
parentList.stream().forEach(Parent::getChildList); 
```
- 해결 방법    
1. JPQL `join fetch` 사용   
> ex) SELECT m from Member m join fetch m.orders     
2. @BatchSize 사용 (hibernate)
> 사이즈 만큼 IN절을 이용하여 한번에 조회해 옴.    
> 즉시로딩시 모두 불러와야 하므로 전체 데이터를 size 만큼 나눠서 IN절로 로딩한다.     
> 지연로딩시 첫 조회시 정해진 size만큼 조회, 추가 로딩시 size개수 만큼 추가 조회    
3. @Fetch(FetchMode.SUBSELECT) 사용 (hibernate)
> 즉시로딩 조회시 IN절의 subquery 형태로 조회     
4. @EntityGraph 사용 (Spring)
```java
public interface XXXRepository extends JpaRepository<E, K> {
    @EntityGraph(attributePaths= {"..."}, type=...)
    Optional<E> findByAAA(K key);
}
``` 

- 즉시로딩은 N+1/안쓰는 데이터 로딩 등 불합리
- 모두 지연로딩을 사용하고 필요한 부분에만 페치조인을 적용하는것이 합리적  







