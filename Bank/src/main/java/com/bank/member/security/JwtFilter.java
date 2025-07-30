package com.bank.member.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bank.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		   String authHeader = request.getHeader("Authorization");
		   
		   if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}

	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);
	            
	            if (jwtUtil.isTokenValid(token)) {
	                String userId = jwtUtil.getValue(token,"");

	                UsernamePasswordAuthenticationToken authentication =
	                        new UsernamePasswordAuthenticationToken(userId, null, List.of());
	                SecurityContextHolder.getContext().setAuthentication(authentication);
	            }
	        }

	        chain.doFilter(request, response);
	    }
		
	}

}
