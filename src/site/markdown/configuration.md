## Configuration
OpenWMS.org defines additional configuration parameters beside the standard Spring Framework ones. All custom parameters are children of the
`owms` property namespace.

|Parameter|Type|Default profile value|Description|
|---------|----|-----------|
|owms.eureka.url|string|http://user:sa@localhost:8761|The base URL of the running Eureka service discovery server, inclusive schema and port|
|owms.eureka.zone|string|http://user:sa@localhost:8761/eureka/|The full Eureka registration endpoint URL|
|owms.service.protocol|string|http|The protocol the service' is accessible from Eureka clients|  
|owms.service.hostname|string|localhost|The hostname the service' is accessible from Eureka clients|
|owms.security.useEncoder|bool|false|Whether to encrypt the password in the database or not. **Highly recommended to set this to true in production**|
|owms.security.successUrl|string|/|The URL where the UAA service shall redirect after successful authorization|
|owms.security.system.username|string|openwms|The name of the system user with all privileges|
|owms.security.system.password|string|openwms|The password of the system user with all privileges|
