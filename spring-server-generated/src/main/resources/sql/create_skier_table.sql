CREATE TABLE IF NOT EXISTS %s (
  PostId INTEGER NOT NULL AUTO_INCREMENT,
  SkierId INTEGER NOT NULL,
  LyftId INTEGER NOT NULL,
  TimeOfDay INTEGER NOT NULL,
  PRIMARY KEY (PostId)
);