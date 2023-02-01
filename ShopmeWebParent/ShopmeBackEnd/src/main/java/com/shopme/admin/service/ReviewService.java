package com.shopme.admin.service;

import com.shopme.admin.repository.ProductRepository;
import com.shopme.admin.repository.ReviewRepository;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public Review get(int id) {
        return reviewRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void save(Review review) {
        review.setId(null);
        reviewRepository.save(review);
        productRepository.updateAvgRatingAndReviewCount(
                review.getOrderDetail().getProduct().getId());
    }

    public void edit(int id, Review review) {
        review.setId(id);
        reviewRepository.save(review);
    }

    public void delete(int id) {
        reviewRepository.deleteById(id);
    }
}
