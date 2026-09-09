package com.universidad.confudes.asistencia;

import com.universidad.confudes.externo.qrcheck.QRCheckClient;
import com.universidad.confudes.externo.qrcheck.QRCheckRequest;
import com.universidad.confudes.externo.qrcheck.QRCheckResponse;
import org.springframework.stereotype.Service;

// Adapter: traduce el contrato de QRCheckAPI (externo) al contrato
// interno ServicioAsistencia, sin que ControladorCheckIn sepa que existe.
@Service
public class AdaptadorQRCheck implements ServicioAsistencia {

    private final QRCheckClient qrCheckClient;

    public AdaptadorQRCheck() {
        this.qrCheckClient = new QRCheckClient();
    }

    @Override
    public ResultadoCheckIn registrarAsistencia(String eventoId, String participanteId, String credencialQR) {
        long idEvento = eventoId.hashCode();

        QRCheckRequest request = new QRCheckRequest(credencialQR, idEvento);
        QRCheckResponse response = qrCheckClient.validar(request);

        boolean exitoso = response.getCodigoRespuesta() == 200;
        return new ResultadoCheckIn(exitoso, response.getDetalle());
    }
}