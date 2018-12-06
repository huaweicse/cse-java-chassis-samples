package io.provider;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path = "/accessDenied")
@ApiResponses({
    @ApiResponse(code = 403, response = DeniedInfo.class, message = "deny response")
})
public class AccessDeniedService {
  @Inject
  private BasicErrorController basicErrorController;

  private String generateMessage(HttpServletRequest request) {
    String message = "access denied";

    HttpSession httpSession = request.getSession(false);
    if (httpSession == null) {
      return message;
    }

    SecurityContext securityContext = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
    if (securityContext == null) {
      return null;
    }

    Authentication authentication = securityContext.getAuthentication();
    if (authentication == null) {
      return null;
    }

    User user = (User) authentication.getPrincipal();
    if (user == null) {
      return null;
    }

    return message + ",user=" + user.getUsername();
  }

  @GetMapping
  public void denyGet(HttpServletRequest request) {
    DeniedInfo deniedInfo = new DeniedInfo();
    deniedInfo.setTimestamp(new Date());
    deniedInfo.setMessage(generateMessage(request));
    deniedInfo.setBasicErrorControllerMessage(basicErrorController.error(request).toString());
    throw new InvocationException(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), deniedInfo);
  }

  @PostMapping
  public void denyPost(HttpServletRequest request) {
    denyGet(request);
  }
}
