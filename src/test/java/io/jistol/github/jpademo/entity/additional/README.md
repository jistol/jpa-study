# 14. 컬렉션과 부가기능 #

컬렉션 (Collection, List, Set)
----
- @OneToMany, @ManyToMany, @ElementCollection을 이용할 떄 사용
- JPA 명세에는 특별한 언급 없음
- hibernate는 컬렉션은 Entity를 영속상태로 만들때 레퍼컬렉션으로 변경
- 컬렉션 사용시 즉시 초기화 권장. 
> ex) Collection<Member> members = new ArrayList<>();
- Collection / List (PersistenceBag) : 중복(O), 순서(x)
> Entity추가시 중복Entity 비교없이 저장, 지연로딩된 컬렉션을 초기화 하지 않는다    
- Set (PersistenceSet) : 중복(X), 순서(X)
> Entity추가시 중복체크를 위해 지연로딩 된 컬렉션을 초기화 함      
> 비교연산으로 equals와 hashcode를 같이 사용.    
> Lombok사용시 @Data만 사용할 경우 비교연산중 오류 발생. @EqualsAndHashcode 재정의를 통해 무한 hashcode 호출을 막아줘야 한다.    
- List + @OrderColumn (PersistenceList) : 중복(O), 순서(O)
> 실무에서 잘 사용되지 않는다    
> 순서가 있는 컬렉션으로 데이터베이스 순서도 같이 관리한다    
> 1:N관계에서 1쪽에서 매핑하므로 N쪽에서는 순서 컬럼을 알 수 없다    
> 저장시 순서를 UPDATE하는 쿼리를 추가로 실행    
> 위치값 변경시 각 Entity의 순서 변경을 위한 쿼리를 N번 실행한다    
> 중간에 순서가 빌 경우 List에는 null을 보관한다    
- @OrderBy : 컬렉션 정렬을 지정    
