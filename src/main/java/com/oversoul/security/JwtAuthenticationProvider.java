package com.oversoul.security;

import com.oversoul.entity.User;
import com.oversoul.repository.UserRepository;
import com.oversoul.security.model.JwtSettings;
import com.oversoul.security.model.JwtToken;
import com.oversoul.security.model.RawAccessJwtToken;
import com.oversoul.security.model.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An {@link AuthenticationProvider} implementation that will use provided
 * instance of {@link JwtToken} to perform authentication.
 */
@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JwtSettings jwtSettings;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // JwtAuthenticationToken auth = (JwtAuthenticationToken) authentication;

        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject();
        // long issuedDate = jwsClaims.getBody().getIssuedAt().getTime();

        log.info("Issued at :: {}  Time : {}", jwsClaims.getBody().getIssuedAt(),
                jwsClaims.getBody().getIssuedAt().getTime());
        Optional<User> user = userRepo.findById(Long.parseLong(subject));
        if (!user.isPresent())
            throw new UsernameNotFoundException(messageSource.getMessage("userNotFound", null, null));
        Long lastResetTokenDate = user.get().getLastLoginTime();
        JwtAuthenticationToken jwtAuthenticationToken = null;

        lastResetTokenDate /= 1000;
        // issuedDate /= 1000;
        List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
        List<GrantedAuthority> authorities = scopes.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UserContext context = UserContext.createContext(null, subject, null, authorities);

        jwtAuthenticationToken = new JwtAuthenticationToken(context, context.getAuthorities());
        return jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
