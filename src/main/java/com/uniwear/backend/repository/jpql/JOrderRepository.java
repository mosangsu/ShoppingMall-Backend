package com.uniwear.backend.repository.jpql;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uniwear.backend.dto.OrderDto;
import com.uniwear.backend.entity.Order;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.uniwear.backend.entity.QOrder.*;

@Repository
public class JOrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JOrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public List<OrderDto.Count.Res> countOrderGroupByStatus(Long memberId) {
        return query
                .select(Projections.fields(OrderDto.Count.Res.class, order.status, order.count().as("count")))
                .from(order)
                .where(order.member.memberId.eq(memberId)) //동적쿼리
                .groupBy(order.status)
                .limit(1000)
                .fetch();
    }
}