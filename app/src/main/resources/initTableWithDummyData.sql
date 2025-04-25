DROP TABLE IF EXISTS speler_speelt_tornooi;
DROP TABLE IF EXISTS speler;
DROP TABLE IF EXISTS wedstrijd;
DROP TABLE IF EXISTS tornooi;

CREATE TABLE tornooi(
    id INT NOT NULL PRIMARY KEY,
    clubnaam VARCHAR(200) NOT NULL
);

CREATE TABLE speler(
    tennisvlaanderenid INT NOT NULL PRIMARY KEY,
    naam VARCHAR(200) NOT NULL,
    punten INT NOT NULL
);

CREATE TABLE wedstrijd(
    id INT AUTO_INCREMENT PRIMARY KEY,
    tornooi INT NOT NULL,
    speler1 INT ,
    speler2 INT ,
    winnaar INT,
    score VARCHAR(200),
    finale INT NOT NULL,
    FOREIGN KEY (tornooi) REFERENCES tornooi(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (speler1) REFERENCES speler(tennisvlaanderenid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (speler2) REFERENCES speler(tennisvlaanderenid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (winnaar) REFERENCES speler(tennisvlaanderenid) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE speler_speelt_tornooi(
    id INT AUTO_INCREMENT PRIMARY KEY,
    speler INT NOT NULL,
    tornooi INT NOT NULL,
    FOREIGN KEY (speler) REFERENCES speler(tennisvlaanderenid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (tornooi) REFERENCES tornooi(id) ON DELETE CASCADE ON UPDATE CASCADE
);


INSERT INTO tornooi(id, clubnaam) VALUES (1, 'T.P.Tessenderlo');
INSERT INTO tornooi(id, clubnaam) VALUES (2, 'T.C.Diest');
INSERT INTO tornooi(id, clubnaam) VALUES (3, 'T.C.Laakdal');

INSERT INTO speler(tennisvlaanderenid, naam, punten) VALUES (1, 'Rafael Nadal', 3);
INSERT INTO speler(tennisvlaanderenid, naam, punten) VALUES (2, 'Roger Federer', 1);
INSERT INTO speler(tennisvlaanderenid, naam, punten) VALUES (3, 'Stefano Tsisipas', 4);
INSERT INTO speler(tennisvlaanderenid, naam, punten) VALUES (4, 'Carlos Alcaraz', 5);
INSERT INTO speler(tennisvlaanderenid, naam, punten) VALUES (5, 'Kim Clijsters', 8);
INSERT INTO speler(tennisvlaanderenid, naam, punten) VALUES (6, 'Justine Henin', 9);
INSERT INTO speler(tennisvlaanderenid, naam, punten) VALUES (7, 'Serena Wiliams', 2);
INSERT INTO speler(tennisvlaanderenid, naam, punten) VALUES (8, 'Maria Sharapova', 3);


INSERT INTO wedstrijd(tornooi, speler1, speler2, winnaar, score, finale) VALUES (1, 1, 2, 2, '6-4, 6-2', 2);
INSERT INTO wedstrijd(tornooi, speler1, speler2, winnaar, score, finale) VALUES (1, 3, 4, 4, '6-4, 5-7, 6-0', 2);
INSERT INTO wedstrijd(tornooi, speler1, speler2, winnaar, score, finale) VALUES (1, 2, 4, 4, '6-1, 6-4', 1);
INSERT INTO wedstrijd(tornooi, speler1, speler2, winnaar, score, finale) VALUES (2, 5, 6, 5, '6-0, 6-1', 2);
INSERT INTO wedstrijd(tornooi, speler1, speler2, winnaar, score, finale) VALUES (2, 7, 8, 7, '7-6(3), 6-4', 2);
INSERT INTO wedstrijd(tornooi, speler1, speler2, winnaar, score, finale) VALUES (2, 5, 7, 5, '6-0, 6-0', 1);


INSERT INTO speler_speelt_tornooi(speler, tornooi) VALUES (1, 1);
INSERT INTO speler_speelt_tornooi(speler, tornooi) VALUES (2, 1);
INSERT INTO speler_speelt_tornooi(speler, tornooi) VALUES (3, 1);
INSERT INTO speler_speelt_tornooi(speler, tornooi) VALUES (4, 1);
INSERT INTO speler_speelt_tornooi(speler, tornooi) VALUES (5, 2);
INSERT INTO speler_speelt_tornooi(speler, tornooi) VALUES (6, 2);
INSERT INTO speler_speelt_tornooi(speler, tornooi) VALUES (7, 2);
INSERT INTO speler_speelt_tornooi(speler, tornooi) VALUES (8, 2);
