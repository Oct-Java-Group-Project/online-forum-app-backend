package com.beaconfire.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JWTValidationFilter implements org.springframework.cloud.gateway.filter.GlobalFilter {

    private static final String SECRET = "<BFSONLINEFORUMNPASSWORD> backward is <DROSWAPNMRUOFENILNOSFB>";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
    	System.out.print("Stsart filtering....."  );
    	String path = exchange.getRequest().getURI().getPath();
    	String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        
        // Allow public access to `/auth/**` routes
        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
        	
            String token = authHeader.substring(7); // Strip "Bearer " prefix
            System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + token );
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            // Add claims as headers for downstream services
            exchange.getRequest().mutate()
                    .header("X-Authenticated-User", claims.getSubject())
                    .header("X-User-Type", claims.get("type", String.class))
                    .build();
            System.out.print("End filtering....."  );
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
