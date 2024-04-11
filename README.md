# Ejecución

---

## Disclaimer

- Se necesita una instancia de MySQL en ejecución a la hora de ejecutar el servidor del proyceto.

- Se recomienda usar IntelliJ IDEA para ejecutar el proyecto gracias a su resolución de dependencias y facilidad de uso
(permite ejecutar los targets necesarios en el apartado *Plugins*).

- Se necesita Java 21 y configurar Maven para usarlo (o usar IntelliJ).

---

1. La base de datos del proyecto y su usuario asociado ejecutando el script SQL *src/main/resources/sql/create_user.sql*.

1. (Opcional) Limpiar el directorio de trabajo de aquellos archivos que hayan sido
generados durante la construcción del proyecto con el target *clean:clean* de Maven
(```mvn clean:clean -f pom.xml```).

2. Compilar el proyecto mediante el target *compiler:compile* de Maven (```mvn compiler:compile -f pom.xml```).

3. Ejecutar el servidor mediante el target *jetty:run* de Maven (```mvn jetty:run -f pom.xml```).

4. Ejecutar el client mediante el target *exec:java* de Maven (```mvn exec:java -P client -f pom.xml```).