package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository repository;

    @Test
    void save() {
        //given
        Member member = new Member("memberA");

        //when
        Member savedMember = repository.save(member);

        //then
        Member findMember = repository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    void delete() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        repository.save(memberA);
        repository.save(memberB);

        //when
        repository.delete(memberA);

        //then
        assertThatThrownBy(() -> repository.findById(memberA.getId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findAll() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        repository.save(memberA);
        repository.save(memberB);

        //when
        List<Member> resultList = repository.findAll();

        //then
        long count = repository.count();
        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList.size()).isEqualTo(count);
    }

    @Test
    void count() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        repository.save(memberA);
        repository.save(memberB);

        //when
        long count = repository.count();

        //then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void findById() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member savedMember = repository.save(memberA);

        //when
        Member findMember = repository.findById(memberA.getId()).get();

        //then
        assertThat(findMember).isEqualTo(savedMember);
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(memberA);
    }

    @Test
    void find() {
        //given
        Member member = new Member("memberA");
        Member savedMember = repository.save(member);

        //when
        Member findMember = repository.find(savedMember.getId());

        //then
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void update() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member savedMember = repository.save(memberA);

        //when
        savedMember.setUsername("memberB");

        //then
        Member findMember = repository.findById(savedMember.getId()).get();
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember.getUsername()).isEqualTo("memberB");
    }
}