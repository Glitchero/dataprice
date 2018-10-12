# Instalation

Paso 1: docker run --name demo-mysql -e MYSQL_ROOT_PASSWORD=12345 -e MYSQL_DATABASE=univers -d mysql:5.7
Paso 2: sudo apt-get update
Paso 3: sudo apt-get install maven
Paso 4: sudo apt-get install openjdk-8-jdk
Paso 5: docker run --restart unless-stopped -d -p 8910:8910 wernight/phantomjs phantomjs --webdriver=8910
Paso 6: clonar repo
Paso 7: cd dataprice
Paso 8: mvn clean package docker:build
Paso 9: cd ..
Paso 10: docker run -d     --name dataprice-app     --link demo-mysql:mysql     -p 8080:8080     -e DATABASE_HOST=demo-mysql     -e DATABASE_PORT=3306     -e DATABASE_NAME=univers     -e DATABASE_USER=root     -e DATABASE_PASSWORD=12345     -e DATABASE_DRIVER=com.mysql.jdbc.Driver      glitchero/dataprice
Paso 10: docker ps ---> Éxito si hay tres contenedores corriendo:
Paso 11: Ir a http://xxx.xx.xxx.xxx:8080/login
Paso 12: Entrar al sistema como administrador



