package io.jistol.github.jpademo.entity.oopquery;

import io.jistol.github.jpademo.entity.oopquery.entity.Address;
import io.jistol.github.jpademo.entity.oopquery.entity.Member;
import io.jistol.github.jpademo.entity.oopquery.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SetupHelper {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
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
    
    public void generate(int memberCount) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
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
            
            Team team = teamList.get(i >= teamList.size() ? i % teamList.size() : i);
            m.setTeam(team);
            team.getMembers().add(m);
            
            em.persist(m);
        }
        et.commit();
    }
}
