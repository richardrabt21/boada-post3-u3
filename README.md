# Post-contenido — Unidad 3: Patrones Estructurales en ConfUDES

## Descripción
Repositorio del post-contenido de la Unidad 3 de Patrones de Diseño de Software. Proyecto Spring Boot (confudes-patrones-estructurales) que resuelve las necesidades del backend de ConfUDES.

## Cómo ejecutar

## Decisiones de diseño

### Necesidad 1 — Registro de asistencia
Se aplicó el patrón **Adapter**. El problema era una incompatibilidad de contratos: el SDK del proveedor (`QRCheckClient`) expone un contrato distinto al que ya usa el resto del sistema (`ServicioAsistencia`). `AdaptadorQRCheck` implementa `ServicioAsistencia` y traduce internamente los tipos y códigos de respuesta hacia y desde `QRCheckClient`. Se descartó Facade porque aquí no hay múltiples colaboradores que simplificar: hay un único colaborador (`QRCheckClient`) cuyo contrato es incompatible con el que el cliente ya espera. Facade no resuelve incompatibilidad de tipos, solo reduce el número de dependencias conocidas por el cliente.

### Necesidad 2 — Emisión de certificados
Se aplicó el patrón **Facade**. `ControladorCertificados` conocía y orquestaba directamente 4 servicios (`ValidadorAsistencia`, `GeneradorCertificadoPDF`, `FirmaDigitalService`, `EnvioCorreoService`), lo que lo hacía frágil ante cualquier cambio en esos servicios. `FachadaCertificados` centraliza la orquestación (validar → generar → firmar → enviar) detrás de una única operación simple, y el controlador ahora depende de un solo colaborador. Se descartó Adapter porque ninguno de los 4 servicios tiene un contrato incompatible que traducir: todos ya exponen APIs funcionales tal como están; el problema real era el exceso de colaboradores conocidos por el cliente, no una incompatibilidad de interfaces.

## Herramientas utilizadas
- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code, Git, GitHub