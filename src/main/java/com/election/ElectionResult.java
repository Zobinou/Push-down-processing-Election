package com.election;

/**
 * Represents the result of the election with the winning candidate.
 */
public record ElectionResult(String candidateName, long validVoteCount) {

    @Override
    public String toString() {
        return "ElectionResult{candidateName=" + candidateName +
                ", validVoteCount=" + validVoteCount + "}";
    }
}
