package org.example.vote.strategy;

import org.example.vote.model.Vote;
import java.util.*;

public class PluralityCountingStrategy implements CountingStrategy {

    public Map<String,Integer> count(List<Vote> votes){
        Map<String,Integer> m = new HashMap<>();
        for(var v: votes) m.merge(v.candidateId(), 1, Integer::sum);
        return m;
    }
}
