package com.elinikon.merrbio.service;

import com.elinikon.merrbio.dto.PostDTO;
import com.elinikon.merrbio.dto.PostResponse;
import com.elinikon.merrbio.entity.Post;
import com.elinikon.merrbio.entity.PostRequest;
import com.elinikon.merrbio.entity.User;
import com.elinikon.merrbio.repository.PostRepository;
import com.elinikon.merrbio.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    int pageSize = 20;
    @Autowired
    PostRepository postRepository;

    public List<PostResponse> getPostsByPage(Integer page) {
        if (page==null){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Post> postsPage = postRepository.findAll(pageable);
        return postsPage.stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getDescription(),
                        post.getPrice(),
                        post.getImage(),
                        post.getUser().getFirstName() + " " + post.getUser().getLastName(),
                        post.getCreatedDate()
                ))
                .collect(Collectors.toList());
    }

    public Page<PostResponse> getPostsByTitleAndPrice(String title, Integer page, BigDecimal minPrice, BigDecimal maxPrice) {
        if (page == null) {
            page = 1; // Default to page 1 if not provided
        }

        Pageable pageable = PageRequest.of(page, pageSize); // Page size is defined elsewhere

        Page<Post> postPage;

        // If no title filter, return all posts with pagination
        if ((title == null || title.isEmpty()) && (minPrice == null && maxPrice == null)) {
            postPage = postRepository.findAll(pageable);
        } else {
            // Apply filters: title and/or price
            if (title != null && !title.isEmpty()) {
                // Filter by title
                if (minPrice != null && maxPrice != null) {
                    // Filter by title and price range
                    postPage = postRepository.findByTitleContainingIgnoreCaseAndPriceBetween(title, minPrice, maxPrice, pageable);
                } else if (minPrice != null) {
                    // Filter by title and minimum price
                    postPage = postRepository.findByTitleContainingIgnoreCaseAndPriceGreaterThanEqual(title, minPrice, pageable);
                } else if (maxPrice != null) {
                    // Filter by title and maximum price
                    postPage = postRepository.findByTitleContainingIgnoreCaseAndPriceLessThanEqual(title, maxPrice, pageable);
                } else {
                    // Only filter by title
                    postPage = postRepository.findByTitleContainingIgnoreCase(title, pageable);
                }
            } else {
                // Only filter by price
                if (minPrice != null && maxPrice != null) {
                    postPage = postRepository.findByPriceBetween(minPrice, maxPrice, pageable);
                } else if (minPrice != null) {
                    postPage = postRepository.findByPriceGreaterThanEqual(minPrice, pageable);
                } else if (maxPrice != null) {
                    postPage = postRepository.findByPriceLessThanEqual(maxPrice, pageable);
                } else {
                    postPage = postRepository.findAll(pageable);
                }
            }
        }

        return postPage.map(post -> new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getPrice(),
                post.getImage(),
                post.getUser().getFirstName() + " " + post.getUser().getLastName(),
                post.getCreatedDate()
        ));
    }



    public List<Post> getFromUser(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        return postRepository.findByUserId(user.getId());
    }

    @Transactional
    public Post create(PostDTO postDTO, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        ModelMapper modelMapper = new ModelMapper();
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(user);

        return post;
    }

    @Transactional
    public Post update(int id, PostDTO postDTO, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Post current = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));

        if(current.getUser()!=user){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot edit this post");
        }

        current.setDescription(postDTO.getDescription());
        current.setTitle(postDTO.getTitle());
        current.setPrice(postDTO.getPrice());
        current.setImage(postDTO.getImage());

        return postRepository.save(current);
    }

    @Transactional
    public void deleteById(int id, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Post current = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));

        if(current.getUser()!=user){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this post");
        }

        postRepository.deleteById(id);
    }
}
