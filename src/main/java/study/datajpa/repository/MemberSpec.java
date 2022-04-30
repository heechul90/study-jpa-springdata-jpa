package study.datajpa.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import static antlr.build.ANTLR.root;

public class MemberSpec {

    public static Specification<Member> teamName(final String teamname) {
        return (Specification<Member>) (root, query, builder) -> {
            if (StringUtils.isEmpty(teamname)) {
                return null;
            }

            Join<Member, Team> t = root.join("team", JoinType.INNER); //&#xD68C;&#xC6D0;&#xACFC; &#xC870;&#xC778;
            return builder.equal(t.get("name"), teamname);
        };
    }

    public static Specification<Member> username(final String username) {
        return (Specification<Member>) (root, query, builder) ->
                builder.equal(root.get("username"), username);
    }
}
