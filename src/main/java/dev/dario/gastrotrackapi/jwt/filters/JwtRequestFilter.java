package dev.dario.gastrotrackapi.jwt.filters;

import dev.dario.gastrotrackapi.jwt.services.ApplicationUserDetailsService;
import dev.dario.gastrotrackapi.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* The doFilterInternal method in the JwtERequestFilter class is responsible for filtering incoming HTTP requests to validate JWT tokens and set the authentication context.
* */

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final ApplicationUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // JWT Extraction
        final String authorizationHeader =
                request.getHeader("Authorization");

        String username = null;
        String token = null;

        if (authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ")) {

            token = authorizationHeader.substring(7);
            username = jwtUtil.extractUserName(token);
        }

        // JWT extraction section
        // JWT validation and  creating the new
        // -> UsernamePasswordAuthenticationToken
        if ( username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService
                    .loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {

                var usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(
                                usernamePasswordAuthenticationToken
                        );
            }
        }

        filterChain.doFilter(request, response);
    }
}
