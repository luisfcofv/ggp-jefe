# JEFE - GENERAL GAME PLAYING PLAYER

Jefe is a general game playing gamer written in Kotlin used in the course T-732-GAPL, General Game Playing at Reykjavik University.

Jefe is based on general game playing base package https://github.com/ggp-org/ggp-base.

#### Requirements

* Java 8 or later.
* Kotlin 1.0.0-beta-4589 or later.

#### Usage

Jefe commands:

* `./jefe.sh <Port>` - Run Jefe gamer in the specified port
* `python jefe.py -p <Port>` - Run Jefe gamer in the specified port, default port is 9147


Gradle commands (use gradlew.bat instead if on Windows):

* `./gradlew -v` - Confirm Gradle version.
* `./gradlew tasks` - Show list of available tasks.
* `./gradlew build` - Compile code and run tests.
* `./gradlew assemble` - Compile without running tests.
* `./gradlew kiosk` - Run the Kiosk app.
* `./gradlew player` - Run the Player app.
* `./gradlew server` - Run the Server app.
* `./gradlew idea` - Create IntelliJ IDEA files.


See the PROGRAMS file for additional programs that can be run, including non-GUI player and server applications.

#### License

* This project is licensed under the New BSD License. Licensing information for
  the project can be found in the licenses/LICENSE file. Licensing information
  for the included external libraries can be found in the licenses/ directory.
