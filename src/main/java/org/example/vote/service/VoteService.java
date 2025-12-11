package org.example.vote.service;

import org.example.vote.model.Vote;
import org.example.vote.repo.VoteRepository;
import org.example.vote.strategy.CountingStrategy;
import org.example.vote.observer.VoteListener;
import java.util.*;

public class VoteService {

    private final VoteRepository repo;
    private final List<VoteListener> listeners = new ArrayList<>();

    public VoteService(VoteRepository repo){
        this.repo = repo;
    }

    public void addListener(VoteListener l){
        listeners.add(l);
    }

    public void cast(Vote v){
        repo.save(v);
        for(var l: listeners)
            l.onVote(v);
    }

    public Map<String,Integer> count(CountingStrategy strategy){
        return strategy.count(repo.findAll());
    }

    public void reset(){
        repo.clear();
    }
}
