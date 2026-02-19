
package com.election;

/**
 * Represents the count of votes for a specific vote type.
 */
public record VoteTypeCount(String voteType, long count) {

    @Override
    public String toString() {
        return "VoteTypeCount{voteType=" + voteType + ", count=" + count + "}";
    }
}
