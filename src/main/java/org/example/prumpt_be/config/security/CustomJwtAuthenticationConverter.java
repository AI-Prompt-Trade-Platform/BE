package org.example.prumpt_be.config.security;

import org.example.prumpt_be.domain.entity.Users;
import org.example.prumpt_be.repository.UsersRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UsersRepository usersRepository;
    private final JwtGrantedAuthoritiesConverter scopesConverter = new JwtGrantedAuthoritiesConverter();

    public CustomJwtAuthenticationConverter(UsersRepository repo) {
        this.usersRepository = repo;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String sub   = jwt.getSubject();
        String email = jwt.getClaim("email");

        usersRepository.findByAuth0Id(sub)
            .orElseGet(() -> usersRepository.save(new Users(sub, email)));

        Collection<GrantedAuthority> authorities = scopesConverter.convert(jwt);
        return new JwtAuthenticationToken(jwt, authorities, sub);
    }
}
