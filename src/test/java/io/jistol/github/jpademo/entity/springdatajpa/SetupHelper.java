package io.jistol.github.jpademo.entity.springdatajpa;

import io.jistol.github.jpademo.entity.springdatajpa.entity.Address;
import io.jistol.github.jpademo.entity.springdatajpa.entity.Member;
import io.jistol.github.jpademo.entity.springdatajpa.entity.Team;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Lazy
@Component("springdatajpaSetupHelper")
public class SetupHelper {
    @PersistenceContext
    private EntityManager em;

    public Address genAddress(int i) {
        Address address = new Address();
        address.setCity("CITY" + i);
        address.setStreet("STREET" + i);
        address.setZipcode("1234" + i);
        return address;
    }
    
    public Team genTeam(int i) {
        Team team = new Team();
        team.setName("TEAM" + i);
        return team;
    }

    @Transactional
    public void generate(int memberCount) {
        List<Address> addressList = IntStream.range(0, 10)
                .mapToObj(this::genAddress)
                .collect(Collectors.toList());
        
        List<Team> teamList = IntStream.range(0, 5)
                .mapToObj(this::genTeam)
                .peek(team -> em.persist(team))
                .collect(Collectors.toList());
        
        for (int i=1 ; i < memberCount ; i++) {
            Member m = new Member();
            m.setName("MEM" + i);
            m.setAge((i + 20) % 25);
            m.setAddress(addressList.get(i >= addressList.size() ? i % addressList.size() : i));
            em.persist(m);

            Team team = teamList.get(i >= teamList.size() ? i % teamList.size() : i);
            m.setTeam(team);
            team.getMembers().add(m);
        }
    }
}
