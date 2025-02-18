package com.uniwear.backend.repository.jpql;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uniwear.backend.dto.DeliveryCaseDto;
import com.uniwear.backend.entity.DeliveryCase;
import com.uniwear.backend.entity.Product;
import com.uniwear.backend.enums.DeliveryCaseType;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
import static com.uniwear.backend.entity.QDeliveryCase.*;
import static com.uniwear.backend.entity.QDeliveryCasePicture.*;


@Repository
public class JDeliveryCaseRepository {

    @Autowired
    private ModelMapper modelMapper;

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JDeliveryCaseRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(DeliveryCase deliveryCase) {
        em.persist(deliveryCase);
    }

    private JPAQuery<DeliveryCase> findAllQuery() {
        return query
            .select(deliveryCase)
            .from(deliveryCase)
            .leftJoin(deliveryCase.deliveryCasePictures, deliveryCasePicture)
            .fetchJoin()
            .where(deliveryCasePicture.type.eq("THUMBNAIL").or(deliveryCasePicture.isNull()));
    }

    public Page<DeliveryCaseDto.Summary.Res> findAll(Long printTypeId, String caseType, Pageable pageable) {
        List<DeliveryCaseDto.Summary.Res> result = findAllQuery()
            .where(printTypeIdEq(printTypeId), caseTypeEq(caseType))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch()
            .stream().map(p -> modelMapper.map(p, DeliveryCaseDto.Summary.Res.class)).collect(Collectors.toList());
        
        JPAQuery<Long> countQuery = query
            .select(deliveryCase.count())
            .from(deliveryCase)
            .where(printTypeIdEq(printTypeId));

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

    private BooleanExpression printTypeIdEq(Long printTypeId) {
        if (printTypeId == null) {
            return null;
        }
        return deliveryCase.deliveryCasePrintTypes.any().printType.printTypeId.eq(printTypeId);
    }

    private BooleanExpression caseTypeEq(String caseType) {
        if (caseType == null) {
            return null;
        }
        return deliveryCase.type.eq(DeliveryCaseType.find(caseType));
    }
}