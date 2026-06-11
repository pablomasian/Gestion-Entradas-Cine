DROP TABLE OrderTable;
DROP TABLE Session;
DROP TABLE Movie;
DROP TABLE Room;
DROP TABLE User;

CREATE TABLE User (
    id BIGINT NOT NULL AUTO_INCREMENT,
    userName VARCHAR(60) COLLATE latin1_bin NOT NULL,
    password VARCHAR(60) NOT NULL, 
    firstName VARCHAR(60) NOT NULL,
    lastName VARCHAR(60) NOT NULL, 
    email VARCHAR(60) NOT NULL,
    role TINYINT NOT NULL,
    CONSTRAINT UserPK PRIMARY KEY (id),
    CONSTRAINT UserNameUniqueKey UNIQUE (userName)
) ENGINE = InnoDB;

CREATE INDEX UserIndexByUserName ON User (userName);

CREATE TABLE Movie (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(60),
    resume VARCHAR(255),
    duration INT,
    CONSTRAINT MoviePK PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX MovieIndexByTitle ON Movie (title);

CREATE TABLE Room (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60),
    capacity INT,
    CONSTRAINT RoomPK PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX RoomIndexByName ON Room (name);

CREATE TABLE Session (
    id BIGINT NOT NULL AUTO_INCREMENT,
    price DECIMAL(10,2),
    date DATETIME,
    localitiesLeft INT,
    version BIGINT NOT NULL,
    movieId BIGINT NOT NULL,
    roomId BIGINT NOT NULL,

    CONSTRAINT SessionPK PRIMARY KEY (id),

    CONSTRAINT SessionMovieFK
        FOREIGN KEY (movieId)
            REFERENCES Movie(id),

    CONSTRAINT SessionRoomFK
        FOREIGN KEY (roomId)
            REFERENCES Room(id)

) ENGINE=InnoDB;

CREATE TABLE OrderTable (
    id BIGINT NOT NULL AUTO_INCREMENT,
    userId BIGINT NOT NULL,
    sessionId BIGINT NOT NULL,
    quantity INT,
    creditCardNumber VARCHAR(32) NOT NULL,
    date DATETIME,
    collectedTickets BOOLEAN,

    CONSTRAINT OrderTablePK PRIMARY KEY (id),

    CONSTRAINT OrderTableUserFK
        FOREIGN KEY (userId)
            REFERENCES User(id),

    CONSTRAINT OrderTableSessionFK
        FOREIGN KEY (sessionId)
            REFERENCES Session(id)

) ENGINE=InnoDB;