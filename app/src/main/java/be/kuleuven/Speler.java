package be.kuleuven;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;



@Entity
@Table(name = "speler")
public class Speler {
  @Id
  private int tennisvlaanderenId;
  @Column(name="naam", nullable = false)
  private String naam;
  
  @Column(name="punten", nullable = false)
  private int punten;

  // For relations
  // One-to-Many unidirectional for matches where this is speler1
  @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JoinColumn(name = "speler1", referencedColumnName = "tennisvlaanderenid")
  private List<Wedstrijd> wedstrijdenAlsSpeler1 = new ArrayList<>();

  // One-to-Many unidirectional for matches where this is speler2
  @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JoinColumn(name = "speler2", referencedColumnName = "tennisvlaanderenid")
  private List<Wedstrijd> wedstrijdenAlsSpeler2 = new ArrayList<>();

  // Transient combined list of all matches
  @Transient
  private List<Wedstrijd> wedstrijden = new ArrayList<>();

  @PostLoad
  private void populateWedstrijden() {
    wedstrijden.clear();
    wedstrijden.addAll(wedstrijdenAlsSpeler1);
    wedstrijden.addAll(wedstrijdenAlsSpeler2);
  }

  // Many-to-Many with Tornooi, renamed field
  @ManyToMany
  @JoinTable(name = "speler_speelt_tornooi", joinColumns = @JoinColumn(name = "speler", referencedColumnName = "tennisvlaanderenid"), inverseJoinColumns = @JoinColumn(name = "tornooi", referencedColumnName = "id"))
  private List<Tornooi> tornooien;

  // Constructors
  public Speler() {
  }

  public Speler(int tennisvlaanderenId, String naam, int punten) {
    this.tennisvlaanderenId = tennisvlaanderenId;
    this.naam = naam;
    this.punten = punten;
    // For relations
    this.wedstrijden = new ArrayList<>();
    this.tornooien = new ArrayList<>();
  }

  public int getTennisvlaanderenId() {
    return tennisvlaanderenId;
  }

  public void setTennisvlaanderenId(int tennisvlaanderenId) {
    this.tennisvlaanderenId = tennisvlaanderenId;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public int getPunten() {
    return punten;
  }

  public void setPunten(int punten) {
    this.punten = punten;
  }

  // @Override
  // public int hashCode() {
  //   final int prime = 31;
  //   int result = 1;
  //   result = prime * result + tennisvlaanderenId;
  //   result = prime * result + ((naam == null) ? 0 : naam.hashCode());
  //   result = prime * result + punten;
  //   return result;
  // }
  @Override
  public int hashCode() {
    return Objects.hash(tennisvlaanderenId, naam, punten);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Speler other = (Speler) obj;
    if (tennisvlaanderenId != other.tennisvlaanderenId)
      return false;
    if (naam == null) {
      if (other.naam != null)
        return false;
    } else if (!naam.equals(other.naam))
      return false;
    if (punten != other.punten)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Speler [tennisvlaanderenId=" + tennisvlaanderenId + ", naam=" + naam + ", punten=" + punten + "]";
  }

  // For relations
  public List<Wedstrijd> getWedstrijden() {
    return wedstrijden;
  }

  public void addWedstrijd(Wedstrijd wedstrijd) {
    if (wedstrijd != null && !wedstrijden.contains(wedstrijd)) {
      wedstrijden.add(wedstrijd);
    }
  }

  public void deleteWedstrijd(int wedstrijdId) {
    wedstrijden.removeIf(wedstrijd -> wedstrijd.getId() == wedstrijdId);
  }

  public List<Tornooi> getTornooien() {
    return tornooien;
  }

  public void addTornooi(Tornooi tornooi) {
    if (tornooi != null && !tornooien.contains(tornooi)) {
      tornooien.add(tornooi);
    }
  }

  public void deleteTornooi(int tornooiId) {
    tornooien.removeIf(tornooi -> tornooi.getId() == tornooiId);
  }
}
