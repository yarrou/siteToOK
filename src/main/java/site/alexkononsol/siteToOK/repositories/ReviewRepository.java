package site.alexkononsol.siteToOK.repositories;

import site.alexkononsol.siteToOK.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Review save(Review review);

    @Query(value = "SELECT * FROM review  where approved='true' order by date DESC LIMIT 5 OFFSET (:numberPage -1)*5",nativeQuery = true)
    ArrayList<Review> lastReviews(@Param("numberPage") int numberPage);

    @Query(value = "SELECT * FROM review  order by date  LIMIT 5 OFFSET (:numberPage -1)*5 ",nativeQuery = true)
    ArrayList<Review> firstReviews(@Param("numberPage") int numberPage);

    @Query(value="SELECT COUNT(id) FROM review where approved='true'",nativeQuery = true)
    int sizeTable();

    @Query(value = "SELECT * FROM review where approved='false' order by date DESC LIMIT 5 OFFSET (:numberPage -1)*5 ",nativeQuery = true)
    ArrayList<Review> lastReviewsForCheck(@Param("numberPage") int numberPage);

    @Query(value="SELECT COUNT(id) FROM review where approved='false'",nativeQuery = true)
    int sizeTableForCheck();

    @Query(value="SELECT COUNT(id) FROM review where approved='true'",nativeQuery = true)
    int sizeTableApproved();

    @Query(value="update review set approved='true' where id=:reviewId",nativeQuery = true)
    void setApproved(@Param("reviewId") long id);
}