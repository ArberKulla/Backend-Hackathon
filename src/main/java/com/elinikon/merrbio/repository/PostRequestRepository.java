package com.elinikon.merrbio.repository;

import com.elinikon.merrbio.entity.Post;
import com.elinikon.merrbio.entity.PostRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRequestRepository extends JpaRepository<PostRequest, Integer> {

    List<PostRequest> findByUserId(int userId);

    List<PostRequest> findByPostId(int postId);
}
