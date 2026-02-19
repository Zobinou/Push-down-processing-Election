package com.election;

import java.util.List;

/**
 * Entry point – runs all election queries and prints results to the console.
 */
public class Main {

    public static void main(String[] args) {

        ElectionRepository repo = new ElectionRepository();

        // ── Q1 – Total number of votes ────────────────────────────────────────
        long totalVotes = repo.countAllVotes();
        System.out.println("totalVote=" + totalVotes);

        // ── Q2 – Votes per type ───────────────────────────────────────────────
        List<VoteTypeCount> votesByType = repo.countVotesByType();
        System.out.println(votesByType);

        // ── Q3 – Valid votes per candidate ───────────────────────────────────
        List<CandidateVoteCount> validByCandidate = repo.countValidVotesByCandidate();
        // Display like: [Alice=2, BLANK=2, NULL=1]
        validByCandidate.forEach(c ->
                System.out.println(c.candidateName() + "=" + c.validVoteCount())
        );

        // ── Q4 – Global vote summary ──────────────────────────────────────────
        VoteSummary summary = repo.computeVoteSummary();
        System.out.println(summary);

        // ── Q5 – Turnout rate ─────────────────────────────────────────────────
        double turnoutRate = repo.computeTurnoutRate();
        System.out.printf("Turnout rate: %.0f%%%n", turnoutRate * 100);

        // ── Q6 – Election winner ──────────────────────────────────────────────
        ElectionResult winner = repo.findWinner();
        if (winner != null) {
            System.out.println("Winner: " + winner);
        } else {
            System.out.println("No winner found (no valid votes).");
        }
    }
}