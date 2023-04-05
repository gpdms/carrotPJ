package com.exercise.carrotproject.domain;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


//@SpringBootTest
public class QueryDslTest {

    private JPAQueryFactory jpaQueryFactory;

    @PersistenceContext
    EntityManager em;

/*    @Test
    public void test1() {
        QMember member = QMember.member;
        jpaQueryFactory = new JPAQueryFactory(em);
        List<Member> list = jpaQueryFactory.selectFrom(member).fetch();
//        List<Tuple> tuples = jpaQueryFactory.select(emp.ename, emp.job, emp.sal)
//                .from(emp).fetch();
        list.stream().forEach(t->{
            System.out.println("t = " + t);
        });
    }*/

    @Test
    public void test2(){
//        Member build = Member.builder().memPwd("jk652222").memId("jk65222").loc(Loc.GANGBUK).nickname("22").build();
//        em.persist(build);
//        Member member = em.find(Member.class, build.getMemId());
//        System.out.println("member = " + member);

    }
}
