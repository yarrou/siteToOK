package site.alexkononsol.siteToOK.service;

import site.alexkononsol.siteToOK.entity.Review;

import java.util.List;

public interface ReviewService {
    int sizeTableForCheck();
    List<Review> lastReviewsForCheck(int page);
    int countReviews();
    Review getById(Long id);
    void save(Review review);
    void delete(Review review);
    List<Review> lastReviews(int page);
}
