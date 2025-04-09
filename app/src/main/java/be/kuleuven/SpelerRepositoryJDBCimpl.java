package be.kuleuven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
          prepared.setInt(1, speler.getTennisvlaanderenid()); // First questionmark
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
          prepared.setInt(1, tennisvlaanderenId); // First questionmark
          ResultSet result = prepared.executeQuery();
          
          while (result.next()) {
              int tennisvlaanderenid = result.getInt("tennisvlaanderenid");
              String naam = result.getString("naam");
              int punten = result.getInt("punten");
              
              foundSpeler = new Speler(tennisvlaanderenid, naam, punten);
          }
          
          if (foundSpeler == null) {
              throw new InvalidSpelerException("Speler met tennisvlaanderenid " + tennisvlaanderenId + " niet gevonden.");
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
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'getAllSpelers'");
  }

  @Override
  public void updateSpelerInDb(Speler speler) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'updateSpelerInDb'");
  }

  @Override
  public void deleteSpelerInDb(int tennisvlaanderenid) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'deleteSpelerInDb'");
  }

  @Override
  public String getHoogsteRankingVanSpeler(int tennisvlaanderenid) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'getHoogsteRankingVanSpeler'");
  }

  @Override
  public void addSpelerToTornooi(int tornooiId) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'addSpelerToTornooi'");
  }

  @Override
  public void removeSpelerFromTornooi(int tornooiId) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'removeSpelerFromTornooi'");
  }
}
