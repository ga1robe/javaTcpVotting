# java-Tcp-Votting

## Opis

Serwer nasłuchujący na porcie TCP 5017 z łatwą możliwością zmiany na wyższy.
Serwer przyjmuje połączenia od dowolnej ilości klientów.
Łączący się klienci mają możliwość przeprowadzenia wielu głosowań.

## Protokół komunikacyjny

Strony komunikują się komendami, które są ciągami znaków ASCII zakończonymi znakiem LF ('\n'),
zaś pola komend oddzielone są znakami SP (spacja).

### Komendy Klienta:

* "NODE" SP nazwa LF -- nazwa - unikalna nazwa węzła, używana później do jego identyfikacji

* "NEW" SP głosowanie SP YN SP treść LF -- węzeł inicjuje nowe głosowanie.
głosowanie - unikalna nazwa głosowania,
YN - głos inicjującego węzła "Y" albo "N",
treść - dowolny ciąg znaków (z wyjątkiem LF) to literalnego przesłania przez serwer pozostałym węzłom.

* "VOTE" SP głosowanie SP YN LF -- Węzeł oddaje głos w głosowaniu.
głosowanie - nazwa głosowania, w której węzeł oddaje głos,
YN - głos węzła "Y" albo "N",
Dopóki głosowanie trwa, węzeł może oddawać dowolnie dużo głosów, nawet zmieniając zdanie. Wzięty pod uwagę ma być tylko ostatni oddany głos.

* "PING" LF -- Serwer odpowie "PONG" LF

* "PONG" LF -- komenda zignorowana.

### Komendy Serwera

* "NOK" SP treść LF -- serwer odrzuca ostatnią komendę Klienta, treść może być dowolnym ciągiem znaków (poza LF) opisującym human-readable przyczynę odrzucenia.

* "NEW" SP węzeł SP głosowanie SP treść LF -- serwer rozsyła tę komendę do wszystkich węzłów w reakcji na prawidłową komendę "NEW" jakiegoś  węzła.
węzeł - nazwa węzła, który zainicjował głosowanie,
głosowanie - jego nazwa,
treść - ciąg znaków, który Klient przesłał przy inicjacji nowego głosowania.

* "VOTE" SP węzeł SP głosowanie SP YN LF -- serwer rozsyła tę komendę do wszystkich węzłów w reakcji na prawidłowo oddany głos przez jakiś węzeł.

* "RESULT" SP głosowanie SP YN LF -- serwer rozsyła tę komendę do wszystkich węzłów po zamknięciu głosowania.
YN - rezultat głosowania.

O zamknięciu głosowania decyduje:

1) spośród wszystkich aktywnych ... węzłów więcej niż połowa opowiadająca się po którejś stronie

2) po określonym czasie od otwarcia, w wyniku zdania większości głosujących węzłów; w przypadku głosów padających równo, odpowedź N

## Opis środowiska

Serwer stworzony w środowisku Java z wykorzystaniem Mavenem.

Ustawienia portu, czasów zostały umieszczone w pliku `resources/application.properties`

Uruchomienie aplikacji

```sh
./mvnw spring-boot:run
```

Po stronie klienta nasłuchiwanie po `localhost`  i numerze portu `5017` (opcjonalny, z możliwością zmiany).
