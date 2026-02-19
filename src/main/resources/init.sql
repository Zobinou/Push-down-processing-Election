
-- ============================================================
--  Election Database – Schema & Seed Data
-- ============================================================

-- Drop existing objects (clean slate)
DROP TABLE IF EXISTS vote     CASCADE;
DROP TABLE IF EXISTS voter    CASCADE;
DROP TABLE IF EXISTS candidate CASCADE;
DROP TYPE  IF EXISTS vote_type;

-- ── Tables ──────────────────────────────────────────────────

CREATE TABLE candidate (
                           id   SERIAL PRIMARY KEY,
                           name TEXT NOT NULL
);

CREATE TABLE voter (
                       id   SERIAL PRIMARY KEY,
                       name TEXT NOT NULL
);

CREATE TYPE vote_type AS ENUM ('VALID', 'BLANK', 'NULL');

CREATE TABLE vote (
                      id           SERIAL PRIMARY KEY,
                      candidate_id INT          REFERENCES candidate(id),   -- NULL for blank/null votes
                      voter_id     INT NOT NULL REFERENCES voter(id),
                      vote_type    vote_type NOT NULL
);

-- ── Seed data ────────────────────────────────────────────────

INSERT INTO candidate (name) VALUES
                                 ('Alice'),
                                 ('Bob'),
                                 ('Charlie');

INSERT INTO voter (name) VALUES
                             ('Voter1'),
                             ('Voter2'),
                             ('Voter3'),
                             ('Voter4'),
                             ('Voter5'),
                             ('Voter6');

INSERT INTO vote (candidate_id, voter_id, vote_type) VALUES
                                                         (1, 1, 'VALID'),   -- Alice  ← Voter1
                                                         (1, 2, 'VALID'),   -- Alice  ← Voter2
                                                         (2, 3, 'VALID'),   -- Bob    ← Voter3
                                                         (2, 4, 'BLANK'),   -- Blank  ← Voter4
                                                         (NULL, 5, 'BLANK'),-- Blank  ← Voter5
                                                         (3, 6, 'NULL');    -- Null   ← Voter6

-- ── Quick verification ───────────────────────────────────────

-- Q1
SELECT COUNT(*) AS total_votes FROM vote;

-- Q2
SELECT vote_type::TEXT, COUNT(*) AS count
FROM vote
GROUP BY vote_type
ORDER BY vote_type;

-- Q3
SELECT c.name AS candidate_name,
       COUNT(v.id) FILTER (WHERE v.vote_type = 'VALID') AS valid_vote
FROM candidate c
         LEFT JOIN vote v ON v.candidate_id = c.id
GROUP BY c.name
ORDER BY valid_vote DESC;

-- Q4
SELECT COUNT(*) FILTER (WHERE vote_type = 'VALID') AS valid_count,
    COUNT(*) FILTER (WHERE vote_type = 'BLANK') AS blank_count,
    COUNT(*) FILTER (WHERE vote_type = 'NULL')  AS null_count
FROM vote;

-- Q5
SELECT COUNT(DISTINCT voter_id)::FLOAT
     / NULLIF((SELECT COUNT(*) FROM voter), 0) AS turnout_rate
FROM vote;

-- Q6
SELECT c.name AS candidate_name, COUNT(v.id) AS valid_vote_count
FROM candidate c
         JOIN vote v ON v.candidate_id = c.id AND v.vote_type = 'VALID'
GROUP BY c.name
ORDER BY valid_vote_count DESC, c.name ASC
    LIMIT 1;