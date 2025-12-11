package org.example.vote;

import org.example.vote.factory.RepositoryFactory;
import org.example.vote.model.Vote;
import org.example.vote.observer.LoggingVoteListener;
import org.example.vote.service.VoteService;
import org.example.vote.strategy.PluralityCountingStrategy;

import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args){
        var repo = RepositoryFactory.createRepo("memory");
        var svc = new VoteService(repo);
        svc.addListener(new LoggingVoteListener());

        Scanner sc = new Scanner(System.in);
        System.out.println("Commands: vote, count, exit");

        while(true){
            String cmd = sc.nextLine();
            if(cmd.equals("vote")){
                System.out.println("Voter ID:");
                String voter = sc.nextLine();
                System.out.println("Candidate ID:");
                String candidate = sc.nextLine();

                svc.cast(new Vote(voter, candidate, System.currentTimeMillis()));
            }
            else if(cmd.equals("count")){
                Map<String,Integer> r = svc.count(new PluralityCountingStrategy());
                System.out.println(r);
            }
            else if(cmd.equals("exit")){
                break;
            }
        }
        sc.close();
    }
}
