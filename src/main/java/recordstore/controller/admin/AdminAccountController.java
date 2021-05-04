package recordstore.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import recordstore.entity.Account;
import recordstore.error.AccountNotFoundException;
import recordstore.service.AccountService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/accounts/")
public class AdminAccountController {

    private final AccountService accountService;

    public AdminAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String getAllUsers(Model model, @RequestParam("page") Optional<Integer> page) {
        int currentPage = page.orElse(1);
        Page<Account> accounts = accountService.getAllUsers(PageRequest.of(currentPage - 1 ,10, Sort.by("username").descending()));
        model.addAttribute("accounts", accounts);
        getPages(model, accounts.getTotalPages());
        return "admin/accounts/index";
    }

    @PostMapping("/delete")
    public String deleteAccount(@RequestParam("id") long id) throws IOException {
        accountService.deleteAccount(id);
        return "redirect:/admin/accounts/";
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ModelAndView accountNotFoundHandler(AccountNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("/errorPages/pageNotFound");
        modelAndView.getModel().put("message", ex.getMessage());
        return modelAndView;
    }

    private void getPages(Model model, int pages) {
        if (pages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, pages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}