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






