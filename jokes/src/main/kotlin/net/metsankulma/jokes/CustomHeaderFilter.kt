package net.metsankulma.jokes

/*
 * http://www.gnuterrypratchett.com/index.php#springboot
 */

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import java.io.IOException
import jakarta.servlet.ServletException

@Component
class CustomHeaderFilter : Filter {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpResponse = response as HttpServletResponse
        httpResponse.setHeader("X-Clacks-Overhead", "GNU Terry Pratchett")

        chain.doFilter(request, response)
    }
}