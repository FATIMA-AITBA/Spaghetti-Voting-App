package org.example.vote.repo;

import org.example.vote.model.Vote;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryVoteRepositoryTest {

    @Test
    void testSaveAndFindAll() {
        VoteRepository repo = new InMemoryVoteRepository();
        Vote v = new Vote("user1", "Alice", System.currentTimeMillis());
        
        repo.save(v);
        
        List<Vote> votes = repo.findAll();
        assertEquals(1, votes.size());
        assertEquals("Alice", votes.get(0).candidateId());
    }

    @Test
    void testClear() {
        VoteRepository repo = new InMemoryVoteRepository();
        repo.save(new Vote("u1", "A", 0));
        repo.clear();
        assertTrue(repo.findAll().isEmpty());
    }
}