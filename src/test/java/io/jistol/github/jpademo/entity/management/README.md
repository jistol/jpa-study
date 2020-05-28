# 13. 웹 애플리케이션과 영속성 관리 #

스프링 컨테이너의 영속성 전략
----
- 같은 트랜잭션 범위 내에서 영속성 컨텍스트를 유지한다
- DI로 EntityManager를 주입받으면서도 thread-safe하게 동작한다
> 실제 주입받는 EntityManager는 Proxy로 감쌓아진 SharedEntityManagerInvocationHandler
> thread마다 EntityManager를 새로 생성하여 동작

준영속 상태와 지연로딩
----
- 서비스계층에서 반환한 Entity는 프리젠테이션 계층에서 받아 지연로딩시 영속성컨텍스트가 종료되어 오류발생(준영속 상태)
```java
class TestController {
    public String view() {
        Member member = memberService.selectMember();
        member.getAddress(); // 지연로딩 시 예외 발생
    }
}
```
- 프리젠테이션 계층은 view를 보여주는것이 목적이기 때문에 준영속으로 인한 변경감지 미 동작은 이슈 없음
- 지연로딩 해결 방법
> 미리 로딩
> 1. 글로벌 페치 전략 수정 : FetchType.EAGER      
> + 미사용 Entity조회, N+1이슈       
> 2. JPQL 페치조인 (fetch join)    
> + 화면별 필요한 Entity만 조회하기 위해 repository단에 메서드가 많아질 수 있음     
> 3. 강제 초기화 : 서비스계층에서 미리 초기화(조회)해 둠     
> + 프리젠테이션 계층과 서비스 계층이 커플링 생김
> + 해결을 위해 FACADE계층을 추가시 코드작업이 많아짐     
> OSVI : 영속성 컨텍스트를 VIEW(프리젠테이션 계층)까지 열여두는 방법

OSVI (Open Sesssion In View / Open Entity In View)
----
- 영속성 컨텍스트를 프리젠테이션계층(Controller/Interceptor/Filter)까지 열어둔다는 의미    
- Transaction per request : 클라이언트 요청이 들어와 Filter나 Interceptor에서 트랜잭션이 시작하고 끝나는 방식    
> 프리젠테이션 계층에서도 수정이 가능하여 오동작 가능 (ex: 마스킹을 위해 member.getName("XXX")로 변경)      
> 인터페이스 제공 / 래핑 / DTO로 변환 등의 조치 가능    
> 거의 사용 안함    
- 비지니스 계층 트랜잭션 : 서비스 계층에서만 트랜잭션 적용, 영속성 컨텍스트는 Filter나 Interceptor에서 종료. 이때 flush하지 않는다    
> NonTransactional reads : 영속성 컨텍스트는 트랜잭션 범위내에서 조회/수정, 범위밖에서 조회만 가능.
> 프리젠테이션 계층에서는 조회만 하여 지연로딩 제어     
> 트랜잭션 공유 이슈 : 프리젠테이션 계층에서 수정후 다시 트랜잭션을 시작(서비스계층)하면 수정된 사항이 반영되어 이슈 발생     
> 프리젠테이션 계층에서 지연로딩으로 인한 SQL실행 -> 성능튜닝시 확인 범위 넓음    
- OSVI의 단점       
> 복잡한 화면은 Entity조회보단 DTO로 생성하여 필요한 데이터만 뽑아내는것이 더 효율적    
> 외부 노출 데이터의 경우 Entity를 직접 노출하기 보단 DTO를 생성하는것이 더 안전하다    

