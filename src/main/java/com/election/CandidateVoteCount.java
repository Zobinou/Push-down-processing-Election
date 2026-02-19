
package com.election;

/**
 * Represents the number of valid votes received by a candidate.
 */
public record CandidateVoteCount(String candidateName, long validVoteCount) {

    @Override
    public String toString() {
        return "CandidateVoteCount{candidateName=" + candidateName + ", validVoteCount=" + validVoteCount + "}";
    }
}