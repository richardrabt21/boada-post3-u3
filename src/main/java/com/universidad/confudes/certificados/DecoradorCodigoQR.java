package com.universidad.confudes.certificados;

// Decorator: añade un QR de verificación sobre el resultado de otro ServicioCertificados.
public class DecoradorCodigoQR implements ServicioCertificados {

    private final ServicioCertificados delegado;

    public DecoradorCodigoQR(ServicioCertificados delegado) {
        this.delegado = delegado;
    }

    @Override
    public byte[] emitir(SolicitudCertificado solicitud) {
        byte[] documento = delegado.emitir(solicitud);
        String urlVerificacion = "https://confudes.edu/verificar/" + solicitud.getParticipanteId();
        return UtilidadesPDF.insertarCodigoQR(documento, urlVerificacion);
    }
}