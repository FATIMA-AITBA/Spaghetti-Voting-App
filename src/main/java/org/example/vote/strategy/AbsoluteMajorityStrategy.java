package org.example.vote.strategy;

import org.example.vote.model.Vote;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbsoluteMajorityStrategy implements CountingStrategy {

    @Override
    public Map<String, Integer> count(List<Vote> votes) {

        Map<String, Integer> results = new HashMap<>();
        Map<String, Integer> finalWinner = new HashMap<>();
        
        int totalVotes = votes.size();
        if (totalVotes == 0) return finalWinner;

        for (Vote v : votes) {
            results.merge(v.candidateId(), 1, Integer::sum);
        }

        double threshold = totalVotes / 2.0;

        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            if (entry.getValue() > threshold) {
                finalWinner.put(entry.getKey(), entry.getValue());
                return finalWinner;
            }
        }

        System.out.println("  Aucune majorité absolue atteinte (" + totalVotes + " votes).");
        return finalWinner;
    }
}