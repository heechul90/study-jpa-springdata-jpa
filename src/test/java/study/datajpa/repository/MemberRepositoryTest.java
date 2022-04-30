package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberQueryRepository memberQueryRepository;

    @Test
    void save() {
        //given
        Member member = new Member("memberA");

        //when
        Member savedMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    void delete() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        memberRepository.delete(memberA);

        //then
        assertThatThrownBy(() -> memberRepository.findById(memberA.getId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findAll() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        List<Member> resultList = memberRepository.findAll();

        //then
        long count = memberRepository.count();
        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList.size()).isEqualTo(count);
    }

    @Test
    void count() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        long count = memberRepository.count();

        //then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void findById() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member savedMember = memberRepository.save(memberA);

        //when
        Member findMember = memberRepository.findById(memberA.getId()).get();

        //then
        assertThat(findMember).isEqualTo(savedMember);
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(memberA);
    }

    @Test
    void update() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member savedMember = memberRepository.save(memberA);

        //when
        savedMember.setUsername("memberB");

        //then
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember.getUsername()).isEqualTo("memberB");
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        //given
        Member memberA = new Member("member", 10, null);
        Member memberB = new Member("member", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        List<Member> member = memberRepository.findByUsernameAndAgeGreaterThan("member", 15);

        //then
        assertThat(member.get(0).getUsername()).isEqualTo("member");
        assertThat(member.get(0).getAge()).isEqualTo(20);
    }

    @Test
    void findHelloBy() {
        //given
        Member memberA = new Member("member", 10, null);
        Member memberB = new Member("member", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        List<Member> members = memberRepository.findHelloBy();

        //then
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    void findByUsernameNamedQueryTest() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        List<Member> members = memberRepository.findByUsernameNamedQuery(memberA.getUsername());

        //then
        assertThat(members.get(0).getUsername()).isEqualTo("memberA");
        assertThat(members.get(0).getAge()).isEqualTo(10);
    }

    @Test
    void findUserTest() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        List<Member> members = memberRepository.findUser("memberA", 10);

        //then
        assertThat(members.get(0).getUsername()).isEqualTo("memberA");
        assertThat(members.get(0).getAge()).isEqualTo(10);
    }

    @Test
    void findUsernameListTest() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        List<String> usernames = memberRepository.findUsernameList();
        for (String username : usernames) {
            System.out.println("username = " + username);
        }

        //then
        assertThat(usernames.size()).isEqualTo(2);
    }

    @Test
    void findMemberDtoTest() {
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member memberA = new Member("memberA", 10, team);
        Member memberB = new Member("memberB", 20, team);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        List<MemberDto> memberDtos = memberRepository.findMemberDto();
        for (MemberDto memberDto : memberDtos) {
            System.out.println("memberDto.getId() = " + memberDto.getId());
            System.out.println("memberDto.getUsername() = " + memberDto.getUsername());
            System.out.println("memberDto.getTeamname() = " + memberDto.getTeamname());
        }

        //then
        assertThat(memberDtos.get(0).getTeamname()).isEqualTo("teamA");
        assertThat(memberDtos.get(1).getTeamname()).isEqualTo("teamA");
    }


    @Test
    void findByNamesTest() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        List<Member> members = memberRepository.findByNames(Arrays.asList("memberA", "memberB"));
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
        }

        //then
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    void findListByUsername() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        List<Member> members = memberRepository.findListByUsername("memberA");
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
        }

        //결과값이 없을때 null이 아니다
        List<Member> resultList = memberRepository.findListByUsername("memberAAA");
        System.out.println("resultList = " + resultList);

        //then
        assertThat(members.get(0).getUsername()).isEqualTo("memberA");
        assertThat(members.get(0)).isEqualTo(memberA);
    }

    @Test
    void findMemberByUsername() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        Member member = memberRepository.findMemberByUsername("memberA");

        //결과값이 없을때 null로 반환
        Member result = memberRepository.findMemberByUsername("memberAAA");
        System.out.println("result = " + result);

        //then
        assertThat(member.getUsername()).isEqualTo("memberA");
        assertThat(member).isEqualTo(memberA);
    }

    @Test
    void findOptionalByUsername() {
        //given
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        Optional<Member> member = memberRepository.findOptionalByUsername("memberA");

        //then
        assertThat(member.get()).isEqualTo("memberA");
    }

    @Test
    void pagingPage() {
        //given
        memberRepository.save(new Member("memeber1", 10, null));
        memberRepository.save(new Member("memeber2", 10, null));
        memberRepository.save(new Member("memeber3", 10, null));
        memberRepository.save(new Member("memeber4", 10, null));
        memberRepository.save(new Member("memeber5", 10, null));
        memberRepository.save(new Member("memeber6", 15, null));
        memberRepository.save(new Member("memeber7", 15, null));
        memberRepository.save(new Member("memeber8", 15, null));
        memberRepository.save(new Member("memeber9", 15, null));
        memberRepository.save(new Member("memeber10", 15, null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member = " + member);
        }

        long totalElements = page.getTotalElements();
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
    }

    @Test
    void pagingSlice() {
        //given
        memberRepository.save(new Member("memeber", 10, null));
        memberRepository.save(new Member("memeber", 10, null));
        memberRepository.save(new Member("memeber", 10, null));
        memberRepository.save(new Member("memeber", 10, null));
        memberRepository.save(new Member("memeber", 10, null));
        memberRepository.save(new Member("memeber", 15, null));
        memberRepository.save(new Member("memeber", 15, null));
        memberRepository.save(new Member("memeber", 15, null));
        memberRepository.save(new Member("memeber", 15, null));
        memberRepository.save(new Member("memeber", 15, null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Slice<Member> page = memberRepository.getByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member = " + member);
        }

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
    }

    @Test
    void bulkAgePlus() {
        //given
        memberRepository.save(new Member("memeber1", 10, null));
        memberRepository.save(new Member("memeber2", 10, null));
        memberRepository.save(new Member("memeber3", 10, null));
        memberRepository.save(new Member("memeber4", 10, null));
        memberRepository.save(new Member("memeber5", 10, null));
        memberRepository.save(new Member("memeber6", 15, null));
        memberRepository.save(new Member("memeber7", 15, null));
        memberRepository.save(new Member("memeber8", 15, null));
        memberRepository.save(new Member("memeber9", 15, null));
        memberRepository.save(new Member("memeber10", 15, null));

        //when
        int resultCount = memberRepository.bulkAgePlus(14);

        //영속성 컨텍스트를 무시하고 업데이트하기 때문에 영속성을 다 날려준다
        //em.flush();
        //em.clear(); //어노테이션에 옵션 넣어두어도 된다.

        //then
        assertThat(resultCount).isEqualTo(5);
    }

    @Test
    void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        em.flush();
        em.clear();

        //when
        //List<Member> members = memberRepository.findAll();
        //List<Member> members = memberRepository.findMemberFetchJoin();
        //List<Member> members = memberRepository.findMemberEntityGraph();
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    void queryHint() {
        //given
        Member member = new Member("memberA", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when
        //Member findMember = memberRepository.findById(member.getId()).get();
        Member findMember = memberRepository.findReadOnlyById(member.getId());
        findMember.setUsername("memberB");

        //then
        assertThat(findMember.getUsername()).isEqualTo("memberB");
    }

    @Test
    void lock() {
        //given
        Member member = new Member("memberA", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findLockByUsername("memberA");
    }

    @Test
    void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    void specBasic() {
        //given
        Team team = new Team("teamA");
        em.persist(team);

        Member m1 = new Member("m1", 0, team);
        Member m2 = new Member("m2", 0, team);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        //then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void queryByExample() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        //probe
        Member member = new Member("m1");
        Team team = new Team("teamA");
        member.setTeam(team);

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("m1");
    }

    @Test
    void projections() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

        //then
        for (UsernameOnly username : result) {
            System.out.println("username = " + username);
        }
    }

    @Test
    void projections2() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        List<NestedCloseProjections> resultList = memberRepository.findProjectionsDtoByUsername("m1", NestedCloseProjections.class);

        //then
        for (NestedCloseProjections result : resultList) {
            System.out.println("username = " + result.getUsername());
        }
    }

    @Test
    void nativeQuery() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        Member result = memberRepository.findByNativeQuery("m1");

        //then
        System.out.println("result = " + result);
    }

    @Test
    void nativeProjectionQuery() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = result.getContent();

        //then
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection = " + memberProjection.getUsername());
            System.out.println("memberProjection = " + memberProjection.getTeamName());
        }
    }

}