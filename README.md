# DATAPRICE 

Dataprice uses open source software in order to fetch online stores and extract all the necessary information for benchmarking prices. It also provides a framework for extracting e-commerce products data. Dataprice is built in java.

## Use cases

 - Track and compare prices for products in different e-commerce stores.
 - Create your own e-commerce feeds in order to update price comparison shopping engines
 - Scrape online stores (id, name, price, description, url, image, sku, brand and category)
          
## Main Features

 - Create, delete and run scraping tasks with the GUI. 
 - Import and Export tasks with JSON
 - Multithread, you can run several tasks at the same time.
 - Possibility to add a proxy to yout task, in order to avoid ip blocking.
 - Easy installation with docker, in order words, great portability
 - Matching section for normalizing data.
 - Price matrices reports
 - One month price history with plot included. 
 - Authentication and the possibility to change password 
 - Ability to export to csv or excel the data


## Getting Started

These instructions will get you a copy of the project up and running on your server machine.

### Prerequisites

In order to install dataprice software, you will need:

*   Docker (Last Version)
*   Java 8
*   Maven (Last Version)

### Installing

Please follow the instructions:

```
# Step 1: Install mysql using docker
docker run --name demo-mysql -e MYSQL_ROOT_PASSWORD=12345 -e MYSQL_DATABASE=univers -d mysql:5.7

# Step 2: Install phantomjs driver using docker
docker run --restart unless-stopped -d -p 8910:8910 wernight/phantomjs phantomjs --webdriver=8910

# Step 3
Clone dataprice repository 

# Step 4 : Inside the dataprice folder run the following command.
mvn clean package docker:build

# Step 5 : Once the image is created, run it.
docker run -d     --name dataprice-app     --link demo-mysql:mysql     -p 8080:8080     -e DATABASE_HOST=demo-mysql     -e DATABASE_PORT=3306     -e DATABASE_NAME=univers     -e DATABASE_USER=root     -e DATABASE_PASSWORD=12345     -e DATABASE_DRIVER=com.mysql.jdbc.Driver      glitchero/dataprice

```

In order to see the logs, please use the command:

```
docker logs dataprice-app
```

Finally, go to the browser and see the application in http://xxx.xx.xxx.xxx:8080/login (for local http://localhost:8080/login).

## Installation videos

 - [Installation in Ubuntu Server Digital Ocean](https://www.youtube.com/watch?v=N878vHbl2O8)
 - [Installation in Local Machine Windows](https://www.youtube.com/watch?v=N878vHbl2O8)
 - [How to Test a Scraper Locally with eclipse](https://www.youtube.com/watch?v=N878vHbl2O8)

## How to create a scraper

See all the tutorials about how to scrape a site using dataprice:

[Amazon](https://www.youtube.com/watch?v=N878vHbl2O8) , 
[Mercado Libre](https://www.youtube.com/watch?v=N878vHbl2O8) , 
[Linio](https://www.youtube.com/watch?v=N878vHbl2O8)

