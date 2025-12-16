package org.example.vote.repo;

import org.example.vote.model.Vote;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileVoteRepository implements VoteRepository {
    private static final String FILE_PATH = "votes.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String HEADER = "VOTER_NAME,CANDIDATE_NAME,DATE_TIME";

    public FileVoteRepository() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                Files.createFile(Paths.get(FILE_PATH));
                Files.writeString(Paths.get(FILE_PATH), HEADER + "\n", StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Vote vote) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(vote.timestamp()), ZoneId.systemDefault());
        String formattedDate = FORMATTER.format(date);

        String line = vote.voterId() + "," + vote.candidateId() + "," + formattedDate + "\n";
        
        try {
            Files.writeString(Paths.get(FILE_PATH), line, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde fichier : " + e.getMessage());
        }
    }

    @Override
    public List<Vote> findAll() {
        List<Vote> votes = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String dateStr = parts[2];
                    LocalDateTime date = LocalDateTime.parse(dateStr, FORMATTER);
                    long timestamp = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                    votes.add(new Vote(parts[0], parts[1], timestamp));
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lecture fichier : " + e.getMessage());
        }
        return votes;
    }

    @Override
    public void clear() {
        try {
            Files.writeString(Paths.get(FILE_PATH), HEADER + "\n", StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}