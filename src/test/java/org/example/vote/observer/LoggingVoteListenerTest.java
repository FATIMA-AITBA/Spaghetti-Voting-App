package org.example.vote.observer;

import org.example.vote.model.Vote;
import org.junit.jupiter.api.Test;

class LoggingVoteListenerTest {

    @Test
    void testOnVote() {
        LoggingVoteListener listener = new LoggingVoteListener();
        Vote v = new Vote("tester", "Bob", System.currentTimeMillis());
        
        listener.onVote(v);
        
    }
}