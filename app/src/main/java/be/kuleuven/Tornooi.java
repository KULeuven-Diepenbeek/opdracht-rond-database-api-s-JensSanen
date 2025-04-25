package be.kuleuven;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "tornooi")
public class Tornooi {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "clubnaam", nullable = false)
  private String clubnaam;
  // For relations
  // Many-to-Many with Speler (mappedBy renamed to match Speler.tornooien)
  @ManyToMany(mappedBy = "tornooien")
  private List<Speler> ingeschrevenSpelers = new ArrayList<>();

  // One-to-Many unidirectional for matches in this tournament
  @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JoinColumn(name = "tornooi", referencedColumnName = "id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Wedstrijd> wedstrijden = new ArrayList<>();

  public Tornooi() {

  }

  public Tornooi(int id, String clubnaam) {
    this.id = id;
    this.clubnaam = clubnaam;
    // For relations
    this.ingeschrevenSpelers = new ArrayList<>();
    this.wedstrijden = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getClubnaam() {
    return clubnaam;
  }

  public void setClubnaam(String clubnaam) {
    this.clubnaam = clubnaam;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((clubnaam == null) ? 0 : clubnaam.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Tornooi other = (Tornooi) obj;
    if (id != other.id)
      return false;
    if (clubnaam == null) {
      if (other.clubnaam != null)
        return false;
    } else if (!clubnaam.equals(other.clubnaam))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Tornooi [id=" + id + ", clubnaam=" + clubnaam + "]";
  }

  // For relations
  public List<Speler> getIngeschrevenSpelers() {
    return ingeschrevenSpelers;
  }

  public List<Wedstrijd> getWedstrijden() {
    return wedstrijden;
  }

  public void addSpeler(Speler speler) {
    this.ingeschrevenSpelers.add(speler);
  }

  public void addWedstrijd(Wedstrijd wedstrijd) {
    this.wedstrijden.add(wedstrijd);
  }

  public void deleteSpeler(int spelerId) {
    ingeschrevenSpelers.removeIf(speler -> speler.getTennisvlaanderenId() == spelerId);
  }

  public void deleteWedstrijd(int wedstrijdId) {
    wedstrijden.removeIf(wedstrijd -> wedstrijd.getId() == wedstrijdId);
  }
}
