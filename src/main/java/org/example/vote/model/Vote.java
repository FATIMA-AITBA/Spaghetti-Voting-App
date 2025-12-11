package org.example.vote.model;


public record Vote(String voterId, String candidateId, long timestamp) {}
