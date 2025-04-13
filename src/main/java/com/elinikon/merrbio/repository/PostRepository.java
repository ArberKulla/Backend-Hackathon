package com.elinikon.merrbio.repository;

import com.elinikon.merrbio.entity.Post;
import com.elinikon.merrbio.entity.PostRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Post> findByUserId(int userId, Pageable pageable);

    Page<Post> findByUserIdAndTitleContaining(int userId, String title, Pageable pageable);

    // Find by title and price range
    Page<Post> findByTitleContainingIgnoreCaseAndPriceBetween(String title, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // Find by title and minimum price
    Page<Post> findByTitleContainingIgnoreCaseAndPriceGreaterThanEqual(String title, BigDecimal minPrice, Pageable pageable);

    // Find by title and maximum price
    Page<Post> findByTitleContainingIgnoreCaseAndPriceLessThanEqual(String title, BigDecimal maxPrice, Pageable pageable);

    // Find by price range
    Page<Post> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // Find by minimum price
    Page<Post> findByPriceGreaterThanEqual(BigDecimal minPrice, Pageable pageable);

    // Find by maximum price
    Page<Post> findByPriceLessThanEqual(BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.postRequests WHERE p.user.id = :userId")
    List<Post> findByUserIdWithPostRequests(@Param("userId") int userId);

}
