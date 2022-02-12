package site.alexkononsol.siteToOK.service.impl;

import org.springframework.stereotype.Service;
import site.alexkononsol.siteToOK.entity.Review;
import site.alexkononsol.siteToOK.repositories.ReviewRepository;
import site.alexkononsol.siteToOK.service.ReviewService;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;

    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public int sizeTableForCheck() {
        return repository.sizeTableForCheck();
    }

    @Override
    public List<Review> lastReviewsForCheck(int page) {
        return repository.lastReviewsForCheck(page);
    }

    @Override
    public int countReviews() {
        return repository.sizeTable();
    }

    @Override
    public Review getById(Long id) {
        return repository.getOne(id);
    }

    @Override
    public void save(Review review) {
        repository.save(review);
    }

    @Override
    public void delete(Review review) {
        repository.save(review);
    }

    @Override
    public List<Review> lastReviews(int page) {
        return repository.lastReviews(page);
    }
}
