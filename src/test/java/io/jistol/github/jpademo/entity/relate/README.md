# 8장 프록시와 연관관계 관리 #

프록시
----
- 객체가 DB에 저장되어 있어 객체 그래프로 연관객체 탐색이 어려움
- 실제 사용 시점에 DB에서 조회
- 즉시로딩/지연로딩 지원
- Member.team 객체의 실제사용시점까지 지연(ex:team.getName())
- 프록시 객체 : DB조회를 지연해주는 가짜객체
- 컬렉션 래퍼 : 지연로딩시 List<T> 같은 컬렉션을 래핑하는 객체
- 하이버네이트는 지연로딩을 위해 프록시방식/바이트코드삽입방식 두가지 지원
- 지연로딩을 위해 em.find 대신 em.getReference -> 프록시객체 반환

프록시 객체
----
- 실제 객체를 상속받아 구현하며 실 객체의 참조를 저장하여 메소드 실행시 위임한다
- 프록시 객체 초기화 : 메소드 호출시 DB를 조회하여 엔티티객체를 생성
- 영속성컨텍스트에 이미 존재하면 find / getReference() 이미 생성된 객체를 반환한다. find로 생성되면 원래객체 / getReference로 생성되면 프록시객체
- 타입체크시 프록시 객체도 상속했기 때문에 instanceof 체크시 true
- 준영속 상태에서 프록시 초기화시 LazyInitializationException 발생 (hibernate의 경우)
- id값은 기본 저장. AccessType.PROPERTY 방식일 경우 getId()실행시에는 초기화 하지 않음
- 연관관계설정시 getReference로 객체 생성하면 DB조회없이 저장가능
- 프록시 초기화 여부는 PersistenceUnitUtil.isLoaded로 확인가능

JPA 조인전략
----
- FK가 nullable 일 경우 left outer join 
- nullable=false 이거나 optional=false 일 경우 inner join 
> table 생성시 not null로 생성된다.
- 컬렉션 즉시로딩시 항상 outer join 사용됨 (JPA가 판단 불가)
> @ManyToOne, @OneToOne >> optional : false => inner join / true => outer join      
> @OneToMany, @ManyToMany >> optional : false => outer join / true => outer join     

즉시로딩 (FetchType.EAGER)
----
- 연관 entity를 즉시 조회. hibernate는 가능한 SQL조인을 통해 한번에 조회함
- @ManyToOne, @OneToOne
- 컬렉션을 하나 이상 즉시 로딩시 비추

지연로딩 (FetchType.LAZY) 
----
- 연관 entity를 프록시객체를 통해 조회
- @OneToMany, @ManyToMany
- 추천방법 : 모든 로딩을 LAZY를 사용후 개발 완료단계시 실제 사용 상황을 판단하여 EAGER로 변경

영속성 전이 (CASCADE)
----
- 영속화시 연관객체를 같이 영속화 해준다
- REMOVE 연결시 같이 삭제 해주나 delete문을 ID마다 날린다 (다른 방법 필요)

고아객체(ORPHAN)제거 (orphanRemoval)
----
- 고아객체 : 부모Entity와 연관관계가 끊어진 자식 Entity
- 참조하는 곳이 하나일때만 사용 가능
- @OneToOne, @OneToMany
- CascadeType.REMOVE : 부모객체 삭제시 적용 
- orphanRemoval : 자식객체를 부모객체에서 제거시 적용