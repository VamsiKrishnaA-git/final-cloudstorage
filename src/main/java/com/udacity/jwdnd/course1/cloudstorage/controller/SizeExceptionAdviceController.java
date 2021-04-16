package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
@ControllerAdvice
public class SizeExceptionAdviceController extends Throwable {
        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes){
            redirectAttributes.addFlashAttribute("uploadError", true);
            redirectAttributes.addFlashAttribute("uploadError", "File size exceed maximum");
            return "redirect:/home";
        }


}

