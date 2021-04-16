package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLException;

@Controller
//@RequestMapping("/home")
public class HomeController{

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;
    private FileService fileService;
    public String ifError = null;
    public String ifSuccess = null;
    public String successMessage = null;
    public String errorMessage = null;


    @Autowired
    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, EncryptionService encryptionService, FileService fileService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }

    @GetMapping("/home")
    public String homeView(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                           @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                           Model model, Authentication authentication){
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "home";
    }

    @PostMapping("/home")
    public String writeNote(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                            @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject, RedirectAttributes redirectAttributes,
                            Model model, Authentication authentication){
        this.ifError = null;
        this.ifSuccess = null;
        this.errorMessage = null;
        this.successMessage = null;
        if(noteFormObject.getNoteDescription().toString().length() >= 1000){
            redirectAttributes.addFlashAttribute("ifError", true);
            redirectAttributes.addFlashAttribute("errorMessage","Failed to Save, Please keep Description below 1000 characters");
            return "redirect:/home";

        }
        int rowsAdded =  noteService.addNote(noteFormObject, authentication);
        if (rowsAdded < 0){
            this.errorMessage = "There was an error for adding a note. Please try again";
        }
        if (this.ifError == null) {

            redirectAttributes.addFlashAttribute("ifSuccess",true);
            redirectAttributes.addFlashAttribute("successMessage", "You successfully added a new note");
        } else {
            redirectAttributes.addFlashAttribute("ifError", true);
            redirectAttributes.addFlashAttribute("errorMessage",this.errorMessage);
        }
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "redirect:/home";
    }

    @GetMapping("/deleteNote")
    public String deleteCurrentNote(@RequestParam(value = "id") Integer id, @RequestParam(value = "noteid") Integer noteid, Model model,
                                    @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                    @ModelAttribute("noteFormObject") NoteFormObject noteFormObject, Authentication authentication,RedirectAttributes redirectAttributes){
        this.ifError = null;
        this.ifSuccess = null;
        this.errorMessage = null;
        this.successMessage = null;
        User user = userService.getUserById(id);
        Note note = noteService.getNoteById(noteid);
        int rowsAdded = noteService.deleteNote(user.getUserid(), note.getNoteid());
        if (rowsAdded < 0){
            this.errorMessage = "There was an error for deleting note. Please try again";
        }
        if (this.ifError == null) {
            redirectAttributes.addFlashAttribute("ifSuccess",true);
            redirectAttributes.addFlashAttribute("successMessage", "You successfully deleted note");
        } else {
            redirectAttributes.addFlashAttribute("ifError", true);
            redirectAttributes.addFlashAttribute("errorMessage",this.errorMessage);
        }
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        System.out.println("Executing deleteCurrentNote()");
        return "redirect:/home";
    }

    @PostMapping("/updateNote")
    public String updateNote(@RequestParam("noteId") Integer noteId, Model model, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                             @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,RedirectAttributes redirectAttributes,
                             @ModelAttribute("noteFormObject") NoteFormObject noteFormObject,Authentication authentication){
        this.ifError = null;
        this.ifSuccess = null;
        this.errorMessage = null;
        this.successMessage = null;
        System.out.println("noteId: " + noteId);
        if(noteFormObject.getNoteDescription().toString().length() >= 1000){
            redirectAttributes.addFlashAttribute("ifError", true);
            redirectAttributes.addFlashAttribute("errorMessage","Failed to Save ! Please keep Description below 1000 characters");
            return "redirect:/home";

        }
        int rowsAdded = noteService.updateNoteById(noteFormObject.getNoteTitle(), noteFormObject.getNoteDescription(), noteId);
        if (rowsAdded < 0){
            this.errorMessage = "There was an error for updating a note. Please try again";
        }
        if (this.ifError == null) {
            redirectAttributes.addFlashAttribute("ifSuccess",true);
            redirectAttributes.addFlashAttribute("successMessage", "Your changes are Saved");
        } else {
            redirectAttributes.addFlashAttribute("ifError", true);
            redirectAttributes.addFlashAttribute("errorMessage",this.errorMessage);
        }
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "redirect:/home";
    }

    @PostMapping("/add-credential")
    public String addCredential(@ModelAttribute("noteFormObject") NoteFormObject noteFormObject, @ModelAttribute("fileFormObject") FileFormObject fileFormObject,
                                @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                Model model, Authentication authentication,RedirectAttributes redirectAttributes){
        this.ifError = null;
        this.ifSuccess = null;
        this.errorMessage = null;
        this.successMessage = null;
        int rowsAdded = credentialService.addCredential(credentialFormObject,authentication);
        if (rowsAdded < 0){
            this.errorMessage = "There was an error for adding a credential. Please try again";
        }
        if (this.ifError == null) {
            redirectAttributes.addFlashAttribute("ifSuccess",true);
            redirectAttributes.addFlashAttribute("successMessage", "You successfully added a new credential");
        } else {
            redirectAttributes.addFlashAttribute("ifError", true);
            redirectAttributes.addFlashAttribute("errorMessage",this.errorMessage);
        }

        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "redirect:/home";
    }

    @GetMapping("/delete-credential")
    public String deleteCredential(@RequestParam(value = "credentialId") Integer credentialId,@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                                   @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                   Model model, Authentication authentication,RedirectAttributes redirectAttributes){
        this.ifError = null;
        this.ifSuccess = null;
        this.errorMessage = null;
        this.successMessage = null;
        int rowsUpdated = credentialService.deleteCredential(credentialId);
        if (rowsUpdated < 0){
            this.errorMessage = "There was an error for deleting a credential. Please try again";
        }
        if (this.ifError == null) {
            redirectAttributes.addFlashAttribute("ifSuccess",true);
            redirectAttributes.addFlashAttribute("successMessage", "You successfully deleted a credential");
        } else {
            redirectAttributes.addFlashAttribute("ifError", true);
            redirectAttributes.addFlashAttribute("errorMessage",this.errorMessage);
        }
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "redirect:/home";
    }

    @PostMapping("/update-credential")
    public String updateCredential(@RequestParam(value = "credentialId") Integer credentialId,@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                                   @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                                   Model model, Authentication authentication,RedirectAttributes redirectAttributes){
        this.ifError = null;
        this.ifSuccess = null;
        this.errorMessage = null;
        this.successMessage = null;
        int rowsUpdated = credentialService.updateCredential(credentialFormObject.getCredentialUrl(),credentialFormObject.getCredentialUsername(), credentialFormObject.getCredentialPassword(),credentialId);
        if (rowsUpdated < 0){
            this.errorMessage = "There was an error for updating a credential. Please try again";
        }
        if (this.ifError == null) {
            redirectAttributes.addFlashAttribute("ifSuccess",true);
            redirectAttributes.addFlashAttribute("successMessage", "You successfully updated a credential");
        } else {
            redirectAttributes.addFlashAttribute("ifError", true);
            redirectAttributes.addFlashAttribute("errorMessage",this.errorMessage);
        }
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "redirect:/home";
    }

    @PostMapping("/add-file")
    public String uploadFile(@ModelAttribute("fileFormObject") FileFormObject fileFormObject, @ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                             @ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,RedirectAttributes redirectAttributes,
                             Model model, Authentication authentication) throws IOException, SQLException {
        this.ifError = null;
        this.ifSuccess = null;
        this.errorMessage = null;
        this.successMessage = null;
        String uploadError = null;
        String successUpload = "File has been successfully uploaded";//successUpload uploadError
        if (fileFormObject.getFileUpload().isEmpty()) {
            redirectAttributes.addFlashAttribute("successUpload", false);
            redirectAttributes.addFlashAttribute("uploadError", true);
            redirectAttributes.addFlashAttribute("uploadError", "File not selected to upload");
            return "redirect:/home";
        }
        if(fileService.fileAlreadyExists(fileFormObject.getFileUpload().getOriginalFilename())){
            //uploadError = "File with this name already exists";
            //model.addAttribute("uploadError", uploadError);
            redirectAttributes.addFlashAttribute("successUpload", false);
            redirectAttributes.addFlashAttribute("uploadError", true);
            redirectAttributes.addFlashAttribute("uploadError", "file name already exists");
            return "redirect:/home";

        } else{
            try {
                model.addAttribute("successUpload", successUpload);
                if(fileFormObject.getFileUpload().getSize() > 5242880){
                    model.addAttribute("uploadError", "File size exceed maximum");
                    throw new MaxUploadSizeExceededException(fileFormObject.getFileUpload().getSize());
                }
                fileService.addFile(fileFormObject, authentication);
                redirectAttributes.addFlashAttribute("successUpload", true);
                redirectAttributes.addFlashAttribute("successUpload", "New File added successfully");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                redirectAttributes.addFlashAttribute("uploadError", true);
                redirectAttributes.addFlashAttribute("uploadError", "System error!" + e.getMessage());
            }

        }
        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "redirect:/home";
    }



    @GetMapping("/delete-file")
    public String deleteFile(@RequestParam(value = "id") Integer fileId,@ModelAttribute("noteFormObject") NoteFormObject noteFormObject,
                             @ModelAttribute("fileFormObject") FileFormObject fileFormObject,@ModelAttribute("credentialFormObject") CredentialFormObject credentialFormObject,
                             Model model, Authentication authentication,RedirectAttributes redirectAttributes){
        this.ifError = null;
        this.ifSuccess = null;
        this.errorMessage = null;
        this.successMessage = null;
        try {
            fileService.deleteFile(fileId);
            redirectAttributes.addFlashAttribute("ifSuccess", true);
            redirectAttributes.addFlashAttribute("successMessage", "file Deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("ifError", true);
            redirectAttributes.addFlashAttribute("errorMessage", "System error!" + e.getMessage());
        }

        model.addAttribute("notes", noteService.getAllNotes(authentication));
        model.addAttribute("credentials", credentialService.getAllCredentials(authentication));
        model.addAttribute("decryptedPassword",encryptionService);
        model.addAttribute("files", fileService.getAllFiles(authentication));
        return "redirect:/home";
    }

    @GetMapping("/download-file")
    public ResponseEntity downloadFile(@RequestParam(value = "fileId") Integer fileId){
        File file = fileService.downloadFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }


}
