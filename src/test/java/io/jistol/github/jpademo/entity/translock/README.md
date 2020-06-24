# 16장. 트랜잭션과 락, 2차 캐시 # 

트랜잭션과 락
----
1. 낙관적 락
- 대부분 충돌이 발생하지 않는다고 가정
- 트랜잭션을 커밋하기 전까지는 알 수 없다    
2. 비관적 락 
- 충돌 발생을 가정하고 우선 락을 거는 방법
- 데이터베이스가 제공하는 락 기능을 이용 (ex : SELECT FOR UPDATE)     
3. 두 번의 갱신 분실 문제 (second lost updates problem)
- A, B가 같은 데이터를 수정 중일때 A가 저장한 이후 B가 저장할 경우 A의 수정사항이 유실되는 현상    
- 해결방법 1) 마지막 커밋만 인정 (기본)
- 해결방법 2) 최초 커밋만 인정 (@Version을 이용하여 처리)
- 해결방법 3) 충돌하는 갱신내용 병합   

JPA 락
----
1. 락 적용위치
```java
// EntityManager.find
EntityManager em = emf.createEntityManager();
EntityTransaction et = em.getTransaction();
et.begin();
Member m1 = em.find(Member.class, 1L, LockModeType.NONE);

// EntityManager.lock
Member m2 = em.find(Member.class, 2L);
em.lock(m2, LockModeType.OPTIMISTIC);

// Query.setLockMode
String query = "SELECT m FROM Member m WHERE id = 3";
Member m3 = (Member)em.createQuery(query)
        .setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
        .getSingleResult();

// @NamedQuery Lock
Member m4 = (Member)em.createNamedQuery("Member.findByNameWithLock")
        .setParameter("name", "NAME4")
        .getSingleResult();
```
```java
@Entity
@Data
@NamedQuery(
        name = "Member.findByNameWithLock",
        query = "SELECT m FROM Member m WHERE m.name = :name",
        lockMode = LockModeType.OPTIMISTIC
)
public class Member {
    @Id @GeneratedValue private Long id;
    private String name;
    @Version private Long version;
}
```

@Version
----
- 엔티티의 버전을 관리 하는 기능    
- 엔티티 수정시마다 version+1로 업데이트
- 수정할때 조회시점 버전과 다를 경우 예외 발생 (최초 커밋만 유지)

JPA 낙관적 락
----
1. LockModeType.NONE
- 별도 락 모드를 적용하지 않은 상태로 @Version 필드로만 낙관적 락을 적용한 상태  
- 조회~수정 시점까지를 보장  
- 두번의 갱신 분실 문제를 '최초 커밋만 유지'방식으로 적용
2. LockModeType.OPTIMISTIC
- 조회한 엔티티는 트랜젝션이 끝날때까지 다른 트랜젝션에서 변경하지 않음을 보장
- NONE의 경우 수정을 해야만 버전을 체크하나 OPTIMISTIC은 조회시에도 버전을 체크
- DIRTY_READ / NON-REPEATABLE_READ를 방지   
3. LockModeType.OPTIMISTIC_FORCE_INCREMENT
- 엔티티를 조회한 순간 Version을 올린다
- 조회한 엔티티를 다시 UPDATE 할 경우 Version이 또 올라가 +2가 될 수 있다
- 연관 엔티티와 함께 버전관리를 할 경우 사용하면 좋다
> ex : 게시판 테이블의 연관 테이블인 첨부파일 테이블 수정시 게시판이 수정되지 않더라도 첨부파일 수정에 맞춰 같이 Version을 변경    
> 실효성이 있을지 의문   
 
