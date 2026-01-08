This project is a Java Application that display the number of button pressed on Arduino, connected by USB port.
At the moment, it don't connect to Arduino but simulate the button press ad generate random number between 0 and 5.

## Requirements
- Java 25
- Maven 4
- Arduino (not yet implemented)

## How to run
1. Clone the repository
2. Compile the project using Maven:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="it.sarabanda.app.SarabandaApp"
