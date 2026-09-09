package com.universidad.confudes.certificados;

import org.springframework.stereotype.Service;

// Facade: reduce los cuatro colaboradores a una sola operación simple
// (validar -> generar -> firmar -> enviar), que es lo único que necesita conocer el controlador.
@Service
public class FachadaCertificados {

    private final ValidadorAsistencia validador;
    private final GeneradorCertificadoPDF generador;
    private final FirmaDigitalService firma;
    private final EnvioCorreoService correo;

    public FachadaCertificados(ValidadorAsistencia validador, GeneradorCertificadoPDF generador,
                                FirmaDigitalService firma, EnvioCorreoService correo) {
        this.validador = validador;
        this.generador = generador;
        this.firma = firma;
        this.correo = correo;
    }

    public String emitirCertificado(String eventoId, String participanteId, String nombre, String correoDestino) {
        if (!validador.tieneAsistenciaMinima(participanteId, eventoId, 0.8)) {
            return null; // el controlador interpreta null como "asistencia insuficiente"
        }

        byte[] doc = generador.iniciarDocumento("plantilla-2026");
        generador.insertarDatosParticipante(doc, nombre, eventoId, "2026-08-06");
        byte[] documentoFinal = generador.finalizarDocumento();

        FirmaDigitalService.Sesion sesion = firma.abrirSesion("cert-udes-2026.pfx");
        byte[] documentoFirmado = firma.firmar(sesion, documentoFinal);
        firma.cerrarSesion(sesion);

        correo.adjuntarArchivo(correoDestino, documentoFirmado, "certificado-" + participanteId + ".pdf");
        correo.enviar("Su certificado de participación", "Adjunto encontrará su certificado.");

        return "Certificado emitido y enviado";
    }
}