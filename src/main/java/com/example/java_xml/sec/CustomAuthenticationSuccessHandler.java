package com.example.java_xml.sec;

import com.example.java_xml.metier.Metier;
import com.example.java_xml.model.Etudiant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.stream().anyMatch(role -> role.getAuthority().equals("ROLE_admin"))) {
            response.sendRedirect("/all"); // Redirige l'administrateur vers /admin
        } else if (authorities.stream().anyMatch(role -> role.getAuthority().equals("ROLE_etudiant"))) {
             String etudiantApogee = authentication.getName();
            Etudiant etudiant = null;
            try {
                etudiant = Metier.getEtudiantByApogee(etudiantApogee);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
            response.sendRedirect("/profil?id="+etudiant.getId()); // Redirige l'utilisateur vers /user
        } else {
            response.sendRedirect("/login"); // Redirige les autres rôles vers /default
        }
    }
}
