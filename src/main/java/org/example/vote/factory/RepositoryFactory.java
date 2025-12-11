package org.example.vote.factory;

import org.example.vote.repo.*;

public class RepositoryFactory {
    public static VoteRepository createRepo(String type){
        if("memory".equalsIgnoreCase(type)) return new InMemoryVoteRepository();
        throw new IllegalArgumentException("Unknown repo type " + type);
    }
}
