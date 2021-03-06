CREATE DATABASE GED;

CREATE TABLE IMAGE (
	I_ID INT PRIMARY KEY AUTO_INCREMENT,
        NOM VARCHAR(255) NOT NULL,
        CHEMIN TEXT NOT NULL,
	I_DATE DATE NOT NULL,
        WIDTH INT NOT NULL,
        HEIGHT INT NOT NULL,
        SIZE INT NOT NULL,
        NOTE INT DEFAULT 0,
	DESCRIPTION TEXT NOT NULL, 
        CONSTRAINT CHK_NOTE  CHECK( NOTE BETWEEN 0 AND 5)
);

CREATE TABLE TAG (
	T_ID INT PRIMARY KEY AUTO_INCREMENT,
        NOM VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE SERIE (
	S_ID INT PRIMARY KEY AUTO_INCREMENT,
        NOM VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IMTAG (
	I_ID INT,
        T_ID INT,
        CONSTRAINT FK_I_ID FOREIGN KEY(I_ID) REFERENCES IMAGE(I_ID),
        CONSTRAINT FK_T_ID FOREIGN KEY(T_ID) REFERENCES TAG(T_ID),
        CONSTRAINT PK_IMTAG PRIMARY KEY(I_ID,T_ID)
);

CREATE TABLE IMSERIE (
	I_ID INT,
        S_ID INT,
        CONSTRAINT FK_I_ID2 FOREIGN KEY(I_ID) REFERENCES IMAGE(I_ID),
        CONSTRAINT FK_S_ID FOREIGN KEY(S_ID) REFERENCES SERIE(S_ID),
        CONSTRAINT PK_IMSERIE PRIMARY KEY(I_ID,S_ID)
);

CREATE TABLE COMPTE_FLICKR (
  
	F_ID int(11) PRIMARY KEY AUTO_INCREMENT,
  
	TOKEN varchar(255) NOT NULL

);