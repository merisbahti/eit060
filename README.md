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

    $ ant server 

Detta startar en server på port 9876 och hämtar serverkeystore samt servertruststore från stores/ mappen.
  
för att köra klient:

    $ ant client

Detta startar klienten och kopplar sig till localhost:9876. stores från stores/

För att endast kompilera->packa->testa så kör du:

    $ ant test

