package com.example.Ez.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, Model model) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("statusCode", 500);
        mav.addObject("errorMessage", "Error interno del servidor (500)");
        mav.addObject("message", ex.getMessage());
        
        // Imprime el error en la consola para debugging
        ex.printStackTrace();
        
        return mav;
    }
}
