package com.bank.member.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.bank.member.bean.Member;
import com.bank.member.service.MemberService;
import com.bank.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private HandlerExceptionResolver handlerExceptionResolver;
	
	@Autowired
	private MemberService memberService;

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
	            
	            try {
	            	if (jwtUtil.isTokenValid(token)) {
	            		String userId = JwtUtil.getSubject(token);
	            		Member member = memberService.getMemberById(Integer.parseInt(userId) );
	            		
	            		UsernamePasswordAuthenticationToken authentication =
	            				new UsernamePasswordAuthenticationToken(member, null, List.of());
	            		SecurityContextHolder.getContext().setAuthentication(authentication);
	            	}
				}catch (Exception e) {
					handlerExceptionResolver.resolveException(request, response, null, e);
					return;
				}
	            
	        }

	        filterChain.doFilter(request, response);
	    }
		
	}


