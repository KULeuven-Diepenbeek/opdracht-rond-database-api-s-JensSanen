package be.kuleuven;

import java.util.List;

import org.jdbi.v3.core.Jdbi;

public class SpelerRepositoryJDBIimpl implements SpelerRepository {
  private final Jdbi jdbi;

  // Constructor
  SpelerRepositoryJDBIimpl(String connectionString, String user, String pwd) {
    this.jdbi = Jdbi.create(connectionString, user, pwd);
  }

  @Override
  public void addSpelerToDb(Speler speler) {
    jdbi.withHandle(handle -> {
       return handle.createUpdate("INSERT INTO speler (tennisvlaanderenid, naam, punten) VALUES (:tennisvlaanderenId, :naam, :punten)")
          .bindBean(speler)
          .execute();
    });
  }

  @Override
  public Speler getSpelerByTennisvlaanderenId(int tennisvlaanderenId) {
    return (Speler) jdbi.withHandle(handle -> {
      return handle.createQuery("SELECT * FROM speler WHERE tennisvlaanderenid = :id;")
          .bind("id", tennisvlaanderenId)
          .mapToBean(Speler.class)
          .findFirst()
          .orElseThrow(() -> new InvalidSpelerException(tennisvlaanderenId + ""));
    });
  }

  @Override
  public List<Speler> getAllSpelers() {
    return jdbi.withHandle(handle -> {
      return handle.createQuery("SELECT * FROM speler;")
          .mapToBean(Speler.class)
          .list();
    }); 
  }

  @Override
  public void updateSpelerInDb(Speler speler) {
    int affectedRows = jdbi.withHandle(handle -> {
      return handle
        .createUpdate(
          "UPDATE speler SET tennisvlaanderenid = :tennisvlaanderenId, naam = :naam, punten = :punten WHERE tennisvlaanderenid = :tennisvlaanderenId;")
        .bindBean(speler)
        .execute();
    });
    if (affectedRows == 0) {
      throw new InvalidSpelerException(speler.getTennisvlaanderenId() + "");
    }
  }

  @Override
  public void deleteSpelerInDb(int tennisvlaanderenid) {
    int affectedRows = jdbi.withHandle(handle -> {
      return handle
        .createUpdate(
          "DELETE FROM speler WHERE tennisvlaanderenid = :id;")
        .bind("id", tennisvlaanderenid)
        .execute();
    });
    if (affectedRows == 0) {
      throw new InvalidSpelerException(tennisvlaanderenid + "");
    }
  }

  @Override
  public String getHoogsteRankingVanSpeler(int tennisvlaanderenid) {
    try {
      getSpelerByTennisvlaanderenId(tennisvlaanderenid); // check of speler bestaat

      return jdbi.withHandle(handle -> {
        String query = """
          SELECT wedstrijd.finale, wedstrijd.winnaar, tornooi.clubnaam
          FROM wedstrijd
          INNER JOIN speler AS speler1 ON speler1.tennisvlaanderenid = wedstrijd.speler1
          INNER JOIN speler AS speler2 ON speler2.tennisvlaanderenid = wedstrijd.speler2
          INNER JOIN tornooi ON tornooi.id = wedstrijd.tornooi
          WHERE wedstrijd.speler1 = :id OR wedstrijd.speler2 = :id
          ORDER BY wedstrijd.finale ASC
          LIMIT 1;
        """;

        return handle.createQuery(query)
          .bind("id", tennisvlaanderenid)
          .map((rs, ctx) -> {
            int finaleNumber = rs.getInt("finale");
            int winnaar = rs.getInt("winnaar");
            String clubnaam = rs.getString("clubnaam");

            if (winnaar == tennisvlaanderenid) {
              return "Hoogst geplaatst in het tornooi van " + clubnaam + " met plaats in de winst";
            } else {
              return switch (finaleNumber) {
                case 1 -> "Hoogst geplaatst in het tornooi van " + clubnaam + " met plaats in de finale";
                case 2 -> "Hoogst geplaatst in het tornooi van " + clubnaam + " met plaats in de halve finale";
                case 4 -> "Hoogst geplaatst in het tornooi van " + clubnaam + " met plaats in de kwart finale";
                default -> throw new RuntimeException("Invalid finale number: " + finaleNumber);
              };
            }
          })
          .findFirst()
          .orElse("Geen tornooigegevens gevonden voor speler met tennisvlaanderenid " + tennisvlaanderenid + ".");
      });

    } catch (InvalidSpelerException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addSpelerToTornooi(int tornooiid, int tennisvlaanderenid) {
    try {
      getSpelerByTennisvlaanderenId(tennisvlaanderenid); // Check if speler exists

      jdbi.useHandle(handle -> {
        boolean exists = handle.createQuery("SELECT * FROM tornooi WHERE id = :id")
          .bind("id", tornooiid)
          .mapTo(Integer.class)
          .findFirst()
          .isPresent();

        if (!exists) {
            throw new InvalidTornooiException(tornooiid + "");
        }

        handle.createUpdate("INSERT INTO speler_speelt_tornooi (speler, tornooi) VALUES (:speler, :tornooi)")
          .bind("speler", tennisvlaanderenid)
          .bind("tornooi", tornooiid)
          .execute();
      });

    } catch (InvalidSpelerException | InvalidTornooiException e) {
      throw new RuntimeException(e);
    } 
  }

  @Override
  public void removeSpelerFromTornooi(int tornooiid, int tennisvlaanderenid) {
    try {
      getSpelerByTennisvlaanderenId(tennisvlaanderenid); // Check if speler exists

      jdbi.useHandle(handle -> {
        boolean exists = handle.createQuery("SELECT * FROM tornooi WHERE id = :id")
          .bind("id", tornooiid)
          .mapTo(Integer.class)
          .findFirst()
          .isPresent();

        if (!exists) {
          throw new InvalidTornooiException(tornooiid + "");
        }

        handle.createUpdate("DELETE FROM speler_speelt_tornooi WHERE speler = :speler AND tornooi = :tornooi")
          .bind("speler", tennisvlaanderenid)
          .bind("tornooi", tornooiid)
          .execute();
      });

    } catch (InvalidSpelerException | InvalidTornooiException e) {
      throw new RuntimeException(e);
    }
  }
}
