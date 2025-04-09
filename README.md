[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/3hq1u2IC)
### Opdracht: tennisspelers, tornooien en wedstrijden

Voor de verplichte opdrachten rond Database API's in Java (met Gradle) gaan jullie een aantal TODO's in dit project moeten oplossen. De probleemstelling is een zeer beperkte versie van de [casus](/apis/_index.md). De beperkte versie werkt als volgt:
- Je hebt enkel Spelers, Tornooien en Wedstrijden
- Een Speler heeft een unieke tennisvlaanderenId, een naam en een aantal punten.
- Een Tornooi heeft enkel een id en een naam van de tennisclub die dat tornooi organiseert.
- Een Wedstrijd bevat 2 spelers, 1 winnaar, de tornooiId, een score string en een finale nummer
  - De finale nummer geeft aan in de hoeveelste ronde de wedstrijd gespeeld werd: 1 = finale, 2 = halve finale, 4 = kwart finale ...
- Je kan je als Speler ook inschrijven voor een Tornooi

Het EER-schema van het project zie je hieronder:

![EER opdracht api](/img/eer_opdracht_api.png)

In het project zijn alle klassen die je nodig hebt al aangemaakt op de correcte manier. Het enige wat je nog moet doen is de TODO's oplossen bij de `SpelerRespositoryJDBCimpl`-klasse en de `SpelerRespositoryJDBIimpl`-klasse. Waarbij je natuurlijk de juiste technologieën toepast. Er is geen App.java code, je kan jezelf wel testen door de Testen die toegevoegd zijn te runnen. Je mag extra testen schrijven als die nodig is. (De testen werken met een in memory database met SQLite, maar dit zou geen probleem mogen geven bij jullie implementatie)

**BELANGRIJK** wanneer je SQL specifieke imports moet doen, gebruik dan de generieke `java.sql` imports en geen `com.mysql` imports aangezien de testen dan niet zullen werken !!!

**Push voor de deadline van vrijdag 2 mei 2025 23u59 je oplossingen naar je repository**

Veel succes! Je mag me altijd contacteren via [arne.duyver@kuleuven.be](mailto::arne.duyver@kuleuven.be) voor vragen of in de les aanspreken.

Er is ook een video met deze uitleg voorzien die je op [Toledo](https://toledo.kuleuven.be) bij de opdracht kan terugvinden.