package com.uniwear.backend.repository.jpql;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uniwear.backend.dto.ProductDto;
import com.uniwear.backend.entity.Product;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.uniwear.backend.entity.QProduct.*;
import static com.uniwear.backend.entity.QCategory.*;
import static com.uniwear.backend.entity.QProductTag.*;
import static com.uniwear.backend.entity.QTag.*;
import static com.uniwear.backend.entity.QProductPrintType.*;
import static com.uniwear.backend.entity.QPrintType.*;
import static com.uniwear.backend.entity.QSubProduct.*;
import static com.uniwear.backend.entity.QProductOptionGroup.*;
import static com.uniwear.backend.entity.QProductOption.*;
import static com.uniwear.backend.entity.QProductPicture.*;

import com.querydsl.jpa.JPAExpressions;

@Repository
public class JProductRepository {

    @Autowired
    private EntityManager em;

    @Autowired
    private JPAQueryFactory query;

    @Autowired
    private ModelMapper modelMapper;

    public void save(Product product) {
        em.persist(product);
    }

    private JPAQuery<Product> findAllQuery() {
        return query
                .select(product)
                .from(product)
                .leftJoin(product.productPictures, productPicture)
                .fetchJoin()
                .where(productPicture.type.eq("THUMBNAIL").or(productPicture.isNull()));
    }

    public Page<ProductDto.Summary.Res> findAll(Long categoryId, List<String> tags, Integer goe, Integer loe, String order, Pageable pageable) {
        List<ProductDto.Summary.Res> result = findAllQuery()
            .where(categoryIdEq(categoryId), tagsEq(tags), goeEq(goe), loeEq(loe))
            .orderBy(orderBy(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch()
            .stream().map(p -> modelMapper.map(p, ProductDto.Summary.Res.class)).collect(Collectors.toList());
        
        JPAQuery<Long> countQuery = query
            .select(product.count())
            .from(product)
            .where(categoryIdEq(categoryId), tagsEq(tags), goeEq(goe), loeEq(loe))
            .orderBy(orderBy(pageable.getSort()).stream().toArray(OrderSpecifier[]::new));

        return PageableExecutionUtils.getPage(result, pageable, () -> (countQuery.fetchOne()));
    }

    public List<Product> findOne(Long id) {
        return query
                .select(product)
                .from(product)
                .join(product.category, category)
                .join(product.productTag, productTag)
                .join(productTag.tag, tag)
                .join(product.productPrintType, productPrintType)
                .join(productPrintType.printType, printType)
                .join(product.subProduct, subProduct)
                .join(product.productOptionGroup, productOptionGroup)
                .join(productOptionGroup.productOption, productOption)
                .where(idEq(id)) // 동적쿼리
                .orderBy(productOptionGroup.order.asc(), productOption.order.asc())
                .limit(1000)
                .fetch();
    }

    private BooleanExpression idEq(Long id) {
        if (id == null) {
            return null;
        }
        return product.productId.eq(id);
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return product.productCategories.any().category.categoryId.eq(categoryId);
    }

    private BooleanExpression tagsEq(List<String> tags) {
        if (tags == null || tags.size() == 0) {
            return null;
        }
        return (
            JPAExpressions.select(productTag.count()).from(productTag).leftJoin(productTag.tag, tag).where(tag.name.in(tags))
            .where(product.productId.eq(productTag.product.productId))
        ).goe(Long.valueOf(tags.size()));
    }

    private BooleanExpression goeEq(Integer goe) {
        if (goe == null) {
            return null;
        }
        return product.groupSalePrice.goe(goe);
    }

    private BooleanExpression loeEq(Integer loe) {
        if (loe == null) {
            return null;
        }
        return product.groupSalePrice.loe(loe);
    }

    private List<OrderSpecifier<?>> orderBy(Sort sort) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        if (!sort.isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : sort) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                switch (order.getProperty()){
                    case "low_price":
                        orders.add(product.groupSalePrice.asc());
                    case "high_price":
                        orders.add(product.groupSalePrice.desc());
                    case "popular":
                        orders.add(product.sellCount.desc());
                    case "review":
                        orders.add(product.reviewCount.desc());
                }
            }
        }
        return orders;
    }
}