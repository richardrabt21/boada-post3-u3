package com.universidad.confudes.certificados;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/certificados")
public class ControladorCertificados {

    private final FachadaCertificados fachada;

    public ControladorCertificados(FachadaCertificados fachada) {
        this.fachada = fachada;
    }

    @PostMapping("/{eventoId}/{participanteId}")
    public ResponseEntity<String> emitir(@PathVariable String eventoId, @PathVariable String participanteId,
                                          @RequestParam String nombre, @RequestParam String correoDestino) {
        String resultado = fachada.emitirCertificado(eventoId, participanteId, nombre, correoDestino);
        return resultado != null ? ResponseEntity.ok(resultado) : ResponseEntity.status(403).body("Asistencia insuficiente");
    }
}