package com.uniwear.backend.repository.jpql;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uniwear.backend.dto.EstimateDto;
import com.uniwear.backend.dto.OrderDto;
import com.uniwear.backend.entity.Estimate;
import com.uniwear.backend.entity.Order;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.uniwear.backend.entity.QEstimate.*;
import static com.uniwear.backend.entity.QEstimateProduct.*;
import static com.uniwear.backend.entity.QProduct.*;
import static com.uniwear.backend.entity.QProductPicture.*;
import static com.uniwear.backend.entity.member.QMember.*;

@Repository
public class JEstimateRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JEstimateRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public List<EstimateDto.Count.Res> countEstimateGroupByStatus(Long memberId) {
        return query
                .select(Projections.fields(EstimateDto.Count.Res.class, estimate.status, estimate.count().as("count")))
                .from(estimate)
                .where(estimate.member.memberId.eq(memberId)) //동적쿼리
                .groupBy(estimate.status)
                .limit(1000)
                .fetch();
    }

    public List<Estimate> findAll(Long memberId) {
        return query
            .select(estimate)
            .from(estimate)
            .leftJoin(estimate.estimateProducts, estimateProduct).fetchJoin()
            .leftJoin(estimateProduct.product, product).fetchJoin()
            .leftJoin(product.productPictures, productPicture).fetchJoin()
            .where(productPicture.type.eq("THUMBNAIL").or(productPicture.isNull()))
            .where(memberIdEq(memberId))
            .orderBy(estimate.createdAt.desc())
            .limit(1000)
            .fetch();
    }

    private BooleanExpression memberIdEq(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return estimate.member.memberId.eq(memberId);
    }
}