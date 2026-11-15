# Restaurant Ordering System (SDP Final Project)

A small console restaurant ordering system written in Java. It shows how **7 design patterns**
work together in one project.

* **Student:** LiYixin
* **Group:** SE-2526
* **Course:** Software Design Patterns

## Design patterns used

| # | Pattern          | Category   | Where it is used                                    |
|---|------------------|------------|-----------------------------------------------------|
| 1 | Factory Method   | Creational | `factory` – create burgers and drinks               |
| 2 | Builder          | Creational | `builder` – build a custom combo meal step by step  |
| 3 | Abstract Factory | Creational | `abstractfactory` – Chinese / Western set meals     |
| 4 | Bridge           | Structural | `bridge` – food type (staple/drink) + dining mode   |
| 5 | Facade           | Structural | `facade` – stock → payment → kitchen → notify       |
| 6 | Strategy         | Behavioral | `strategy` – discount rules for different customers |
| 7 | Visitor          | Behavioral | `visitor` – calories report and receipt export      |

## Project structure

```
src/main/java/
├── model/              # Dish, ComboMeal, Order
├── factory/            # Factory Method
├── builder/            # Builder
├── abstractfactory/    # Abstract Factory
├── bridge/             # Bridge
├── facade/             # Facade + subsystems
├── strategy/           # Strategy
├── visitor/            # Visitor
└── Main.java           # client / demo
```

## How to run

With Maven:

```bash
mvn -q clean package
java -jar target/restaurant-system-1.0.0.jar
```

Without Maven (plain JDK 21):

```bash
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out Main
```
