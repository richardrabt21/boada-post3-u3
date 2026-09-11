package com.universidad.confudes.certificados;

// Decorator: traduce al inglés el resultado de otro ServicioCertificados.
public class DecoradorTraduccion implements ServicioCertificados {

    private final ServicioCertificados delegado;

    public DecoradorTraduccion(ServicioCertificados delegado) {
        this.delegado = delegado;
    }

    @Override
    public byte[] emitir(SolicitudCertificado solicitud) {
        byte[] documento = delegado.emitir(solicitud);
        return UtilidadesPDF.traducirAIngles(documento);
    }
}