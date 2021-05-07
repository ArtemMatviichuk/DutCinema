ALTER TABLE movies MODIFY COLUMN description varchar(1500);
ALTER TABLE movies ALTER trailer_path SET DEFAULT "none";
ALTER TABLE movies ADD actors varchar(255) null;
ALTER TABLE movies ADD genres varchar(255) null;
ALTER TABLE movies ADD country varchar(255) null;