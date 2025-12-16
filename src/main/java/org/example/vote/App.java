package org.example.vote;

import org.example.vote.factory.RepositoryFactory;
import org.example.vote.model.Vote;
import org.example.vote.observer.LoggingVoteListener;
import org.example.vote.service.VoteService;
import org.example.vote.strategy.AbsoluteMajorityStrategy;
import org.example.vote.strategy.PluralityCountingStrategy;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    private static final List<String> OFFICIAL_CANDIDATES = List.of("Alice", "Bob", "Charlie");

    public static void main(String[] args) {
        var repo = RepositoryFactory.createRepo("file"); 
        var svc = new VoteService(repo);
        svc.addListener(new LoggingVoteListener());

        Scanner sc = new Scanner(System.in);
        System.out.println("=== SYSTÈME DE VOTE ===");
        System.out.println("Commandes disponibles : 'vote', 'count', 'exit'");

        while (true) {
            System.out.print("> ");
            String cmd = sc.nextLine();

            if (cmd.equals("vote")) {
                System.out.print("Nom du votant : ");
                String voter = sc.nextLine();
                
                System.out.println("--- Choisissez un candidat ---");
                for (int i = 0; i < OFFICIAL_CANDIDATES.size(); i++) {
                    System.out.println((i + 1) + ". " + OFFICIAL_CANDIDATES.get(i));
                }

                System.out.print("Entrez le NUMÉRO du candidat : ");
                String inputId = sc.nextLine();

                try {
                    int id = Integer.parseInt(inputId);

                    if (id >= 1 && id <= OFFICIAL_CANDIDATES.size()) {
                        
                        String selectedName = OFFICIAL_CANDIDATES.get(id - 1);
                        
                        svc.cast(new Vote(voter, selectedName, System.currentTimeMillis()));
                        System.out.println("✅ Vote enregistré pour : " + selectedName);
                    
                    } else {
                        System.out.println("❌ Numéro invalide. Veuillez choisir entre 1 et " + OFFICIAL_CANDIDATES.size());
                    }

                } catch (NumberFormatException e) {
                    System.out.println("❌ Erreur : Vous devez entrer un chiffre !");
                }
            } 
            else if (cmd.equals("count")) {
                System.out.println("\n--- RÉSULTATS DÉTAILLÉS ---");
                
                Map<String, Integer> r1 = svc.count(new PluralityCountingStrategy());
                
                System.out.println("📊 Scores complets (Plurality) : " + r1);
                if (!r1.isEmpty()) {
                    int maxVotes = java.util.Collections.max(r1.values());
                    List<String> winners = new java.util.ArrayList<>();
                    r1.forEach((k, v) -> {
                        if (v == maxVotes) winners.add(k);
                    });
                    System.out.println("🏆 GAGNANT(S) : " + winners + " avec " + maxVotes + " voix.");
                } else {
                    System.out.println("   (Aucun vote enregistré)");
                }

                System.out.println("-----------------------------");

                Map<String, Integer> r2 = svc.count(new AbsoluteMajorityStrategy());
                System.out.println("⚖️  Majorité Absolue (>50%)   : " + (r2.isEmpty() ? "Pas de gagnant" : r2.keySet()));
                
                System.out.println("-----------------------------\n");
            }
            else if (cmd.equals("exit")) {
                System.out.println("Fermeture.");
                break;
            } 
            else {
                System.out.println("Commande inconnue.");
            }
        }
        sc.close();
    }
}