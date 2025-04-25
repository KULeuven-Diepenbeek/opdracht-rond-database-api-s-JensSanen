package be.kuleuven;

import java.util.List;

public interface SpelerRepository {
  public void addSpelerToDb(Speler speler);

  public Speler getSpelerByTennisvlaanderenId(int tennisvlaanderenId);

  public List<Speler> getAllSpelers();

  public void updateSpelerInDb(Speler speler);

  public void deleteSpelerInDb(int tennisvlaanderenId);

  public String getHoogsteRankingVanSpeler(int tennisvlaanderenId);
  // Vorm moet zijn: 'Hoogst geplaatst in het tornooi van <clubnaam tornooi> met plaats in de <finalestring>'
  // Hierbij komt een finale plaats van 1 overeen met 'finale', 2 met 'halve finale', 4 met 'kwart finale', hoger dan 4 met 'lager dan kwart finale'.
  // LOT OP: indien de speler de finale ook gewonnen heeft is de finalestring = 'winst'

  public void addSpelerToTornooi(int tornooiId, int tennisvlaanderenId);

  public void removeSpelerFromTornooi(int tornooiId, int tennisvlaanderenId);
}
