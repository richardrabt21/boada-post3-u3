package com.universidad.confudes.acceso;

import com.universidad.confudes.certificados.ServicioCertificados;
import com.universidad.confudes.certificados.SolicitudCertificado;

// Proxy de protección: verifica el rol ANTES de delegar en el servicio real.
// Si el rol no es válido, corta el flujo sin ejecutar la operación costosa.
public class ProxyControlAcceso implements ServicioCertificados {

    private final ServicioCertificados delegado;

    public ProxyControlAcceso(ServicioCertificados delegado) {
        this.delegado = delegado;
    }

    @Override
    public byte[] emitir(SolicitudCertificado solicitud) {
        String rol = ContextoUsuario.rolActual();
        if (!rol.equals("ORGANIZADOR") && !rol.equals("ADMIN")) {
            throw new SecurityException("Rol no autorizado para esta operación: " + rol);
        }
        return delegado.emitir(solicitud);
    }
}