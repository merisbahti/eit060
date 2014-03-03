Bygginstruktioner
=================

För att bygga: 

    $ ant

detta kommer skapa .jar-arkiv med dem relevanta filerna för Server respektive Klient i ./bin katalogen.
    
För att rensa: 

    $ ant clean
    
Detta tar bort allt som genereras under byggkommandot.

Körinstruktioner
================

Enklaste sättet att både kompilera, köra tester samt köra är att göra som följer:

För att köra server:

    $ ant server (DEPRECATED, SE ANVÄNDNING)

Detta startar en server på port 9876 och hämtar serverkeystore samt servertruststore från stores/ mappen.
  
för att köra klient:

    $ ant client

Detta startar klienten och kopplar sig till localhost:9876. stores från stores/

För att endast kompilera->packa->testa så kör du:

    $ ant test

ANVÄNDNING
=================
Programmet ska alltid köras från root mappen.

skriv

    $ java -jar bin/server.jar 9876

för att starta server.

skriv 

    $ java -jar bin/client.jar IP PORT

för att connecta till en instans av servern.

OBSERVERA!!!!!!!!!
Alla certifikat kommer att hämtas, när ombedes skriva in keystore så behöver du bara 
skriva in första delen av keystoren, utan "keystore". 
Vill du exempelvis logga in med patient1keystore så ska du altså bara skriva in 
patient1 i prompten.

SAMTLIGA KEYSTORES har lösenordet password.
