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
    MemberJpaRepository memberJpaRepository;

    @Test
    void save() {
        //given
        Member member = new Member("memberA");

        //when
        Member savedMember = memberJpaRepository.save(member);

        //then
        Member findMember = memberJpaRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    void delete() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        //when
        memberJpaRepository.delete(memberA);

        //then
        assertThatThrownBy(() -> memberJpaRepository.findById(memberA.getId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findAll() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        //when
        List<Member> resultList = memberJpaRepository.findAll();

        //then
        long count = memberJpaRepository.count();
        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList.size()).isEqualTo(count);
    }

    @Test
    void count() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        //when
        long count = memberJpaRepository.count();

        //then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void findById() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member savedMember = memberJpaRepository.save(memberA);

        //when
        Member findMember = memberJpaRepository.findById(memberA.getId()).get();

        //then
        assertThat(findMember).isEqualTo(savedMember);
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(memberA);
    }

    @Test
    void find() {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        //when
        Member findMember = memberJpaRepository.find(savedMember.getId());

        //then
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void update() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member savedMember = memberJpaRepository.save(memberA);

        //when
        savedMember.setUsername("memberB");

        //then
        Member findMember = memberJpaRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember.getUsername()).isEqualTo("memberB");
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        //given
        Member memberA = new Member("member", 10, null);
        Member memberB = new Member("member", 20, null);
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        //when
        List<Member> member = memberJpaRepository.findByUsernameAndAgeGreaterThan("member", 15);

        //then
        assertThat(member.get(0).getUsername()).isEqualTo("member");
        assertThat(member.get(0).getAge()).isEqualTo(20);
    }

    @Test
    void findByUsernameNamedQueryTest() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        //when
        List<Member> members = memberJpaRepository.findByUsernameNamedQuery(memberA.getUsername());

        //then
        assertThat(members.get(0).getUsername()).isEqualTo("memberA");
        assertThat(members.get(0).getAge()).isEqualTo(10);
    }

    @Test
    void paging() {
        //given
        memberJpaRepository.save(new Member("memeber1", 10, null));
        memberJpaRepository.save(new Member("memeber2", 10, null));
        memberJpaRepository.save(new Member("memeber3", 10, null));
        memberJpaRepository.save(new Member("memeber4", 10, null));
        memberJpaRepository.save(new Member("memeber5", 10, null));
        memberJpaRepository.save(new Member("memeber6", 15, null));
        memberJpaRepository.save(new Member("memeber7", 15, null));
        memberJpaRepository.save(new Member("memeber8", 15, null));
        memberJpaRepository.save(new Member("memeber9", 15, null));
        memberJpaRepository.save(new Member("memeber10", 15, null));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    void bulkUpdate() {
        //given
        memberJpaRepository.save(new Member("memeber1", 10, null));
        memberJpaRepository.save(new Member("memeber2", 10, null));
        memberJpaRepository.save(new Member("memeber3", 10, null));
        memberJpaRepository.save(new Member("memeber4", 10, null));
        memberJpaRepository.save(new Member("memeber5", 10, null));
        memberJpaRepository.save(new Member("memeber6", 15, null));
        memberJpaRepository.save(new Member("memeber7", 15, null));
        memberJpaRepository.save(new Member("memeber8", 15, null));
        memberJpaRepository.save(new Member("memeber9", 15, null));
        memberJpaRepository.save(new Member("memeber10", 15, null));

        //when
        int resultCount = memberJpaRepository.bulkAgePlus(14);

        //then
        assertThat(resultCount).isEqualTo(5);
    }

}