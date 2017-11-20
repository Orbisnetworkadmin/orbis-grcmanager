CREATE TABLE users_profile
(
 id_profile                      BIGINT    NOT NULL REFERENCES profile (id_profile)DEFERRABLE,
 user_id                         BIGINT    NOT NULL REFERENCES users (user_id)DEFERRABLE
);

--;;
-- CREATE UNIQUE INDEX idx_support_issue_id_tag_id ON support_issues_tags(support_issue_id, tag_id);