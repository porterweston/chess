# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

[Phase 3 Diagram]
(https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSgM536HHCkARYGGABBECE5cAJsOAAjYBxQxp8zJgDmUCAFdsMAMRpgVAJ4wASik1IOYKMKQQ0RgO4ALJGBSZEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAel1lKAAdNABvLMoTAFsUABoYXA4ON2hpSpQS4CQEAF9MCnDYELC2SQ4oqBs7HygACiKoUoqqpVr6xubWgEpO1nYOGF6hEXEBwZhNFDAAVWzJ7Jm13bEJKW3QtSjLAFFRARgpmZgAMx0Sl9sphbvsHoFel1+pwomhdAgEOtqJtHjBQfcalEQMNhChzpRLsVgGVKtUFlBpDc5GCaqjnvA3qIkq8gUSSXManUKUsWggYEhXFMkdCtjtqRjDtiULjRLowB5CdNiSgqXsJXT5FE4IzmaylezgHKPEkIABrdD81yG+Ug8UHbaQjYDKLW41m9DClEQ0JQyIwV0m81oYWUB2hfzoMBRABMAAZY3l8gH3WgOuhpBptHoDIZoPxjjAADIQWyuQyeby+CNBH2sP1xRKpDLKGrONCJ77K0nzLnSDq+nq15HOmAIEsCxUzbuc+prKFe0Log5RY5nC6dsqqu723r0xmfDcqf4QQFCpfgx3DmEwcZoCDMOEIudOzio8+Yo4nWI9+rjMm9rcaVFJ5NSsd5Pn-eo-gBPVbTVHdLxFKJb3vGBHwQZ8r2AtE7SkLEcR8WV5UnZVAPVXdQO1d5dWTINLX9I04O3C8hyQhj5UDD15wGMM+j9WiuLrHpemrKMYDjBMCgE1NMHTTMdH0IxtBQC1i00PRmHLLwfD8ZBI1RAcomiARXkLV5mTSdIWw4Ns8mkkNBz6TYojHdS5XGaTMJFN9cI-aQUAQE4UCIhVPKYoCNSiEyzJoo1ONcAV2I8cLyMQ5yYA4XQJU9HjvT4qAoky7KB140SY3jWS0AzLQFJzYZpCLUYYAAcWVLYtMrXSAmYRD62a8zLM0ZU7LilMHIMl9DmQexWrKDgPNGoMvIXHD4Lwz8wBChaOJTMidxAl5wKS+LoJPJKUoQ1j0pQh94QwnLXzFNaP1XUQEVmzhxj28EDrAj4YFEQtCyOZUAEkBEqCsfHxfVZnkBBQFNGGpxBsoADllVOwEhrKC6WKckccc4B7RV6grUeJkq8rK8T40TImODTKr5OzIxsF0KBsEC+ACJUD73G0qs9JrfKjISZJBuGkxFvQenlQxsp+yEiasPw6UfA+7a3SDSoiYVlUSZ857Dleo0tfi77aQow7-ukrHzvfUmrpHG60Lu5bcsXXzJV5zW9dIvGrd+qimRZInwchwXka7GB4cR6P2X9xPMcSonA+wvcjvDz5j2x5V0947jr2zw28sMinwfG6nhbEiS5bKcGmeqrNFMMMwArHNwYAAKQgRL+cMOOQFNLr9LJsXTibdIiZGnag0TUS4AgMcoF1sGBCV7oVbYgArPu0E16TKkX5foDXhuBA9x6veNlcTi2sLHciv7Pjt3OHe9wvJuQu9bqfUub7MQ-HvAUmsT4r3PigcGlsM6gVOLEAQocYD5HAdANoCcVCp3zk-Mu38MpZQOKXce+DirK2rt1cqsZKrN1qkpRwFhEDSlgMAbAXNCBOBcALTqolt71miuZV4lk1BV2dteEAgU8CiFYcgEAHC0BfQAatIBhx-KBR8LcRQygFE4N+vwmiCIYBUGAAgXQr5EpTEgZUfgDVXQF1warEhhCi5O1Fo4qQVcRI10odQoAA)
