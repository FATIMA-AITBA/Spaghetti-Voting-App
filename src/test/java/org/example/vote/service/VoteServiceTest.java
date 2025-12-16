package org.example.vote.service;

import org.example.vote.model.Vote;
import org.example.vote.repo.InMemoryVoteRepository;
import org.example.vote.repo.VoteRepository;
import org.example.vote.strategy.PluralityCountingStrategy;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class VoteServiceTest {

    @Test
    void testCastAndCount() {
        VoteRepository repo = new InMemoryVoteRepository();
        VoteService service = new VoteService(repo);

        service.cast(new Vote("user1", "Alice", System.currentTimeMillis()));

        assertEquals(1, repo.findAll().size());

        Map<String, Integer> result = service.count(new PluralityCountingStrategy());
        assertEquals(1, result.get("Alice"));
    }
}
