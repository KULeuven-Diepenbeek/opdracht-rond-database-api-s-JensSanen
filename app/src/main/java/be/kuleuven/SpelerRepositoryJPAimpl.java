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
    this.em = entityManager;
  }

  @Override
  public void addSpelerToDb(Speler speler) {
    try {
      em.getTransaction().begin();
      em.persist(speler);
      em.getTransaction().commit();   
    } catch (Exception e) {
      throw new RuntimeException("A PRIMARY KEY constraint failed", e);
    }
  }

  @Override
  public Speler getSpelerByTennisvlaanderenId(int tennisvlaanderenId) {
    Speler speler = em.find(Speler.class, tennisvlaanderenId);
    if (speler == null) {
      throw new InvalidSpelerException(tennisvlaanderenId + "");
    }
    return speler;
  }

  @Override
  public List<Speler> getAllSpelers() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Speler> cq = cb.createQuery(Speler.class);
    Root<Speler> root = cq.from(Speler.class);
    cq.select(root);
    return em.createQuery(cq).getResultList();
  }

  @Override
  public void updateSpelerInDb(Speler speler) {
    getSpelerByTennisvlaanderenId(speler.getTennisvlaanderenId());

    em.getTransaction().begin();
    em.merge(speler);
    em.getTransaction().commit();
  }

  @Override
  public void deleteSpelerInDb(int tennisvlaanderenId) {
    Speler speler = getSpelerByTennisvlaanderenId(tennisvlaanderenId);

    em.getTransaction().begin();
    em.remove(speler);
    em.getTransaction().commit();
  }

  @Override
  public String getHoogsteRankingVanSpeler(int tennisvlaanderenid) {
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();

      Speler speler = getSpelerByTennisvlaanderenId(tennisvlaanderenid);

      // Verzamel alle wedstrijden waar de speler in betrokken is
      List<Wedstrijd> alleWedstrijden = speler.getWedstrijden();

      // Sorteer op finale ascending
      alleWedstrijden.sort(Comparator.comparingInt(Wedstrijd::getFinale));

      if (alleWedstrijden.isEmpty()) {
        return "Geen tornooigegevens gevonden voor speler met tennisvlaanderenid " + tennisvlaanderenid + ".";
      }

      Wedstrijd besteWedstrijd = alleWedstrijden.get(0);
      int finale = besteWedstrijd.getFinale();
      int winnaarId = besteWedstrijd.getWinnaarId();
      int tornooiId = besteWedstrijd.getTornooiId();
      Tornooi tornooi = em.find(Tornooi.class, tornooiId);
      String clubnaam = tornooi.getClubnaam();

      tx.commit();

      if (winnaarId == tennisvlaanderenid) {
        return "Hoogst geplaatst in het tornooi van " + clubnaam + " met plaats in de winst";
      } else {
        return switch (finale) {
          case 1 -> "Hoogst geplaatst in het tornooi van " + clubnaam + " met plaats in de finale";
          case 2 -> "Hoogst geplaatst in het tornooi van " + clubnaam + " met plaats in de halve finale";
          case 4 -> "Hoogst geplaatst in het tornooi van " + clubnaam + " met plaats in de kwart finale";
          default -> throw new RuntimeException("Invalid finale number: " + finale);
        };
      }

    } catch (InvalidSpelerException e) {
      if (tx.isActive()) tx.rollback();
        throw new RuntimeException(e);
    } catch (Exception e) {
      if (tx.isActive()) tx.rollback();
        throw e;
    } finally {
      em.close();
    }
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
