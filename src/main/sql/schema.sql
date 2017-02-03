DROP SCHEMA IF EXISTS lifecollage_00_01_00;
CREATE SCHEMA IF NOT EXISTS lifecollage_00_01_00;
USE lifecollage_00_01_00;


-- tables
-- Table: application_user
CREATE TABLE application_user (
  id         INT          NOT NULL AUTO_INCREMENT,
  email      VARCHAR(200) NOT NULL,
  password   VARCHAR(200) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name  VARCHAR(100) NOT NULL,
  username   VARCHAR(100) NOT NULL,
  created    DATETIME     NOT NULL DEFAULT NOW(),
  updated    DATETIME     NOT NULL DEFAULT NOW(),
  deleted    DATETIME     NULL,
  UNIQUE INDEX email (email),
  UNIQUE INDEX username (username),
  CONSTRAINT application_user_pk PRIMARY KEY (id)
);

-- Table: collage
CREATE TABLE collage (
  id                  INT          NOT NULL AUTO_INCREMENT,
  title               VARCHAR(100) NOT NULL,
  created             DATETIME     NOT NULL DEFAULT NOW(),
  application_user_id INT          NOT NULL,
  CONSTRAINT collage_pk PRIMARY KEY (id)
);

-- Table: picture
CREATE TABLE picture (
  id         INT          NOT NULL AUTO_INCREMENT,
  location   VARCHAR(250) NOT NULL,
  created    DATETIME     NOT NULL DEFAULT NOW(),
  collage_id INT          NOT NULL,
  CONSTRAINT picture_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: collage_application_user (table: collage)
ALTER TABLE collage ADD CONSTRAINT collage_application_user FOREIGN KEY collage_application_user (application_user_id)
REFERENCES application_user (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

-- Reference: picture_collage (table: picture)
ALTER TABLE picture ADD CONSTRAINT picture_collage FOREIGN KEY picture_collage (collage_id)
REFERENCES collage (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

-- End of file.

