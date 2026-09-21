package vn.edu.quanlyhocphan.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request, RedirectAttributes ra) {
        log.warn("ResourceNotFound: {} on {}", ex.getMessage(), request.getRequestURI());
        ra.addFlashAttribute("errorMsg", ex.getMessage());
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank() && !referer.contains("/error")) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

    @ExceptionHandler(NghiepVuException.class)
    public String handleNghiepVu(NghiepVuException ex, HttpServletRequest request, RedirectAttributes ra) {
        log.warn("NghiepVuException: {} on {}", ex.getMessage(), request.getRequestURI());
        ra.addFlashAttribute("errorMsg", ex.getMessage());
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank() && !referer.contains("/error")) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, HttpServletRequest request, Model model) {
        log.error("Unhandled exception on " + request.getRequestURI(), ex);
        model.addAttribute("status", 500);
        model.addAttribute("error", "Lỗi xử lý hệ thống");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
}
