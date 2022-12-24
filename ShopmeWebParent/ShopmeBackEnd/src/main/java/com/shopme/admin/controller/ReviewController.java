package com.shopme.admin.controller;

import com.shopme.admin.service.ReviewService;
import com.shopme.common.entity.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") int id) {
        return ResponseEntity.ok(reviewService.get(id));
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public void save(Review review) {
        reviewService.save(review);
    }

    @PostMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public void edit(Review review,
                     @PathVariable("id") int id) {
        reviewService.edit(id, review);
    }

    @GetMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        reviewService.delete(id);
    }
}
