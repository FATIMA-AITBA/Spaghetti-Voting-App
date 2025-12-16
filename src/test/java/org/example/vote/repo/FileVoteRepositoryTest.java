package org.example.vote.repo;

import org.example.vote.model.Vote;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileVoteRepositoryTest {

    @Test
    void testSaveAndRead() {
        VoteRepository repo = new FileVoteRepository(); 
        
        Vote v = new Vote("testUser", "Alice", System.currentTimeMillis());
        repo.save(v);
        
        List<Vote> votes = repo.findAll();
        assertFalse(votes.isEmpty());
        
        Vote lastVote = votes.get(votes.size() - 1);
        assertEquals("Alice", lastVote.candidateId());
    }
}