package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository{
    private AtomicLong postId = new AtomicLong(0L);
    private ConcurrentHashMap<Long, Post> repository = new ConcurrentHashMap<>();

    public List<Post> all() {
        return repository.values()
                .stream()
                .filter(post-> !post.getIsRemoved())
                .toList();
    }

    public Optional<Post> getById(long id) {
        var post = repository.getOrDefault(id, null);
        if (post != null) {
            if (post.getIsRemoved()) post = null;
        }
        return Optional.ofNullable(post);

    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(postId.incrementAndGet());
        }
        if (repository.put(post.getId(), post) == null) {
            postId.set(Math.max(post.getId(), postId.get()));
        }
        return post;
    }

    public void removeById(long id) {
        repository.get(id).setRemoved(true);
    }
}
