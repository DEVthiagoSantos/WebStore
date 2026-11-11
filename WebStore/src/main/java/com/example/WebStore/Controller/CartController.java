package com.example.WebStore.Controller;

import com.example.WebStore.DTO.CartAddItemDTO;
import com.example.WebStore.DTO.CartResponseDTO;
import com.example.WebStore.DTO.CartUpdateItemDTO;
import com.example.WebStore.Service.CartService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(
            @AuthenticationPrincipal Jwt jwt
    ) {

        Long userId = Long.valueOf(jwt.getSubject());

        CartResponseDTO cart = cartService.getCart(userId);

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addProduct(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CartAddItemDTO request
            ) {

        Long userId = Long.valueOf(jwt.getSubject());

        cartService.addProductToCart(
                userId,
                request.productId(),
                request.quantity()
        );

        CartResponseDTO cart = cartService.getCart(userId);

        return ResponseEntity.ok(cart);
    }

    @PutMapping("/item/{productId}")
    public ResponseEntity<CartResponseDTO> updateProductQuantity(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productId,
            @Valid @RequestBody CartUpdateItemDTO request
            ) {

        Long userId = Long.valueOf(jwt.getSubject());

        cartService.updateProductQuantity(
                userId,
                productId,
                request.quantity()
        );

        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<CartResponseDTO> removeProduct(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productId
    ) {

        Long userId = Long.valueOf(jwt.getSubject());

        cartService.removeProductFromCart(userId, productId);

        return ResponseEntity.ok(cartService.getCart(userId));
    }
}

