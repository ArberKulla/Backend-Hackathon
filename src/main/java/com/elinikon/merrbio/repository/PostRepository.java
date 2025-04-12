package com.elinikon.merrbio.repository;

import com.elinikon.merrbio.entity.Post;
import com.elinikon.merrbio.entity.PostRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByTitleContainingIgnoreCase(String title);

    List<Post> findByUserId(int userId);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.postRequests WHERE p.user.id = :userId")
    List<Post> findByUserIdWithPostRequests(@Param("userId") int userId);

}
