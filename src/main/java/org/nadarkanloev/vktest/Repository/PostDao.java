package org.nadarkanloev.vktest.Repository;

import org.nadarkanloev.vktest.Model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostDao {

    public static final String HASH_KEY = "Post";

    @Autowired
    private RedisTemplate template;

    public Post save(Post post){
        template.opsForHash().put(HASH_KEY, post.getId(), post);
        return post;
    }
    public List<Post> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }

    public Post findPostById(int id){
        return (Post) template.opsForHash().get(HASH_KEY,id);
    }
}
