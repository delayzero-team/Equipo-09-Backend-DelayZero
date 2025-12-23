package com.alura.forohub.infra.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public abstract class ResponseHandler {

    //Utiliza http headers para devolver un mensaje informativo
    public static <T> ResponseEntity<T> buildResponse(String message, HttpStatusCode status, T result){
        var headers = new HttpHeaders();
        headers.add("message", message);
        return new ResponseEntity<>(result, headers, status);
    }

    /*
    Alternativa que incluye datos extra en el body:

    public static <T> ResponseEntity<T> buildResponse(String message, HttpStatus estado, T result) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("message", message); // Mensaje de exito o error
        response.put("status", estado); // Codigo http
        response.put("result", result); // Datos del service o null si es error
        return new ResponseEntity<>(response, estado);
    }
    */
}
