package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    /**
     * where name = name and age > age
     *
     * @return
     */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /**
     * 전체조회
     */
    List<Member> findHelloBy();

    /**
     * 네임드쿼리 조회
     *
     * @Query 생량 가능
     * Member entity에서 메서드명 findByUsernameNamedQuery 쿼리를 먼저 찾고 없으면 쿼리 이름으로 쿼리 생성한다.
     */
    @Query(name = "Member.findByUsernameNamedQuery")
    List<Member> findByUsernameNamedQuery(@Param("username") String username);

    /**
     * 직접 쿼리 사용
     */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    /**
     * entity말고 그냥 쿼리 생성
     */
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    /**
     * dto로 쿼리생성 조회
     */
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    /**
     * 파라미터 바인딩(리스트)
     */
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    /**
     * 반환타입(컬렉션)
     */
    List<Member> findListByUsername(String username);

    /**
     * 반환타입(단건)
     */
    Member findMemberByUsername(String username);

    /**
     * 반환타입(단건.옵션널)
     */
    Optional<Member> findOptionalByUsername(String username);

    /**
     * 페이징(Page)
     */
    @Query(value = "select m from Member m left join m.team t",
            countName = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * 페이징(Slice)
     */
    Slice<Member> getByAge(int age, Pageable pageable);

    /**
     * 벌크성 수정 쿼리
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /**
     * 패치조인
     */
    @Query("select m from Member m left join fetch m.team t")
    List<Member> findMemberFetchJoin();

    /**
     * 패치조인
     */
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    /**
     * 패치조인
     */
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    /**
     * 패치조인
     */
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    /**
     * 패치조인
     */
    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUsername(@Param("username") String username);

    /**
     * 쿼리힌트 사용. 영속성 컨택스트를 이용해서 업데이트를 할 수 없음
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyById(Long id);

    /**
     * select for update 추가해줌(잘 모름)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
