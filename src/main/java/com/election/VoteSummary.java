
package com.election;

/**
 * Represents a global summary of all votes in one single row.
 */
public record VoteSummary(long validCount, long blankCount, long nullCount) {

    @Override
    public String toString() {
        return "VoteSummary{validCount=" + validCount +
                ", blankCount=" + blankCount +
                ", nullCount=" + nullCount + "}";
    }
}
