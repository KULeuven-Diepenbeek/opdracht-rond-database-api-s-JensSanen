package be.kuleuven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpelerRepositoryJDBCimpl implements SpelerRepository {
  private final Connection connection;

  // Constructor
  SpelerRepositoryJDBCimpl(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return connection;
  }

  @Override
  public void addSpelerToDb(Speler speler) {
    try {
      // Deze tweede try is een try-with-resources. Dit zorgt ervoor dat de prepared statement automatisch gesloten wordt ook als er een exception optreedt.
      try (PreparedStatement prepared = (PreparedStatement) connection 
              .prepareStatement("INSERT INTO speler (tennisvlaanderenid, naam, punten) VALUES (?, ?, ?);")) {
          prepared.setInt(1, speler.getTennisvlaanderenId()); // First questionmark
          prepared.setString(2, speler.getNaam()); // Second questionmark
          prepared.setInt(3, speler.getPunten()); // Third questionmark
          prepared.executeUpdate();
      } 
      connection.commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Speler getSpelerByTennisvlaanderenId(int tennisvlaanderenId) {
    Speler foundSpeler = null;
    try {
      try (PreparedStatement prepared = (PreparedStatement) connection
              .prepareStatement("SELECT * FROM speler WHERE tennisvlaanderenid = ?;")) {
          prepared.setInt(1, tennisvlaanderenId);
          ResultSet result = prepared.executeQuery();
          
          while (result.next()) {
              int tennisvlaanderenid = result.getInt("tennisvlaanderenid");
              String naam = result.getString("naam");
              int punten = result.getInt("punten");
              
              foundSpeler = new Speler(tennisvlaanderenid, naam, punten);
          }
          
          if (foundSpeler == null) {
              throw new InvalidSpelerException(tennisvlaanderenId + "");
          }
          result.close();
      }
    connection.commit();

    } catch (InvalidSpelerException | SQLException e) {
      throw new RuntimeException(e);
    }
    return foundSpeler;
  }

  @Override
  public List<Speler> getAllSpelers() {
    List<Speler> foundSpelers = new ArrayList<>();
    try {
      try (PreparedStatement prepared = (PreparedStatement) connection
              .prepareStatement("SELECT * FROM speler;"); 
              ResultSet result = prepared.executeQuery()) {
          
          while (result.next()) {
              int tennisvlaanderenid = result.getInt("tennisvlaanderenid");
              String naam = result.getString("naam");
              int punten = result.getInt("punten");
            
              foundSpelers.add(new Speler(tennisvlaanderenid, naam, punten));  
          }
      }
    connection.commit();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return foundSpelers;
  }

  @Override
  public void updateSpelerInDb(Speler speler) {
    try {
      getSpelerByTennisvlaanderenId(speler.getTennisvlaanderenId()); // Check if speler exists

      try (PreparedStatement prepared = (PreparedStatement) connection
              .prepareStatement("UPDATE speler SET naam = ?, punten = ? WHERE tennisvlaanderenid = ?;")) {
          prepared.setString(1, speler.getNaam()); 
          prepared.setInt(2, speler.getPunten());
          prepared.setInt(3, speler.getTennisvlaanderenId());
          prepared.executeUpdate();
      } 
      connection.commit();
    } catch (InvalidSpelerException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteSpelerInDb(int tennisvlaanderenid) {
    try {
      getSpelerByTennisvlaanderenId(tennisvlaanderenid); 

      try (PreparedStatement prepared = (PreparedStatement) connection
              .prepareStatement("DELETE FROM speler WHERE tennisvlaanderenid = ?;")) {
          prepared.setInt(1, tennisvlaanderenid); 
          prepared.executeUpdate();
      } 
      connection.commit();
    } catch (InvalidSpelerException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getHoogsteRankingVanSpeler(int tennisvlaanderenid) {
      try {
          getSpelerByTennisvlaanderenId(tennisvlaanderenid); 

          String query = """
              SELECT wedstrijd.finale, wedstrijd.winnaar, tornooi.clubnaam
              FROM wedstrijd
              INNER JOIN speler AS speler1 ON speler1.tennisvlaanderenid = wedstrijd.speler1
              INNER JOIN speler AS speler2 ON speler2.tennisvlaanderenid = wedstrijd.speler2
              INNER JOIN tornooi ON tornooi.id = wedstrijd.tornooi
              WHERE wedstrijd.speler1 = ? OR wedstrijd.speler2 = ?
              ORDER BY wedstrijd.finale ASC
              LIMIT 1;
          """;

          try (PreparedStatement prepared = connection.prepareStatement(query)) {
              prepared.setInt(1, tennisvlaanderenid);
              prepared.setInt(2, tennisvlaanderenid); 
              ResultSet result = prepared.executeQuery();

              if (result.next()) {
                  int finaleNumber = result.getInt("finale");
                  int winnaar = result.getInt("winnaar");
                  String clubnaam = result.getString("clubnaam");

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
              } else {
                  return "Geen tornooigegevens gevonden voor speler met tennisvlaanderenid " + tennisvlaanderenid + ".";   
              }
          }
      } catch (InvalidSpelerException | SQLException e) {
          throw new RuntimeException(e);
      }
  }


  @Override
  public void addSpelerToTornooi(int tornooiid, int tennisvlaanderenid) {
    try {
      getSpelerByTennisvlaanderenId(tennisvlaanderenid); // Check if speler exists

      try (PreparedStatement preparedTornooi = connection.prepareStatement("SELECT * FROM tornooi WHERE id = ?;")) {
          preparedTornooi.setInt(1, tornooiid);
          try (ResultSet result = preparedTornooi.executeQuery()) {
              if (!result.next()) {
                  throw new InvalidTornooiException(tornooiid + "");
              }
          }
      }

      try (PreparedStatement preparedInsert = connection.prepareStatement("INSERT INTO speler_speelt_tornooi (speler, tornooi) VALUES (?, ?);")) {
          preparedInsert.setInt(1, tennisvlaanderenid); 
          preparedInsert.setInt(2, tornooiid); 
          preparedInsert.executeUpdate();
      }
      connection.commit();
    } catch (InvalidSpelerException | InvalidTornooiException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void removeSpelerFromTornooi(int tornooiid, int tennisvlaanderenid) {
    try {
      getSpelerByTennisvlaanderenId(tennisvlaanderenid); // Check if speler exists

      try (PreparedStatement preparedTornooi = connection.prepareStatement("SELECT * FROM tornooi WHERE id = ?;")) {
          preparedTornooi.setInt(1, tornooiid);
          try (ResultSet result = preparedTornooi.executeQuery()) {
              if (!result.next()) {
                  throw new InvalidTornooiException("Invalid Tornooi met identification: " + tornooiid);
              }
          }
      }

      try (PreparedStatement preparedInsert = connection.prepareStatement("DELETE FROM speler_speelt_tornooi WHERE speler = ? AND tornooi = ?;")) {
          preparedInsert.setInt(1, tennisvlaanderenid); 
          preparedInsert.setInt(2, tornooiid); 
          preparedInsert.executeUpdate();
      }
      connection.commit();
    } catch (InvalidSpelerException | InvalidTornooiException | SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
