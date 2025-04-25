package be.kuleuven;

import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class SpelerRepositoryJPAimpl implements SpelerRepository {
  private final EntityManager em;
  public static final String PERSISTANCE_UNIT_NAME = "be.kuleuven.spelerhibernateTest";

  // Constructor
  SpelerRepositoryJPAimpl(EntityManager entityManager) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste constructor op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented constructor");
  }

  @Override
  public void addSpelerToDb(Speler speler) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'getAllSpelers'");
  }

  @Override
  public Speler getSpelerByTennisvlaanderenId(int tennisvlaanderenId) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'getAllSpelers'");
  }

  @Override
  public List<Speler> getAllSpelers() {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'getAllSpelers'");
  }

  @Override
  public void updateSpelerInDb(Speler speler) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'getAllSpelers'");
  }

  @Override
  public void deleteSpelerInDb(int tennisvlaanderenId) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'getAllSpelers'");
  }

  @Override
  public String getHoogsteRankingVanSpeler(int tennisvlaanderenId) {
    // TODO: verwijder de "throw new UnsupportedOperationException" en schrijf de code die de gewenste methode op de juiste manier implementeerd zodat de testen slagen.
    throw new UnsupportedOperationException("Unimplemented method 'getAllSpelers'");
  }

  @Override
  public void addSpelerToTornooi(int tornooiId, int tennisvlaanderenId) {
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();

      Speler speler = em.find(Speler.class, tennisvlaanderenId);
      Tornooi tornooi = em.find(Tornooi.class, tornooiId);

      speler.getTornooien().add(tornooi);
      em.merge(speler);

      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      throw e;
    } finally {
      em.close();
    }
  }

  @Override
  public void removeSpelerFromTornooi(int tornooiId, int tennisvlaanderenId) {
    EntityTransaction tx = em.getTransaction();

    try {
      tx.begin();

      Speler speler = em.find(Speler.class, tennisvlaanderenId);
      Tornooi tornooi = em.find(Tornooi.class, tornooiId);

      if (speler == null || tornooi == null) {
        throw new IllegalArgumentException("Speler or Tornooi not found");
      }

      speler.getTornooien().remove(tornooi);
      em.merge(speler);

      tx.commit();
    } catch (Exception e) {
      if (tx.isActive())
        tx.rollback();
      throw e;
    } finally {
      em.close();
    }
  }
}
