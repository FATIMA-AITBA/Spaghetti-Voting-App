package org.example.vote.strategy;

import org.example.vote.model.Vote;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PluralityCountingStrategyTest {

    @Test
    void testStandardWin() {
        CountingStrategy strategy = new PluralityCountingStrategy();
        
        List<Vote> votes = List.of(
            new Vote("1", "Alice", 0),
            new Vote("2", "Alice", 0),
            new Vote("3", "Bob", 0)
        );

        Map<String, Integer> result = strategy.count(votes);

        assertEquals(2, result.get("Alice"), "Alice devrait avoir 2 voix");
        assertEquals(1, result.get("Bob"), "Bob devrait avoir 1 voix");
    }

    @Test
    void testEmptyList() {
        CountingStrategy strategy = new PluralityCountingStrategy();
        assertTrue(strategy.count(List.of()).isEmpty());
    }
}