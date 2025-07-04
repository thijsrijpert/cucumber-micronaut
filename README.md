# cucumber-micronaut

## Table of Contents

* [About the Project](#about-the-project)
* [Getting Started](#getting-started)
  * [Features](#features)
  * [Build](#build)
  * [Installation](#installation)
* [Usage](#usage)
* [Contributing](#contributing)
* [License](#license)

## About The Project

A lightweight library for using dependency injection in cucumber steps for Micronaut based project.

## Getting Started

### Features

- Cucumber steps dependency injection using the Micronaut dependency injection mechanism. **Only constructor dependency injection is supported**

- Bean discovery by type
- Bean discovery by name. See micronaut @Named annotation usage.
- Bean discovery by parameterized type

### Build

Clone the repo

```sh
https://github.com/thijsrijpert/cucumber-micronaut.git
```

Build the library

```sh
mvn clean package
```

### Installation

Clone the repo

```sh
https://github.com/thijsrijpert/cucumber-micronaut.git
```

Install the library

```sh
mvn clean install
```

## Usage

### Dependency

Add the maven dependency

```xml
  <dependency>
    <groupId>com.rijpert</groupId>
    <artifactId>cucumber-micronaut</artifactId>
    <version>1.0.0</version>
  </dependency>
```

### Configuration

The Micronaut Object Factory needs to be configured as cucumber object factory.

```java
import com.davromalc.cucumber.micronaut.MicronautObjectFactory;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class) //Cucumber does not support JUnit 5 yet
@CucumberOptions(
    plugin = {"pretty", "html:target/features"},
    glue = {"com.example"},
    objectFactory = MicronautObjectFactory.class,
    features = "classpath:features", snippets = SnippetType.CAMELCASE)
public class CucumberRunnerITCase {

}
```

## Contributing

Contributions are what make the open source community such an amazing place to be, learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the Apache License. See `LICENSE` for more information.

Forked from:

Project Link: [https://github.com/david-romero/cucumber-micronaut](https://github.com/david-romero/cucumber-micronaut)
