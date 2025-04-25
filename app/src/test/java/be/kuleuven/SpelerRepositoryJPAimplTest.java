package be.kuleuven;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SpelerRepositoryJPAimplTest extends SpelerRepositoryTest {
  private ConnectionManager connectionManager;
  private SessionFactory sessionFactory;
  private EntityManager entityManager;

  @Before
  public void createDatabaseAndInitializeConnectionManager() {
    this.connectionManager = new ConnectionManager(super.CONNECTIONSTRING_TO_TEST_DB, super.USER_OF_TEST_DB,
        super.PWD_OF_TEST_DB);
    connectionManager.initTables();
    connectionManager.verifyTableContentOfInit();
    try {
      connectionManager.getConnection().commit();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    this.sessionFactory = (SessionFactory) Persistence
        .createEntityManagerFactory(SpelerRepositoryJPAimpl.PERSISTANCE_UNIT_NAME);
    this.entityManager = sessionFactory.createEntityManager();
    super.spelerRepository = new SpelerRepositoryJPAimpl(entityManager);
    assertNotNull("SpelerRepository must be initialized by the subclass", super.spelerRepository);
  }

  @After
  public void closeConnections() {
    try {
      if (this.entityManager != null && this.entityManager.isOpen()) {
        this.entityManager.clear(); // detach all managed entities
        this.entityManager.close(); // close the persistence context
      }

      if (this.sessionFactory != null && this.sessionFactory.isOpen()) {
        this.sessionFactory.close(); // free metadata caches, connection pools
      }
      connectionManager.getConnection().close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void givenNewSpeler_whenAddSpelerToDb_assertThatSpelerIsInDb() {
    super.givenNewSpeler_whenAddSpelerToDb_assertThatSpelerIsInDb();
  }

  @Test
  public void givenNewSpelerThatAlreadyInDb_whenAddSpelerToDb_assertThrowsRuntimeException() {
    super.givenNewSpelerThatAlreadyInDb_whenAddSpelerToDb_assertThrowsRuntimeException();
  }

  @Test
  public void given1_whenGetSpelerByTennisvlaanderenId_assertThatSpelerIsNadal() {
    super.given1_whenGetSpelerByTennisvlaanderenId_assertThatSpelerIsNadal();
  }

  @Test
  public void givenWrongTennisvlaanderenId_whenGetSpelerByTennisvlaanderenId_assertThatThrowsInvalidSpelerException() {
    super.givenWrongTennisvlaanderenId_whenGetSpelerByTennisvlaanderenId_assertThatThrowsInvalidSpelerException();
  }

  @Test
  public void whenGetAllSpelers_assertThat8correctSpelersPresent() {
    super.whenGetAllSpelers_assertThat8correctSpelersPresent();
  }

  @Test
  public void givenSpeler1updateToRafa_whenUpdateSpelerInDb_assertThatSpelerIsInDb() {
    super.givenSpeler1updateToRafa_whenUpdateSpelerInDb_assertThatSpelerIsInDb();
  }

  @Test
  public void givenSpelerNotInDb_whenUpdateSpelerInDb_assertThatThrowsInvalidSpelerException() {
    super.givenSpelerNotInDb_whenUpdateSpelerInDb_assertThatThrowsInvalidSpelerException();
  }

  @Test
  public void givenSpeler1delete_whenDeleteSpelerInDb_assertThatSpelerIsNoLongerInDb() {
    super.givenSpeler1delete_whenDeleteSpelerInDb_assertThatSpelerIsNoLongerInDb();
  }

  @Test
  public void givenSpelerNotInDb_whenDeleteSpelerInDb_assertThatThrowsInvalidSpelerException() {
    super.givenSpelerNotInDb_whenDeleteSpelerInDb_assertThatThrowsInvalidSpelerException();
  }

  @Test
  public void given2_whenGetHoogsteRankingVanSpeler_assertThatTPTessenderloEnFinale() {
    super.given2_whenGetHoogsteRankingVanSpeler_assertThatTPTessenderloEnFinale();
  }

  @Test
  public void given4_whenGetHoogsteRankingVanSpeler_assertThatTPTessenderloEnWinst() {
    super.given4_whenGetHoogsteRankingVanSpeler_assertThatTPTessenderloEnWinst();
  }

  @Test
  public void givenSpeler1enTornooi3_whenAddSpelerToTornooi_assertThatRowInSpeler_speelt_tornooi() {
    super.givenSpeler1enTornooi3_whenAddSpelerToTornooi_assertThatRowInSpeler_speelt_tornooi();
  }

  @Test
  public void givenSpeler5enTornooi2_whenRemoveSpelerToTornooi_assertThatNoRowInSpeler_speelt_tornooi() {
    super.givenSpeler5enTornooi2_whenRemoveSpelerToTornooi_assertThatNoRowInSpeler_speelt_tornooi();
  }

}
