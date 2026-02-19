
package com.election;

public record CandidateVoteCount(String candidateName, long validVoteCount) {

    @Override
    public String toString() {
        return "CandidateVoteCount{candidateName=" + candidateName + ", validVoteCount=" + validVoteCount + "}";
    }
}