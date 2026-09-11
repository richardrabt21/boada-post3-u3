package com.universidad.confudes.certificados;

// Decorator: añade una marca de agua sobre el resultado de otro ServicioCertificados.
public class DecoradorMarcaDeAgua implements ServicioCertificados {

    private final ServicioCertificados delegado;

    public DecoradorMarcaDeAgua(ServicioCertificados delegado) {
        this.delegado = delegado;
    }

    @Override
    public byte[] emitir(SolicitudCertificado solicitud) {
        byte[] documento = delegado.emitir(solicitud);
        return UtilidadesPDF.aplicarMarcaDeAgua(documento, "ConfUDES 2026");
    }
}