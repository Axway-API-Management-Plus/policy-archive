# Description
- Maven plugin is used to create fed, pol and env files 


## API Management Version Compatibilty
This artefact was successfully tested for the following versions:
- Axway AMPLIFY API Management 7.5.3 and 7.6.2


## Prerequisites

- Axway AMPLIFY API Management 7.5.3 or above
- JDK 1.8.0_xxx
- Apache Maven 3.3.9 or above 

## Build the project 

```bash
	$mvn clean install
```

## Example to create pol and env

```xml
 <plugin>
                                <groupId>com.axway</groupId>
                                <artifactId>fed-archive</artifactId>
                                <version>1.0-SNAPSHOT</version>
                                <extensions>true</extensions>
                                <executions>
                                        <execution>
                                                <id>pol</id>
                                                <phase>package</phase>
                                                <goals>
                                                        <goal>pol</goal>
                                                </goals>
                                        </execution>
                                </executions>
                                <configuration>
                                        <name>${archive.name}</name>
                                        <targetPassphrase></targetPassphrase>
                                        <targetPassphrase></targetPassphrase>
                                        <projpackPath>/opt/Axway/APIM/apigateway/posix/bin/projpack</projpackPath>
                                        <polFilePath></polFilePath>

                                </configuration>

                        </plugin>

```

## Example to create fed

```xml
 <plugin>
                                <groupId>com.axway</groupId>
                                <artifactId>fed-archive</artifactId>
                                <version>1.0-SNAPSHOT</version>
                                <extensions>true</extensions>
                                <executions>
                                        <execution>
                                                <id>fed</id>
                                                <phase>package</phase>
                                                <goals>
                                                        <goal>fed</goal>
                                                </goals>
                                        </execution>
                                </executions>
                                <configuration>
                                        <name>${archive.name}</name>
                                        <targetPassphrase></targetPassphrase>
                                        <targetPassphrase></targetPassphrase>
                                        <projpackPath>/opt/Axway/APIM/apigateway/posix/bin/projpack</projpackPath>
                                        <polFilePath></polFilePath>

                                </configuration>

                        </plugin>

```

## Contributing
Please read [Contributing.md](https://github.com/Axway-API-Management-Plus/Common/blob/master/Contributing.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Team

![alt text](https://github.com/Axway-API-Management-Plus/Common/blob/master/img/AxwayLogoSmall.png)
Axway Team



## License
[Apache License 2.0](/LICENSE)
