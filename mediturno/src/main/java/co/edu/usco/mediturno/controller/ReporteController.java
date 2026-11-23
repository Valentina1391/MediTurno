package co.edu.usco.mediturno.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Acceso a datos mediante JDBC (JdbcTemplate).
 */
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final JdbcTemplate jdbc;

    public ReporteController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Cuenta las citas por estado usando JDBC.
     *
     * @return lista con el estado y la cantidad de citas
     */
    @GetMapping("/citas-por-estado")
    public List<Map<String, Object>> citasPorEstado() {
        return jdbc.queryForList(
            "SELECT estado, COUNT(*) AS total FROM citas GROUP BY estado");
    }
}
