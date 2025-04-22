package be.kuleuven;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.Test;


public abstract class SpelerRepositoryTest {
  protected final String CONNECTIONSTRING_TO_TEST_DB = "jdbc:sqlite:./testdb.sqlite";
  protected final String USER_OF_TEST_DB = "";
  protected final String PWD_OF_TEST_DB = "";

  protected SpelerRepository spelerRepository;

  @Test
  public void givenNewSpeler_whenAddSpelerToDb_assertThatSpelerIsInDb() {
    // Arrange
    Speler newSpeler = new Speler(123, "TestNaam", 11);

    // Act
    spelerRepository.addSpelerToDb(newSpeler);

    // Assert
    Speler spelerToCheck = spelerRepository.getSpelerByTennisvlaanderenId(123);
    assertThat(spelerToCheck).isEqualTo(newSpeler);
  }

  @Test
  public void givenNewSpelerThatAlreadyInDb_whenAddSpelerToDb_assertThrowsRuntimeException() {
    // Arrange
    int tennisvlaanderenId = 1;
    Speler newSpeler = new Speler(tennisvlaanderenId, "newNaam", 11);
    // Act
    Throwable thrown = catchThrowable(() -> {
      spelerRepository.addSpelerToDb(newSpeler);
    });
    // Assert
    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessageContaining(" A PRIMARY KEY constraint failed");
  }

  @Test
  public void given1_whenGetSpelerByTennisvlaanderenId_assertThatSpelerIsNadal() {
    // Arrange
    int tennisvlaanderenId = 1;
    Speler spelerSolution = new Speler(tennisvlaanderenId, "Rafael Nadal", 3);

    // Act
    Speler spelerToCheck = spelerRepository.getSpelerByTennisvlaanderenId(tennisvlaanderenId);

    // Assert
    assertThat(spelerToCheck).isEqualTo(spelerSolution);
  }

  @Test
  public void givenWrongTennisvlaanderenId_whenGetSpelerByTennisvlaanderenId_assertThatThrowsInvalidSpelerException() {
    // Arrange
    int tennisvlaanderenId = 9999;

    // Act
    Throwable thrown = catchThrowable(() -> {
      spelerRepository.getSpelerByTennisvlaanderenId(tennisvlaanderenId);
    });

    // Assert
    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Invalid Speler met identification: " + tennisvlaanderenId);
  }

  @Test
  public void whenGetAllSpelers_assertThat8correctSpelersPresent() {
    // Arrange
    ArrayList<Speler> spelersSolution = new ArrayList<>();
    spelersSolution.add(new Speler(1, "Rafael Nadal", 3));
    spelersSolution.add(new Speler(2, "Roger Federer", 1));
    spelersSolution.add(new Speler(3, "Stefano Tsisipas", 4));
    spelersSolution.add(new Speler(4, "Carlos Alcaraz", 5));
    spelersSolution.add(new Speler(5, "Kim Clijsters", 8));
    spelersSolution.add(new Speler(6, "Justine Henin", 9));
    spelersSolution.add(new Speler(7, "Serena Wiliams", 2));
    spelersSolution.add(new Speler(8, "Maria Sharapova", 3));

    // Act
    List<Speler> spelersToCheck = spelerRepository.getAllSpelers();
    // Assert
    assertThat(spelersToCheck).usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(spelersSolution);
  }

  @Test
  public void givenSpeler1updateToRafa_whenUpdateSpelerInDb_assertThatSpelerIsInDb() {
    // Arrange
    int tennisvlaanderenId = 1;
    Speler updateSpeler = new Speler(tennisvlaanderenId, "Rafa", 5);

    // Act
    spelerRepository.updateSpelerInDb(updateSpeler);

    // Assert
    Speler spelerToCheck = spelerRepository.getSpelerByTennisvlaanderenId(tennisvlaanderenId);
    assertThat(spelerToCheck).isEqualTo(updateSpeler);
  }

