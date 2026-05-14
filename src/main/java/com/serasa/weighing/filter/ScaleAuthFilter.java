package com.serasa.weighing.filter;

import com.serasa.weighing.repository.ScaleRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
@RequiredArgsConstructor
public class ScaleAuthFilter implements Filter {

    private final ScaleRepository scaleRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Aplica autenticação apenas no endpoint de leitura das balanças
        if (httpRequest.getRequestURI().equals("/api/weighing/readings")
                && "POST".equalsIgnoreCase(httpRequest.getMethod())) {

            String apiKey = httpRequest.getHeader("X-Scale-Api-Key");
            if (apiKey == null || scaleRepository.findByApiKey(apiKey).isEmpty()) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"Balança não autorizada\"}");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
