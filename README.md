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

Innan du kör programmen så måste du befinna dig i bin/.

Navigera till bin:

    $ cd bin
  
För att köra server:

    $ java -jar server.jar port
  
för att köra klient:

    $ java -jar client.jar ip port

där ip är localhost (om du kör lokalt).

OBSERVERA! 2014-02-21:

Keystores måste existera för server/client i samma directory 
för server/klient när man kör.
