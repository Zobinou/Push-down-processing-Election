
package com.election;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ElectionRepository {


    // Q1 – Total number of votes

    public long countAllVotes() {
        String sql = "SELECT COUNT(*) AS total_votes FROM vote";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong("total_votes");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while counting all votes", e);
        }
        return 0;
    }


    // Q2 – Number of votes per type

    public List<VoteTypeCount> countVotesByType() {
        String sql = """
                SELECT vote_type::TEXT AS vote_type,
                       COUNT(*)        AS count
                FROM vote
                GROUP BY vote_type
                ORDER BY vote_type
                """;

        List<VoteTypeCount> results = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String voteType = rs.getString("vote_type");
                long   count    = rs.getLong("count");
                results.add(new VoteTypeCount(voteType, count));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while counting votes by type", e);
        }
        return results;
    }


    // Q3 – Number of valid votes per candidate

    public List<CandidateVoteCount> countValidVotesByCandidate() {
        String sql = """
                SELECT c.name                                        AS candidate_name,
                       COUNT(v.id) FILTER (WHERE v.vote_type = 'VALID') AS valid_vote
                FROM candidate c
                LEFT JOIN vote v ON v.candidate_id = c.id
                GROUP BY c.name
                ORDER BY valid_vote DESC
                """;

        List<CandidateVoteCount> results = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String candidateName  = rs.getString("candidate_name");
                long   validVoteCount = rs.getLong("valid_vote");
                results.add(new CandidateVoteCount(candidateName, validVoteCount));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while counting valid votes by candidate", e);
        }
        return results;
    }


    // Q4 – Global vote summary (single row)

    public VoteSummary computeVoteSummary() {
        String sql = """
                SELECT COUNT(*) FILTER (WHERE vote_type = 'VALID') AS valid_count,
                       COUNT(*) FILTER (WHERE vote_type = 'BLANK') AS blank_count,
                       COUNT(*) FILTER (WHERE vote_type = 'NULL')  AS null_count
                FROM vote
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                long validCount = rs.getLong("valid_count");
                long blankCount = rs.getLong("blank_count");
                long nullCount  = rs.getLong("null_count");
                return new VoteSummary(validCount, blankCount, nullCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while computing vote summary", e);
        }
        return new VoteSummary(0, 0, 0);
    }


    // Q5 – Turnout rate


    public double computeTurnoutRate() {
        String sql = """
                SELECT COUNT(DISTINCT v.voter_id)::FLOAT
                     / NULLIF((SELECT COUNT(*) FROM voter), 0) AS turnout_rate
                FROM vote v
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("turnout_rate");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while computing turnout rate", e);
        }
        return 0.0;
    }


    // Q6 – Election winner



    public ElectionResult findWinner() {
        String sql = """
                SELECT c.name    AS candidate_name,
                       COUNT(v.id) AS valid_vote_count
                FROM candidate c
                JOIN vote v ON v.candidate_id = c.id AND v.vote_type = 'VALID'
                GROUP BY c.name
                ORDER BY valid_vote_count DESC, c.name ASC
                LIMIT 1
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String candidateName  = rs.getString("candidate_name");
                long   validVoteCount = rs.getLong("valid_vote_count");
                return new ElectionResult(candidateName, validVoteCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while finding election winner", e);
        }
        return null;
    }
}