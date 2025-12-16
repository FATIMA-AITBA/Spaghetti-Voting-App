package org.example.vote.strategy;

import org.example.vote.model.Vote;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class AbsoluteMajorityStrategyTest {

    @Test
    void testWinnerAbove50Percent() {
        CountingStrategy strategy = new AbsoluteMajorityStrategy();
        
        List<Vote> votes = List.of(
            new Vote("1", "Alice", 0),
            new Vote("2", "Alice", 0),
            new Vote("3", "Bob", 0)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        assertTrue(result.containsKey("Alice"), "Alice devrait gagner (66% > 50%)");
    }

    @Test
    void testNoWinnerAt50Percent() {
        CountingStrategy strategy = new AbsoluteMajorityStrategy();
        
        List<Vote> votes = List.of(
            new Vote("1", "Alice", 0),
            new Vote("2", "Bob", 0)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        assertTrue(result.isEmpty(), "Il ne devrait pas y avoir de gagnant à 50% exact");
    }
}