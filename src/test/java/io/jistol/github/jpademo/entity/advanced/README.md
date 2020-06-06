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



