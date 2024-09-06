package cyou.devify.blog.exceptions;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException.ServiceUnavailable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import cyou.devify.blog.entities.User;
import cyou.devify.blog.vm.ExceptionResponseViewModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpServletRequest request) {
    return new ResponseEntity<Object>(new ExceptionResponseViewModel(
        LocalDateTime.now().toInstant(ZoneOffset.UTC),
        "Failed to read the request body. Check the JSON format.",
        request.getRequestURL().toString(),
        HttpStatus.BAD_REQUEST.value()),
        HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoHandlerFoundException.class)
  public Object handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpServletRequest request) {
    var description = request.getRequestURL().toString();
    if (description.contains("/api/")) {
      return new ResponseEntity<>(
          new ExceptionResponseViewModel(Instant.now(), "Recurso não econtrado", description,
              HttpStatus.NOT_FOUND.value()),
          HttpStatus.NOT_FOUND);
    }
    var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var mv = new ModelAndView("404");
    mv.addObject("user", user instanceof User ? user : null);
    mv.setStatus(HttpStatus.NOT_FOUND);
    return mv;
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public Object generic(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    System.out.println(Instant.now());
    System.err.println("* Default exception handler: " + ex.getMessage() + "\n");
    // ex.printStackTrace();
    // if (!request.getServletPath().contains("/api/")) {
    // try {
    // response.sendRedirect("/session?next=" + request.getServletPath());
    // return null;
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    return request.getServletPath().contains("/api/")
        ? new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
        : new ModelAndView("error");
  }

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public Object methodNotSupported(HttpServletRequest request, HttpServletResponse response,
      HttpRequestMethodNotSupportedException ex) {

    return request.getServletPath().contains("/api/")
        ? new ResponseEntity<>(new ExceptionResponseViewModel(LocalDateTime.now().toInstant(ZoneOffset.UTC),
            "Verbo http proibido", request.getRequestURL().toString(), HttpStatus.BAD_REQUEST.value()),
            HttpStatus.BAD_REQUEST)
        : new ModelAndView("405");
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(InternalServerError.class)
  public ResponseEntity<ExceptionResponseViewModel> internalServerError(HttpServletRequest request,
      InternalServerError ex) {
    return new ResponseEntity<>(new ExceptionResponseViewModel(LocalDateTime.now().toInstant(ZoneOffset.UTC),
        ex.getMessage(), request.getRequestURL().toString(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadRequest.class)
  public ResponseEntity<ExceptionResponseViewModel> badRequest(HttpServletRequest request,
      BadRequest ex) {
    return new ResponseEntity<>(new ExceptionResponseViewModel(LocalDateTime.now().toInstant(ZoneOffset.UTC),
        ex.getMessage(), request.getRequestURL().toString(), HttpStatus.BAD_REQUEST.value()),
        HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoResourceFoundException.class)
  public Object resourceNotFound(HttpServletRequest request,
      NoResourceFoundException ex) {
    if (request.getServletPath().contains("/api/")) {
      return new ResponseEntity<>(
          new ExceptionResponseViewModel(LocalDateTime.now().toInstant(ZoneOffset.UTC),
              ex.getMessage(), request.getRequestURL().toString(),
              HttpStatus.NOT_FOUND.value()),
          HttpStatus.NOT_FOUND);
    }
    var mv = new ModelAndView("404", HttpStatus.NOT_FOUND);
    mv.addObject("pageTitle", "Recurso não encontrado");
    return mv;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFound.class)
  public Object notFound(HttpServletRequest request,
      NotFound ex) {
    return request.getServletPath().contains("/api/")
        ? new ResponseEntity<>(
            new ExceptionResponseViewModel(LocalDateTime.now().toInstant(ZoneOffset.UTC),
                ex.getMessage(), request.getRequestURL().toString(),
                HttpStatus.NOT_FOUND.value()),
            HttpStatus.NOT_FOUND)
        : new ModelAndView("404", HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(Conflict.class)
  public ResponseEntity<ExceptionResponseViewModel> conflict(HttpServletRequest request,
      Conflict ex) {
    return new ResponseEntity<>(new ExceptionResponseViewModel(LocalDateTime.now().toInstant(ZoneOffset.UTC),
        ex.getMessage(), request.getRequestURL().toString(), HttpStatus.CONFLICT.value()),
        HttpStatus.CONFLICT);
  }

  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  @ExceptionHandler(PayloadTooLarge.class)
  public ResponseEntity<ExceptionResponseViewModel> payloadTooLarge(HttpServletRequest request,
      PayloadTooLarge ex) {
    return new ResponseEntity<>(new ExceptionResponseViewModel(LocalDateTime.now().toInstant(ZoneOffset.UTC),
        ex.getMessage(), request.getRequestURL().toString(),
        HttpStatus.PAYLOAD_TOO_LARGE.value()),
        HttpStatus.PAYLOAD_TOO_LARGE);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(Unauthorized.class)
  public Object unauthorized(HttpServletRequest request,
      Unauthorized ex) {
    return request.getServletPath().contains("/api/")
        ? new ResponseEntity<>(
            new ExceptionResponseViewModel(LocalDateTime.now().toInstant(ZoneOffset.UTC),
                ex.getMessage(), request.getRequestURL().toString(),
                HttpStatus.UNAUTHORIZED.value()),
            HttpStatus.UNAUTHORIZED)
        : new ModelAndView("401", HttpStatus.UNAUTHORIZED);
  }

  // @ResponseStatus(HttpStatus.FORBIDDEN)
  // @ExceptionHandler(org.springframework.web.client.HttpClientErrorException.Forbidden.class)
  // public Object forbidden(HttpServletRequest request, HttpServletResponse
  // response,
  // org.springframework.web.client.HttpClientErrorException.Forbidden ex) {
  // if (request.getServletPath().contains("/api/")) {
  // return new ResponseEntity<>(
  // new ExceptionResponseViewModel(LocalDateTime.now().toInstant(ZoneOffset.UTC),
  // ex.getMessage(), request.getRequestURL().toString(),
  // HttpStatus.NOT_FOUND.value()),
  // HttpStatus.NOT_FOUND);
  // }
  // var mv = new ModelAndView("403", HttpStatus.NOT_FOUND);
  // mv.addObject("pageTitle", "Solicitação rejeitada");
  // return mv;
  // }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(Forbidden.class)
  public Object forbidden(HttpServletRequest request, HttpServletResponse response,
      Forbidden ex) {
    if (!request.getServletPath().contains("/api/")) {
      try {
        response.sendRedirect("/session?next=" + request.getServletPath());
        return null;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return request.getServletPath().contains("/api/")
        ? new ResponseEntity<>(HttpStatus.FORBIDDEN)
        : new ModelAndView("session");
  }

  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ExceptionHandler(ServiceUnavailable.class)
  public ResponseEntity<ExceptionResponseViewModel> serviceUnavailable(HttpServletRequest request,
      ServiceUnavailable ex) {
    return new ResponseEntity<>(new ExceptionResponseViewModel(LocalDateTime.now().toInstant(ZoneOffset.UTC),
        ex.getMessage(), request.getRequestURL().toString(),
        HttpStatus.SERVICE_UNAVAILABLE.value()),
        HttpStatus.SERVICE_UNAVAILABLE);
  }
}
