package com.example.WebStore.Service;

import com.example.WebStore.DTO.CartItemDTO;
import com.example.WebStore.DTO.CartResponseDTO;
import com.example.WebStore.model.Cart;
import com.example.WebStore.model.CartItem;
import com.example.WebStore.model.Product;
import com.example.WebStore.model.Users;
import com.example.WebStore.repository.CartItemRepository;
import com.example.WebStore.repository.CartRepository;
import com.example.WebStore.repository.ProductRepository;
import com.example.WebStore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void addProductToCart(Long userId, Long productId,
                                 int quantity) {

        if (quantity < 1) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero"
            );
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        "Usuário não encontrado."
                ));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(
                        "Produto não encontrado."
                ));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);

            cart.getItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);
    }

    @Transactional(readOnly = true)
    public CartResponseDTO getCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElse(null);

        if (cart == null) {
            return new CartResponseDTO(
                    null,
                    List.of(),
                    BigDecimal.ZERO
            );
        }

        List<CartItemDTO> items = cart.getItems()
                .stream()
                .map(item -> {

                    Product product = item.getProduct();

                    BigDecimal subtotal = product.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

                    return new CartItemDTO(
                            product.getId(),
                            product.getName(),
                            product.getImage(),
                            product.getPrice(),
                            item.getQuantity(),
                            subtotal
                    );
                })
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemDTO::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(
                cart.getId(),
                items,
                total
        );
    }
}
