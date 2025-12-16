package org.example.vote.factory;

import org.example.vote.repo.*;

public class RepositoryFactory {
    public static VoteRepository createRepo(String type) {
        if ("memory".equalsIgnoreCase(type)) {
            return new InMemoryVoteRepository();
        } 
        else if ("file".equalsIgnoreCase(type)) {
            return new FileVoteRepository();
        }
        throw new IllegalArgumentException("Unknown repo type: " + type);
    }
}