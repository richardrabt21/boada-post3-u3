# Post-contenido — Unidad 3: Patrones Estructurales en ConfUDES

## Descripción
Repositorio del post-contenido de la Unidad 3 de Patrones de Diseño de Software. Proyecto Spring Boot (confudes-patrones-estructurales) que resuelve las necesidades del backend de ConfUDES.

## Cómo ejecutar

## Decisiones de diseño

### Necesidad 1 — Registro de asistencia
Se aplicó el patrón **Adapter**. El problema era una incompatibilidad de contratos: el SDK del proveedor (`QRCheckClient`) expone un contrato distinto al que ya usa el resto del sistema (`ServicioAsistencia`). `AdaptadorQRCheck` implementa `ServicioAsistencia` y traduce internamente los tipos y códigos de respuesta hacia y desde `QRCheckClient`. Se descartó Facade porque aquí no hay múltiples colaboradores que simplificar: hay un único colaborador (`QRCheckClient`) cuyo contrato es incompatible con el que el cliente ya espera. Facade no resuelve incompatibilidad de tipos, solo reduce el número de dependencias conocidas por el cliente.

### Necesidad 2 — Emisión de certificados
Se aplicó el patrón **Facade**. `ControladorCertificados` conocía y orquestaba directamente 4 servicios (`ValidadorAsistencia`, `GeneradorCertificadoPDF`, `FirmaDigitalService`, `EnvioCorreoService`), lo que lo hacía frágil ante cualquier cambio en esos servicios. `FachadaCertificados` centraliza la orquestación (validar → generar → firmar → enviar) detrás de una única operación simple, y el controlador ahora depende de un solo colaborador. Se descartó Adapter porque ninguno de los 4 servicios tiene un contrato incompatible que traducir: todos ya exponen APIs funcionales tal como están; el problema real era el exceso de colaboradores conocidos por el cliente, no una incompatibilidad de interfaces.

### Necesidad 3 — Mejoras opcionales del certificado
Se aplicó el patrón **Decorator**. Cada mejora (`DecoradorMarcaDeAgua`, `DecoradorCodigoQR`, `DecoradorTraduccion`) implementa `ServicioCertificados` y envuelve a otro `ServicioCertificados`, delegando siempre en él y añadiendo su propio efecto sobre el resultado. Se descartaron dos alternativas: la herencia (crear una subclase por cada combinación posible, lo cual con 3 mejoras ya implica 8 clases y no escala si se agregan más mejoras) y los parámetros booleanos en `emitir()` (un único método con banderas que crece en complejidad con cada mejora nueva y mezcla toda la lógica condicional en un solo lugar). El patrón usado en la Necesidad 4 (Proxy) tampoco serviría aquí: un Proxy decide si delega o no, pero las mejoras de esta necesidad necesitan combinarse libremente y siempre ejecutar la operación base — no tienen ninguna condición de acceso que evaluar.

### Necesidad 4 — Control de acceso a la descarga masiva
Se aplicó el patrón **Proxy** (de protección). `ProxyControlAcceso` implementa `ServicioCertificados` y envuelve a otro `ServicioCertificados`, pero a diferencia de los Decorators, decide si delega o no: verifica el rol con `ContextoUsuario` antes de llamar al colaborador real, y si el usuario no es ORGANIZADOR ni ADMIN, lanza una excepción sin ejecutar la lógica costosa de firma digital. El patrón de la Necesidad 3 (Decorator) no serviría aquí, porque un Decorator siempre delega primero y añade después — para cuando pudiera revisar el rol, la operación costosa ya se habría ejecutado, justo lo que se quiere evitar.

### Reflexión — Composite y Flyweight (opcional)
Para la agenda jerárquica de ConfUDES (tracks, sesiones y actividades dentro de cada sesión), el patrón que encajaría de forma natural es **Composite**, porque es una estructura de árbol de partes donde tracks, sesiones y actividades pueden tratarse de manera uniforme (cada nodo puede contener otros nodos). En cambio, **Flyweight** no aplica a las credenciales QR: ese patrón sirve para compartir estado entre muchos objetos similares y así ahorrar memoria, pero cada credencial QR tiene datos únicos e irrepetibles (el payload de cada participante), no hay estado compartible que factorizar.

## Conclusiones
Este laboratorio permitió distinguir con claridad cuándo aplicar cada patrón estructural según el síntoma real del problema, más allá de su forma superficial. Lo más difícil fue diferenciar Decorator de Proxy en la Necesidad 4, ya que estructuralmente ambos envuelven la misma interfaz; la clave estuvo en identificar la intención (añadir vs. decidir) más que la forma del código. También quedó claro que Adapter y Facade resuelven problemas distintos aunque ambos "envuelvan" algo: uno traduce un contrato incompatible, el otro reduce el número de colaboradores conocidos por el cliente. En general, el ejercicio reforzó que elegir el patrón

## Herramientas utilizadas
- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code, Git, GitHub