package recordstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import recordstore.entity.Account;
import recordstore.service.AccountDetailsServiceImpl;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final AccountDetailsServiceImpl accountService;

    public RegistrationController(AccountDetailsServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/registration")
    public String showSignUpForm(Model model) {
        model.addAttribute("userForm", new Account());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid Account user,
                          BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Your new password and confirmation password do not match");
            return "registration";
        }
        if (!accountService.saveUser(user)) {
            model.addAttribute("usernameError", "User with the same name already exists");
            return "registration";
        }
        return "redirect:/login";
    }
}