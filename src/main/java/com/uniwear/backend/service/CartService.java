package com.uniwear.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.uniwear.backend.dto.CartDto;
import com.uniwear.backend.entity.Cart;
import com.uniwear.backend.entity.CartProductGroup;
import com.uniwear.backend.entity.CartProductOption;
import com.uniwear.backend.entity.Product;
import com.uniwear.backend.entity.ProductOption;
import com.uniwear.backend.entity.member.Member;
import com.uniwear.backend.repository.CartProductGroupRepository;
import com.uniwear.backend.repository.CartRepository;
import com.uniwear.backend.repository.MemberRepository;
import com.uniwear.backend.repository.ProductOptionRepository;
import com.uniwear.backend.repository.ProductRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private CartProductGroupRepository cartProductGroupRepository;

    public List<CartDto.Res> getCartList(Long memberId, List<Long> ids) {
        return cartRepository.findByCartIdInAndMemberMemberId(ids, memberId).stream().map(p -> modelMapper.map(p, CartDto.Res.class))
                .collect(Collectors.toList());
    }

    public List<CartDto.Res> getCartList(Long memberId) {
        return cartRepository.findAllByMemberMemberId(memberId).stream().map(p -> modelMapper.map(p, CartDto.Res.class))
                .collect(Collectors.toList());
    }

    public Long getCartListCount(Long memberId) {
        return cartRepository.countByMemberMemberId(memberId);
    }

    // 인쇄없는 상품 INSERT
    // 사용자ID, 상품ID, 인쇄타입 조회해서 있으면 update, 없으면 insert
    public boolean insertCart(CartDto.Req request) {
        // 중복 데이터 체크
        // 기존에는 save할 때 JPA가 알아서 체크하지만 사용자ID, 상품ID, 인쇄타입N 에 대한 중복체크를 해야해서 직접 구현
        Cart cart =
        cartRepository.findByProductProductIdAndMemberMemberIdAndPackageType(request.getProductId(),
        request.getMemberId(), request.getPackageType());

        // 중복 데이터가 아니면 새로 생성
        if (cart == null) {
            Member member = memberRepository.getById(request.getMemberId());
            Product product = productRepository.getById(request.getProductId());

            cart = new Cart(member, product, request.getPackageType(), request.getPrintType());
            cartRepository.save(cart);  // cartId를 필요로 하므로 한번 save
        }

        for (CartDto.Req.CartProductGroup cpg : request.getCartProductGroup()) {
            
            // 중복체크
            // 카트내에 옵션id가 전부일치하는 상품 그룹 조회
            CartProductGroup cartProductGroup =
            cartProductGroupRepository.findByCartIdAndProductOptionIds(cart.getCartId(),
                cpg.getCartProductOption(), cpg.getCartProductOption().size());
            
            // 없을 경우 새 상품그룹 할당하고 옵션 매핑
            if (cartProductGroup == null) {
                cartProductGroup = new CartProductGroup();

                List<ProductOption> productOptions = productOptionRepository.findAllById(cpg.getCartProductOption());
                for (ProductOption po : productOptions) {
                    CartProductOption cartProductOption = new CartProductOption();
                    cartProductOption.setProductOption(po);
                    cartProductGroup.addCartProductOption(cartProductOption);
                }
                cart.addCartProductGroup(cartProductGroup);
            }
            cartProductGroup.setCount(cartProductGroup.getCount() + cpg.getCount());
        }
        // cart.addCartProductGroup(new CartProductGroup());
        cartRepository.save(cart);

        return true;
    }

    public int getCombinedPrice(List<Long> ids) {
        List<Cart> cartList = cartRepository.findAllById(ids);
        int price = 0;
        
        for (Cart cart : cartList) {
            int additionalCost = 0; // 옵션별 추가금액 합산
            int count = 0;
            // 
            for (CartProductGroup cpg : cart.getCartProductGroup()) {
                int ac = 0;
                for (CartProductOption cpo : cpg.getCartProductOption()) {
                    ac += cpo.getProductOption().getAdditionalCost();
                }
                count += cpg.getCount();
                additionalCost += cpg.getCount() * ac;
            }

            boolean groupDcFlag = cart.getProduct().getGroupDiscountStandard() <= getCombinedProductNumber(cart);

            price += count * ((groupDcFlag ? cart.getProduct().getGroupSalePrice() : cart.getProduct().getSalePrice())) + additionalCost + 3000;
        }

        return price;
    }

    public int getCombinedProductNumber(Cart cart) {
        int count = 0;
        for (CartProductGroup cpg : cart.getCartProductGroup()) {
            count += cpg.getCount();
        }
        return count;
    }

    public boolean deleteCarts(List<Long> ids) {
        cartRepository.deleteAllById(ids);
        
        return true;
    }
}