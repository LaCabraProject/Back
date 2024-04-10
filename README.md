

Para ejecutar el proyecto primero hay que tener una instancia de mysql ejecutandose en el equipo
Despues hay que compilarlo mediante la extension de maven de IntelIJ

  LifeCycle -> Compile

o mediante el siguiente comando

  mvn compile

Para ejecutar el Server utilizar el plugin de jetty

  plugins -> jetty -> jetty:run

o ejecutando el siguiente comando

  mvn jetty:run

Para ejecutar el cliente utilizar el plugin de exec

  plugins -> exec -> exec:java

o ejecutando el siguiente comando

  mvn exec:java -pClient
