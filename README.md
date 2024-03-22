# Taller 7 - Aplicación distribuida segura en todos sus frentes

## Diseño de Clases

Los principales componentes de la aplicación son:

+ `LoginApp`: Utiliza el framework Spark para crear un servidor web. La aplicación ofrece dos servicios principales: autenticación de usuarios mediante el endpoint _/login_, donde se validan las credenciales proporcionadas en una solicitud POST, y acceso seguro a recursos externos a través de las funciones trigonométricas seno y coseno en los endpoints _/sin_ y _/cos_, respectivamente. Se implementa un mecanismo de seguridad SSL para la comunicación segura.
+ `CalculatorApp`: Este código Java representa una aplicación web que ofrece servicios para calcular el seno y coseno de un valor dado. Configura un servidor web utilizando Spark, especificando el puerto en el que escuchará las solicitudes. Habilita la seguridad SSL para la comunicación segura, utilizando un almacén de claves y una contraseña. Define dos endpoints GET, uno para calcular el seno y otro para el coseno, donde se lee el valor proporcionado en la solicitud y se realiza el cálculo correspondiente utilizando las funciones trigonométricas de la clase Math.
+ `SecureURLReader`: Esta clase proporciona métodos para leer contenido desde una URL de manera segura utilizando SSL. Primero, establece la configuración de seguridad cargando un almacén de confianza (trust store) que contiene certificados de confianza para validar las conexiones SSL. Luego, inicializa un contexto SSL con los trust managers obtenidos del trust store cargado. Posteriormente, el método readURL se encarga de leer el contenido desde la URL proporcionada, realizando la conexión segura utilizando la configuración SSL previamente establecida. Además, este método imprime los encabezados de la respuesta HTTP y el cuerpo del mensaje en la consola para propósitos de depuración. La clase también proporciona métodos auxiliares para obtener la ubicación del trust store y la contraseña de la clave, permitiendo configuraciones personalizadas a través de variables de entorno si están disponibles, de lo contrario, utiliza valores predeterminados.
+ `UserService`: Implementa la lógica relacionada con la gestión de usuarios y la autenticación en un sistema. La clase utiliza una estructura de datos HashMap para almacenar nombres de usuario como claves y contraseñas hasheadas como valores. Se definen los siguientes usuarios: 
    - admin: password
    - Pepe: 12345
    - Samuel: 54321

## Arquitectura

El objetivo es contruir una aplicación web que permita un acceso seguro desde el browser a la aplicación, garantizando autenticación, autorización e integridad de usuarios. Se implementa la siguiente arquitectura:

![design](https://github.com/An6ie02/AREP-TALLER-07/assets/100453879/9fc8fd38-f71a-4589-a71a-a698f46e220f)

## Despliegue local

Para desplegar la aplicación en un entorno local, se deben seguir los siguientes pasos:

```bash
# Clona el repositorio de la aplicación
git clone https://github.com/An6ie02/AREP-TALLER-07.git
# Cambia al directorio del repositorio
cd AREP-TALLER-07
# Construye la aplicación con Maven
mvn clean
mvn compile
mvn package
# Ejecuta la aplicación
java -cp target/all-secure-web-1.0-SNAPSHOT-jar-with-dependencies.jar  edu.escuelaing.arep.secureweb.LoginApp

java -cp target/all-secure-web-1.0-SNAPSHOT-jar-with-dependencies.jar  edu.escuelaing.arep.secureweb.CalculatorApp
```

Una vez iniciada la aplicación, se puede acceder en la dirección [http://localhost:4567](http://localhost:4567).

## Despliegue en AWS

Se ha creado una instancia EC2 en AWS para desplegar la aplicación. La instancia se ha configurado con una dirección IP pública y se ha habilitado el tráfico de entrada en los puertos 4567 y 4568. Además, se ha instalado java, git, maven en la instancia para poder ejecutar la aplicación. Se ha configurado un grupo de seguridad para permitir el tráfico en los puertos mencionados y se ha creado una clave privada para acceder a la instancia mediante SSH.

El resumen de comandos para desplegar la aplicación en la instancia EC2 es el siguiente:

```bash
# Actualizar el sistema
sudo yum update -y
# Instalar git
sudo yum install git -y
# Instalar java
sudo yum install java-17-amazon-corretto-devel
# Comprobar la versión de java
java --version
# Instalar maven
sudo wget https://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo

sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo

sudo yum install -y apache-maven

# Clonar el repositorio de la aplicación
git clone https://github.com/An6ie02/AREP-TALLER-07.git
# Cambiar al directorio del repositorio
cd AREP-TALLER-07
```
Se crean los certificados para la comunicación segura con la aplicación:

```bash
# Crear un almacén de claves
keytool -genkeypair -alias ecikeypair -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore ecikeystore.p12 -validity 3650
# Crear un certificado autofirmado
keytool -export -keystore ./ecikeystore.p12 -alias ecikeypair -file ecicert.cer
# Importar el certificado en el almacén de confianza
keytool -import -file ./ecicert.cer -alias firstCA -keystore myTrustStore.p12
```

![CreateKey](https://github.com/An6ie02/AREP-TALLER-07/assets/100453879/f7d53a4b-8308-4a5b-8c0d-42de85fb93c3)\
Para ejecutar la aplicación en la instancia EC2, se deben seguir los siguientes pasos:

```bash
# Construir la aplicación con Maven
mvn clean
mvn compile
mvn package
# Ejecutar la aplicación
java -cp target/all-secure-web-1.0-SNAPSHOT-jar-with-dependencies.jar  edu.escuelaing.arep.secureweb.LoginApp

java -cp target/all-secure-web-1.0-SNAPSHOT-jar-with-dependencies.jar  edu.escuelaing.arep.secureweb.CalculatorApp
```

Una vez hecho esto, se puede acceder a la aplicación web en la dirección IP pública de la instancia en el puerto 4567. A continuación, se muestra el resultado de la aplicación web en funcionamiento:

https://github.com/An6ie02/AREP-TALLER-07/assets/100453879/29f50f3f-0035-4ccd-9489-92279f0ad02e


## Autor

* **Angie Natalia Mojica** [Angie Natalia Mojica](https://www.linkedin.com/in/angienataliamojica/)

## Agradecimientos y Fuentes

* Al profesor [Luis Daniel Benavides Navarro](https://www.linkedin.com/in/danielbenavides/) por la guía y la enseñanza en el curso de Arquitecturas Empresariales.
* [Instalar java en EC2](https://github.com/kunchalavikram1427/YouTube_Series/blob/main/Tools/install%20java%20on%20aws%20ec2.md)
* [Instalar maven en EC2](https://docs.aws.amazon.com/es_es/neptune/latest/userguide/iam-auth-connect-prerq.html)

