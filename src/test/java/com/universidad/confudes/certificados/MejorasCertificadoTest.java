package com.universidad.confudes.certificados;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MejorasCertificadoTest {

    private final SolicitudCertificado solicitud =
            new SolicitudCertificado("EVT-001", "PART-123", "Ana Ríos", "ana@correo.com");

    private ServicioCertificados crearBase() {
        return new FachadaCertificados(
                new ValidadorAsistencia(),
                new GeneradorCertificadoPDF(),
                new FirmaDigitalService(),
                new EnvioCorreoService()
        );
    }

    @Test
    void emiteSinNingunaMejoraActivada() {
        ServicioCertificados base = crearBase();
        assertDoesNotThrow(() -> base.emitir(solicitud));
    }

    @Test
    void combinaLasTresMejorasSinCrearUnaClaseNueva() {
        ServicioCertificados conTodo =
                new DecoradorMarcaDeAgua(new DecoradorCodigoQR(new DecoradorTraduccion(crearBase())));
        assertDoesNotThrow(() -> conTodo.emitir(solicitud));
    }

    @Test
    void unaSolaMejoraFuncionaDeFormaIndependiente() {
        ServicioCertificados soloMarcaDeAgua = new DecoradorMarcaDeAgua(crearBase());
        assertDoesNotThrow(() -> soloMarcaDeAgua.emitir(solicitud));
    }
}