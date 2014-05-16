CREATE SEQUENCE recording_ids;

CREATE TABLE recordings (
  id BIGINT default nextval('recording_ids') PRIMARY KEY,
  title text NOT NULL
);
