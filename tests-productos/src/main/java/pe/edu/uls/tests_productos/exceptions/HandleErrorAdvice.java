package pe.edu.uls.tests_productos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pe.edu.uls.tests_productos.cu.adminEquipo.exeptions.EquipoDuplicadoException;
import pe.edu.uls.tests_productos.cu.adminEquipo.exeptions.EquipoInvalidoException;
import pe.edu.uls.tests_productos.cu.adminEquipo.exeptions.EquipoNoEncontradoException;

@RestControllerAdvice
public class HandleErrorAdvice {

        @ExceptionHandler(EquipoInvalidoException.class)
        public ResponseEntity<String> handleEquipoInvalidoException(
                        EquipoInvalidoException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(EquipoNoEncontradoException.class)
        public ResponseEntity<String> handleEquipoNoEncontradoException(
                        EquipoNoEncontradoException ex) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(EquipoDuplicadoException.class)
        public ResponseEntity<String> handleEquipoDuplicadoException(
                        EquipoDuplicadoException ex) {

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ex.getMessage());
        }

        @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
        public ResponseEntity<String> handleResponseStatusException(
                        org.springframework.web.server.ResponseStatusException ex) {

                return ResponseEntity
                                .status(ex.getStatusCode())
                                .body(ex.getReason());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> genericHandler(Exception ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor: " + ex.getMessage());
        }
}