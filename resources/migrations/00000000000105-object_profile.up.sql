CREATE TABLE objects_profile
(
 id_object                      BIGINT    NOT NULL REFERENCES objects (id_object) DEFERRABLE,
 id_profile                     BIGINT    NOT NULL REFERENCES profile (id_profile) DEFERRABLE,
 grant_object_profile           VARCHAR(255)
);

--;;
-- CREATE UNIQUE INDEX idx_support_issue_id_tag_id ON support_issues_tags(support_issue_id, tag_id);