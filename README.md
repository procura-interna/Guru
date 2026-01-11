# Guru

Guru is a Discord bot built with [JDA](https://github.com/DV8FromTheWorld/JDA) (Java Discord API).

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

## Build

To build the project, run:

```bash
mvn clean package
```

The executable JAR will be located in the `target/` directory (e.g., `target/Guru-0.1.0-SNAPSHOT.jar`).

## Configuration

### Logging
The application uses **Logback** for logging. The configuration file `logback.xml` must be present in the working directory or specified via a system property.

A default `logback.xml` is provided at the root of the project.

## Usage

To run the bot, you must provide the path to a file containing your Discord API Token and the Role name to assign to new members.

First, create a file containing your Discord Bot Token:

```bash
echo "YOUR_DISCORD_TOKEN" > token.txt
```

Then run the bot:

```bash
java -Dlogback.configurationFile=logback.xml -jar target/Guru-0.1.0-SNAPSHOT.jar -apiToken token.txt -role <ROLE_NAME>
```

### Arguments

| Argument | Description | Required |
|---|---|---|
| `-apiToken` | Path to a file containing the Discord Bot Token | **Yes** |
| `-role` | The name of the role to assign to new members | **Yes** |
