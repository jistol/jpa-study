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

@Convert
----
- Entity <-> DB간 데이터 변환 지원
- @Converter + AttributeConvert<E,D> 인터페이스 구현
- @Convert 어노테이션으로 제어하며 필드에 직접 지정하거나 클래스에 지정할 수 있다
- @Converter 어노테이션을 통해 글로벌 설정 가능

@Listener
----
- 엔티티 생명주기에 따른 이벤트 처리 지원
- PostLoad, {Pre|Post}{Persist|Update|Remove}
- 엔티티 직접적용 (어노테이션 이용)
- 별도 Listener클래스를 만들고 엔티티클래스에 @EntityListeners로 등록
- default 리스너로 등록
- 여러 리스너 등록시 (기본리스너 -> 부모클래스 리스너 -> 리스너 -> 엔티티) 순으로 동작
- 기본리스너 무시 @ExcludeDefaultListners
- 상위 클래스 이벤트 리스너 무시 @ExcludeSuperclassListeners

엔티티그래프
----
- 엔티티 조회 시점에 연관된 엔티티들을 함께 조회하는 기능
- 엔티티 클래스에 @NamedEntityGraph 지정 
- 연관 엔티티와 연관된 sub 엔티티도 같이 조회하기 위해서는 @NamedSubgraph를 사용    
- setHint메서드를 통해 JPQL에서도 사용가능
- QueryDsl에서는 직접 쿼리문은 만들기 때문에 setHint로 지정하기 보단 직접 fetchJoin을 걸어야 적용된다    
 