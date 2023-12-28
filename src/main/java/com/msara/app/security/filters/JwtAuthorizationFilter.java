package com.msara.app.security.filters;

import com.msara.app.security.jwt.JwtUtils;
import com.msara.app.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization"); //extraemos el header

        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            String token = tokenHeader.substring(7);

            if(jwtUtils.isTokenValid(token)) {
                String username = jwtUtils.getUsernameFromToken(token); //extraemos el username del token
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); //cargamos el usuario

                System.out.println(token);
                System.out.println(userDetails.getAuthorities());
                // pasamos el usuario, la contraseña en null y los permisos(roles)
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null,  userDetails.getAuthorities());

                //En SecurityContextHolder es donde se guarda al autenticacion
                //seteamos la autenticacion del usuario
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
