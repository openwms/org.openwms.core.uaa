## Configuration
OpenWMS.org defines additional configuration parameters beside the standard Spring Framework ones. All custom parameters are children of the
`owms` property namespace.

|Parameter|Type|Default profile value|Description|
|---------|----|-----------|
|owms.eureka.url|string|http://user:sa@localhost:8761|The base URL of the running Eureka service discovery server, inclusive schema and port|
|owms.eureka.zone|string|http://user:sa@localhost:8761/eureka/|The full Eureka registration endpoint URL|
|owms.service.protocol|string|http|The protocol the service' is accessible from Eureka clients|  
|owms.service.hostname|string|localhost|The hostname the service' is accessible from Eureka clients|
|owms.security.encoder.bcrypt.strength|int|15|The encryption strength used for BCrypt encryption|
|owms.security.provider.issuerUrl|string|http://localhost:8110|The URL of the token issuer (the server that issued the tokens)|
|owms.security.successUrl|string|/|The URL where the UAA service shall redirect after successful authorization|
|owms.security.system.username|string|openwms|The name of the system user with all privileges|
|owms.security.system.password|string|openwms|The password of the system user with all privileges|
