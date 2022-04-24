package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TeamRepositoryTest {

    @Autowired
    TeamRepository repository;

    @Test
    void save() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        //when
        Team savedTeam = repository.save(teamA);

        //then
        Team findTeam = repository.findById(savedTeam.getId()).get();
        assertThat(findTeam).isEqualTo(savedTeam);
        assertThat(findTeam.getId()).isEqualTo(teamA.getId());
        assertThat(findTeam).isEqualTo(teamA);
    }

    @Test
    void delete() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        Team savedTeamA = repository.save(teamA);
        repository.save(teamB);

        //when
        repository.delete(teamA);

        //then
        List<Team> teams = repository.findAll();
        assertThatThrownBy(() -> repository.findById(teamA.getId()).get())
                .isInstanceOf(NoSuchElementException.class);
        assertThat(teams.size()).isEqualTo(1);
    }

    @Test
    void findAll() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        repository.save(teamA);
        repository.save(teamB);

        //when
        List<Team> teams = repository.findAll();

        //then
        assertThat(teams.size()).isEqualTo(2);
    }

    @Test
    void count() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        repository.save(teamA);
        repository.save(teamB);

        //when
        long count = repository.count();

        //then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void findById() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        Team savedTeamA = repository.save(teamA);
        repository.save(teamB);

        //when
        Team findTeamA = repository.findById(savedTeamA.getId()).get();

        //then
        assertThat(findTeamA).isEqualTo(teamA);
        assertThat(findTeamA.getId()).isEqualTo(savedTeamA.getId());
    }

    @Test
    void update() {
        //given
        Team teamA = new Team("teamA");
        Team savedTeamA = repository.save(teamA);

        //when
        teamA.setName("teamC");

        //then
        Team findTeam = repository.findById(savedTeamA.getId()).get();
        assertThat(findTeam.getName()).isEqualTo("teamC");
        assertThat(findTeam.getName()).isEqualTo(teamA.getName());
    }
}