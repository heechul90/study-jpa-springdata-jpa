package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * where name = name and age > age
     * @return
     */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /**
     * 전체조회
     */
    List<Member> findHelloBy();

    /**
     * 네임드쿼리 조회
     * @Query 생량 가능
     * Member entity에서 메서드명 findByUsernameNamedQuery 쿼리를 먼저 찾고 없으면 쿼리 이름으로 쿼리 생성한다.
     */
    @Query(name = "Member.findByUsernameNamedQuery")
    List<Member> findByUsernameNamedQuery(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

}
