drop database tweetdata;
CREATE DATABASE tweetdata;

\connect tweetdata;

CREATE TABLE tweet (
    tweetID serial PRIMARY KEY,    
    text varchar(140),
    lang varchar(5),
    date timestamp,
    offsettime integer,
    retweet integer,
    favorite integer,
    profile bigint
);

CREATE TABLE profile (
    userID bigint,
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

CREATE TABLE hashTag (
    tweetID bigint,
    tag varchar(20)
);

CREATE TABLE lastTweet (
    langProg varchar(10),
    tweetIDinSite bigint
);

INSERT INTO lastTweet VALUES 
    ('java', 0),
    ('kotlin', 0),
    ('scala', 0),
    ('cpp', 0),
    ('c', 0),
    ('csharp', 0),
    ('javascript', 0),
    ('python', 0),
    ('ruby', 0),
    ('groovy', 0);