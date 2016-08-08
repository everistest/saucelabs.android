Test funcionales de Android con Appium sobre Sauce Labs
=======================================================

Requisitos
----------

- Una máquina local con JDK 1.7 o superior y Maven 3.3 o superior
- Una cuenta en [Sauce Labs](https://saucelabs.com/)

Configuración de Sauce Labs
----------------------------

La máquina local se conecta con los servidores remotos de Appium de Sauce Labs para ejecutar los tests funcionales de Android. Los tests se ejecutan usando Maven, vía el plugin de JUnit y las bibliotecas Java de acceso remoto a Appium.

La dirección de las credenciales de Sauce Labs y los servidores remotos de Appium están en `src/main/resources/uitest.properties`:

    saucelabs.user = <USERNAME>
    saucelabs.key = <ACCESS_KEY>

    appium.host = ondemand.saucelabs.com
    appium.port = 80

Antes de ejecutar los tests para dispositivos Android, es necesario subir el archivo APK compilado al área de almacenamiento temporal de Sauce Labs usando curl:

    curl -u <USERNAME>:<ACCESS_KEY> \
        -X POST -H "Content-Type: application/octet-stream" \
        https://saucelabs.com/rest/v1/storage/<USERNAME>/test.apk?overwrite=true \
        --data-binary @/path/absoluto/al/archivo/apk/32ae5dfef03d903598da65266d131041.apk

Para más información, veáse [Sauce Labs Basics](https://wiki.saucelabs.com/display/DOCS/Sauce+Labs+Basics)

Compilación y ejecución
-----------------------

Para compilar y ejecutar todos los tests desde la máquina local:

    mvn test

Para ejecutar un test particular:

    mvn -Dtest=cl.entel.test.TestLogin test
