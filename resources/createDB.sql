drop database tweetdata;
CREATE DATABASE tweetdata;

\connect tweetdata;

CREATE TABLE profile (
    userID bigint PRIMARY KEY,
    location varchar(30),
    date date,
    lang varchar(5),
    statuses integer,
    friends integer,
    followers integer,
    favorites integer,
    verified boolean,
    timeZone varchar(30)
);

CREATE TABLE tweet (
    tweetID serial PRIMARY KEY,
    text varchar(150),
    lang varchar(5),
    date timestamp with time zone,
    retweet integer,
    favorite integer,
    profile bigint
);

CREATE TABLE hashTag (
    tweetID bigint,
    tag varchar(20)
);

CREATE TABLE lastTweet (
    progLang varchar(10),
    tweetIDinSite bigint
);

INSERT INTO lastTweet VALUES 
    ('java', 0),
    ('kotlin', 0),
    ('scala', 0),
    ('groovy', 0),
    ('cpp', 0),
    ('swift', 0),
    ('perl', 0),
    ('javascript', 0),
    ('python', 0),
    ('ruby', 0);