## Configuration
OpenWMS.org defines additional configuration parameters beside the standard Spring Framework ones. All custom parameters are children of the
`owms` property namespace.

| Parameter                             | Type   | Default profile value           | Description                                                                                     |
|---------------------------------------|--------|---------------------------------|-------------------------------------------------------------------------------------------------|
| owms.eureka.hostname                  | string | `localhost`                     | Hostname where the discovery server is running                                                  |
| owms.eureka.port                      | string | `8761`                          | Port of the discovery server instance                                                           |
| owms.eureka.url                       | string | `http://user:sa@localhost:8761` | URI to connect to the discovery server - a combination or hostname, port, username and password |
| owms.eureka.user.name                 | string | `user`                          | User's name to access the discovery server                                                      |
| owms.eureka.user.password             | string | `sa`                            | User's password to access to the discovery server                                               |
| owms.eureka.zone                      | string | `${owms.eureka.url}/eureka/`    | URI to get the zone settings from Eureka discovery server                                       |
| owms.srv.hostname                     | string | `localhost`                       | The hostname the service' is accessible from Eureka clients                                   |
| owms.srv.protocol                     | string | `http`                            | The protocol the service' is accessible from Eureka clients                                   |  
| owms.security.encoder.bcrypt.strength | int    | `4`                               | The encryption strength used for BCrypt encryption                                            |
| owms.security.successUrl              | string | `/`                               | The URL where the UAA service shall redirect after successful authorization                   |
| owms.security.system.username         | string | `openwms`                         | The name of the system user with all privileges                                               |
| owms.security.system.password         | string | `openwms`                         | The password of the system user with all privileges                                           |
| owms.tracing.url                      | string | `http://localhost:4317`           | The URL where the OpenTelemetry service accepts traces                                        |
