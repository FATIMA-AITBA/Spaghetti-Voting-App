package org.example.vote.factory;

import org.example.vote.repo.InMemoryVoteRepository;
import org.example.vote.repo.VoteRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RepositoryFactoryTest {

    @Test
    void testCreateMemoryRepo() {
        VoteRepository repo = RepositoryFactory.createRepo("memory");
        assertTrue(repo instanceof InMemoryVoteRepository);
    }

    @Test
    void testCreateFileRepo() {
        VoteRepository repo = RepositoryFactory.createRepo("file");
        assertNotNull(repo);
    }
    @Test
    void testUnknownType() {
        assertThrows(IllegalArgumentException.class, () -> {
            RepositoryFactory.createRepo("cloud-database");
        });
    }
}