package com.shopme.controller;

import com.shopme.common.entity.Review;
import com.shopme.security.JwtService;
import com.shopme.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private JwtService jwtService;

    private ReviewService reviewService;

    public ReviewController(JwtService jwtService, ReviewService reviewService) {
        this.jwtService = jwtService;
        this.reviewService = reviewService;
    }

    @GetMapping("/{orderDetailId}")
    public ResponseEntity get(@PathVariable("orderDetailId")
                              int orderDetailId,
                              HttpServletRequest request) {
        int customerId = getCustomerId(request);

        return ResponseEntity.ok(reviewService.get(customerId, orderDetailId));
    }

    @PostMapping("/save")
    public void save(Review review,
                               HttpServletRequest request) {
        int customerId = getCustomerId(request);
        reviewService.save(customerId, review);
    }

    @PostMapping("{id}/like")
    public void like(@PathVariable("id") int id,
                     HttpServletRequest request) {
        int customerId = getCustomerId(request);

        reviewService.like(customerId, id);
    }

    @GetMapping("/top_3_review/{productId}")
    public ResponseEntity<?> getTop3Reviews(@PathVariable("productId") int productId) {
        return ResponseEntity.ok(
                reviewService.getTop3Reviews(productId)
        );
    }

    private int getCustomerId(HttpServletRequest request) {
        String token = request.getParameter(JwtService.HEADER);

        return jwtService.getCustomerId(token);
    }
}
