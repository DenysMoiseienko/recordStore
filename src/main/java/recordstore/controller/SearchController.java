package recordstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import recordstore.entity.Artist;
import recordstore.entity.Label;
import recordstore.entity.Release;
import recordstore.service.SearchService;

import java.util.List;

@Controller
@RequestMapping(value = {"/", "/releases", "/labels", "/genres", "/artists", "/account"})
public class SearchController {

    private final SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }

    @GetMapping("/autocomplete")
    @ResponseBody
    public List<String> showAutocomplete(@RequestParam("term") String term) {
        return service.search(term);
    }

    @GetMapping("/search")
    public String showSearchResult(@RequestParam("search") String search, Model model) {
        Release release = service.getReleaseByTitle(search);
        Label label = service.getLabelByTitle(search);
        Artist artist = service.getArtistByName(search);

        if (release != null) {
            model.addAttribute("releaseId", release.getId());
            return "redirect:/releases/{releaseId}";
        }
        if (label != null) {
            model.addAttribute("labelId", label.getId());
            return "redirect:/labels/{labelId}";
        }
        if (artist != null) {
            model.addAttribute("artistId", artist.getId());
            return "redirect:/artists/{artistId}";
        }
        model.addAttribute("message", "No items found");
        return "client/search";
    }
}