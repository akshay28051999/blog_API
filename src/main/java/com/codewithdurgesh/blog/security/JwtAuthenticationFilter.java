package com.codewithdurgesh.blog.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component 
public class JwtAuthenticationFilter extends  OncePerRequestFilter{

	
	@Autowired
	private UserDetailsService UserDetailsService;
	
	@Autowired
	private JwtTokenHelper JwtTokenHelper;
	//it is called when api request is hit
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		//get Token
		String requestToken=request.getHeader("Authotirzation");
		
		//Bearer
		System.out.println(requestToken);
		
		String username=null;
		
		String token=null;
		
		if(requestToken!=null&&requestToken.startsWith("Bearer")) {
			
			//Remove Bearer from requestToken variable
			 token=requestToken.substring(7);
			 try {
				 username=this.JwtTokenHelper.getUsernameFromToken(token);
				  
			 }catch(IllegalArgumentException e) {
				 System.out.println("unable to get jwt token");
			 }catch(ExpiredJwtException e) {
				 System.out.println("jwt token has expired");
			 }catch(MalformedJwtException e) {
				 System.out.println("invlaid jwt");
			 }	
		}else {
			System.out.println("JWT token doesnot begin with bearer");
		}
		
		//once we get the token now validate the token
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails=this.UserDetailsService.loadUserByUsername(username);
			
			if(this.JwtTokenHelper.validateToken(token, userDetails)) {
				//sahi chal ra hai
				//authentication karna hai
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		else {
			System.out.println("invlaid jwt token");
		}
					
	}else {
		System.out.println("username is null or context is not null");
	}
		filterChain.doFilter(request, response);
}
}
