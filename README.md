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

```
Give examples
```

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

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc



