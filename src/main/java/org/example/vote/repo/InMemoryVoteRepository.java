package org.example.vote.repo;

import org.example.vote.model.Vote;
import java.util.*;

public class InMemoryVoteRepository implements VoteRepository {
    private final List<Vote> store = Collections.synchronizedList(new ArrayList<>());

    public void save(Vote vote){ store.add(vote); }
    public List<Vote> findAll(){ return new ArrayList<>(store); }
    public void clear(){ store.clear(); }
}
