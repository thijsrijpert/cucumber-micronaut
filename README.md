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

1. Clone the repo

```sh
git@github.com:david-romero/cucumber-micronaut.git
```

2. Build the library

```sh
mvn clean package
```

### Installation

1. Clone the repo

```sh
git@github.com:david-romero/shared-payments.git
```

2. Install the library

```sh
mvn clean install
```


<!-- USAGE EXAMPLES -->
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
    glue = {"com.davromalc.cucumber.micronaut.examples.acceptance"},
    objectFactory = MicronautObjectFactory.class,
    features = "classpath:features", snippets = SnippetType.CAMELCASE)
public class CucumberRunnerITCase {

}
```

### Steps usage


1. Beans definitions

```java
import javax.inject.Singleton;

@Singleton
public class World {
  
}
```

```java
@Singleton
final class AddFriend implements UseCase<AddFriendParams, User> {

  private final FriendRepository friendRepository;

  public AddFriend(FriendRepository friendRepository) {
    this.friendRepository = friendRepository;
  }

  @Override
  public Either<Validation, User> execute(AddFriendParams params) {
     //Implementation...
  }
}
```

2. Step implementation

```java
package com.davromalc.cucumber.micronaut.examples.acceptance.steps;

public class AddFriendStep {

  private final UseCase<AddFriendParams, User> addFriend;

  private final FriendRepository friendRepository;

  private final World world;

  public AddFriendStep(@Named("addFriend") UseCase<AddFriendParams, User> addFriend, FriendRepository friendRepository,
      World world) {
    this.addFriend = addFriend;
    this.friendRepository = friendRepository;
    this.world = world;
  }

  @Given("a new friend with name {string}")
  public void aNewFriendWithName(String friendName) {
    world.setFriendParams(new AddFriendParams(friendName));
  }


  @When("the friend is added")
  public void theFriendIsAdded() {
    addFriend.execute(world.getFriendParams());
  }

  @Then("the new friend {string} has been saved")
  public void theNewFriendHasBeenSaved(String newFriendName) {
    final Optional<String> newFriendNameFromDatabase = friendRepository.findByName(newFriendName)
        .map(User::getName)
        .filter(newFriendName::equals)
        .findAny();
    assertThat(newFriendNameFromDatabase)
        .matches(Optional::isPresent)
        .get()
        .asString()
        .isEqualTo(newFriendName);
  }
}
```

## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the Apache License. See `LICENSE` for more information.

Forked from:

Project Link: [https://github.com/david-romero/cucumber-micronaut](https://github.com/david-romero/cucumber-micronaut)
