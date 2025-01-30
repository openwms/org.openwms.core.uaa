## Configuration
OpenWMS.org defines additional configuration parameters beside the standard Spring Framework ones. All custom parameters are children of the
`owms` property namespace.

| Parameter                             |Type|Default profile value| Description                                                                            |
|---------------------------------------|----|---------------------|----------------------------------------------------------------------------------------|
| owms.eureka.url                       |string|http://user:sa@localhost:8761| The base URL of the running Eureka service discovery server, inclusive schema and port |
| owms.eureka.zone                      |string|http://user:sa@localhost:8761/eureka/| The full Eureka registration endpoint URL                                              |
| owms.srv.protocol                     |string|http| The protocol the service' is accessible from Eureka clients                            |  
| owms.srv.hostname                     |string|localhost| The hostname the service' is accessible from Eureka clients                            |
| owms.security.encoder.bcrypt.strength |int|4| The encryption strength used for BCrypt encryption                                     |
| owms.security.successUrl              |string|/| The URL where the UAA service shall redirect after successful authorization            |
| owms.security.system.username         |string|openwms| The name of the system user with all privileges                                        |
| owms.security.system.password         |string|openwms| The password of the system user with all privileges                                    |
| owms.tracing.url                      |string|http://localhost:4317| The url where the OpenTelementry service accepts traces                                |
