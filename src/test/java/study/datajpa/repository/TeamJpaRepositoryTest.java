package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamJpaRepositoryTest {

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Test
    void save() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        //when
        Team savedTeam = teamJpaRepository.save(teamA);

        //then
        Team findTeam = teamJpaRepository.findById(savedTeam.getId()).get();
        assertThat(findTeam).isEqualTo(savedTeam);
        assertThat(findTeam.getId()).isEqualTo(teamA.getId());
        assertThat(findTeam).isEqualTo(teamA);
    }

    @Test
    void delete() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        Team savedTeamA = teamJpaRepository.save(teamA);
        teamJpaRepository.save(teamB);

        //when
        teamJpaRepository.delete(teamA);

        //then
        List<Team> teams = teamJpaRepository.findAll();
        assertThatThrownBy(() -> teamJpaRepository.findById(teamA.getId()).get())
                .isInstanceOf(NoSuchElementException.class);
        assertThat(teams.size()).isEqualTo(1);
    }

    @Test
    void findAll() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamJpaRepository.save(teamA);
        teamJpaRepository.save(teamB);

        //when
        List<Team> teams = teamJpaRepository.findAll();

        //then
        assertThat(teams.size()).isEqualTo(2);
    }

    @Test
    void count() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamJpaRepository.save(teamA);
        teamJpaRepository.save(teamB);

        //when
        long count = teamJpaRepository.count();

        //then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void findById() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        Team savedTeamA = teamJpaRepository.save(teamA);
        teamJpaRepository.save(teamB);

        //when
        Team findTeamA = teamJpaRepository.findById(savedTeamA.getId()).get();

        //then
        assertThat(findTeamA).isEqualTo(teamA);
        assertThat(findTeamA.getId()).isEqualTo(savedTeamA.getId());
    }

    @Test
    void update() {
        //given
        Team teamA = new Team("teamA");
        Team savedTeamA = teamJpaRepository.save(teamA);

        //when
        teamA.setName("teamC");

        //then
        Team findTeam = teamJpaRepository.findById(savedTeamA.getId()).get();
        assertThat(findTeam.getName()).isEqualTo("teamC");
        assertThat(findTeam.getName()).isEqualTo(teamA.getName());
    }
}