  @Test
  public void givenSpelerNotInDb_whenUpdateSpelerInDb_assertThatThrowsInvalidSpelerException() {
    // Arrange
    int tennisvlaanderenId = 9999;
    Speler updateSpeler = new Speler(tennisvlaanderenId, "newNaam", 11);

    // Act
    Throwable thrown = catchThrowable(() -> {
      spelerRepository.updateSpelerInDb(updateSpeler);
    });

    // Assert
    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Invalid Speler met identification: " + tennisvlaanderenId);
  }

  @Test
  public void givenSpeler1delete_whenDeleteSpelerInDb_assertThatSpelerIsNoLongerInDb() {
    // Arrange
    int tennisvlaanderenId = 1;

    // Act
    spelerRepository.deleteSpelerInDb(tennisvlaanderenId);

    // Assert
    Throwable thrown = catchThrowable(() -> {
      spelerRepository.getSpelerByTennisvlaanderenId(tennisvlaanderenId);
    });

    // Assert
    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Invalid Speler met identification: " + tennisvlaanderenId);
  }

  @Test
  public void givenSpelerNotInDb_whenDeleteSpelerInDb_assertThatThrowsInvalidSpelerException() {
    // Arrange
    int tennisvlaanderenId = 9999;

    // Act
    Throwable thrown = catchThrowable(() -> {
      spelerRepository.deleteSpelerInDb(tennisvlaanderenId);
    });

    // Assert
    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Invalid Speler met identification: " + tennisvlaanderenId);
  }

  @Test
  public void given2_whenGetHoogsteRankingVanSpeler_assertThatTPTessenderloEnFinale() {
    // Arrange
    String solutionString = "Hoogst geplaatst in het tornooi van T.P.Tessenderlo met plaats in de finale";
    // Act
    String checkString = spelerRepository.getHoogsteRankingVanSpeler(2);
    // Assert
    assertThat(checkString).isEqualTo(solutionString);
  }

  @Test
  public void given4_whenGetHoogsteRankingVanSpeler_assertThatTPTessenderloEnWinst() {
    // Arrange
    String solutionString = "Hoogst geplaatst in het tornooi van T.P.Tessenderlo met plaats in de winst";
    // Act
    String checkString = spelerRepository.getHoogsteRankingVanSpeler(4);
    // Assert
    assertThat(checkString).isEqualTo(solutionString);
  }

  @Test
  public void givenSpeler1enTornooi3_whenAddSpelerToTornooi_assertThatRowInSpeler_speelt_tornooi() {
    // Arrange
    int tennisvlaanderenId = 1;
    int tornooiId = 3;
    // Act
    spelerRepository.addSpelerToTornooi(tornooiId, tennisvlaanderenId);
    // Assert
    try {
      ConnectionManager cm = new ConnectionManager(CONNECTIONSTRING_TO_TEST_DB, USER_OF_TEST_DB, PWD_OF_TEST_DB);
      Statement statement = (Statement) cm.getConnection().createStatement();
      var result = statement
          .executeQuery("SELECT COUNT(*) as cnt FROM speler_speelt_tornooi WHERE speler = 1 and tornooi = 3;");
      while (result.next()) {
        assertThat(result.getInt("cnt")).isEqualTo(1);
      }
      statement.close();
      cm.getConnection().commit();
      cm.getConnection().close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

  }

  @Test
  public void givenSpeler5enTornooi2_whenRemoveSpelerToTornooi_assertThatNoRowInSpeler_speelt_tornooi() {
    // Arrange
    int tennisvlaanderenId = 5;
    int tornooiId = 2;
    // Act
    spelerRepository.removeSpelerFromTornooi(tornooiId, tennisvlaanderenId);
    // Assert
    try {
      ConnectionManager cm = new ConnectionManager(CONNECTIONSTRING_TO_TEST_DB, USER_OF_TEST_DB, PWD_OF_TEST_DB);
      Statement statement = (Statement) cm.getConnection().createStatement();
      var result = statement
          .executeQuery("SELECT COUNT(*) as cnt FROM speler_speelt_tornooi WHERE speler = 5 and tornooi = 2;");
      while (result.next()) {
        assertThat(result.getInt("cnt")).isEqualTo(0);
      }
      statement.close();
      cm.getConnection().commit();
      cm.getConnection().close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

}
