package recordstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import recordstore.entity.Genre;
import recordstore.entity.Release;
import recordstore.entity.YouTubeVideo;
import recordstore.service.GenreService;
import recordstore.service.ReleaseService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/releases")
public class ReleaseController {

    private final ReleaseService service;
    private final GenreService genreService;

    public ReleaseController(ReleaseService service, GenreService genreService) {
        this.service = service;
        this.genreService = genreService;
    }

    @GetMapping
    public String showAllReleases(Model model, @RequestParam("page") Optional<Integer> page){
        int currentPage = page.orElse(1);
        Page<Release> releases = service.getAllReleases(PageRequest.of(currentPage - 1 ,10, Sort.by("releaseDate").descending()));
        model.addAttribute("releases", releases);
        getPages(model, releases);
        return "client/releases/index";
    }

    @GetMapping("/genre/{id}")
    public String showAllReleasesByGenre(Model model,
                                         @PathVariable long id,
                                         @RequestParam("page") Optional<Integer> page){
        int currentPage = page.orElse(1);
        if (genreService.isPresent(id)){
            Genre genre = genreService.getGenre(id);
            Page<Release> releases = service.getAllReleasesByGenre(genre, PageRequest.of(currentPage - 1 ,10 , Sort.by("releaseDate").descending()));
            model.addAttribute("releases", releases);
            model.addAttribute("genre", genre);
            getPages(model, releases);
        } else {
            model.addAttribute("error", "Genre not found");
        }
        return "client/releases/list";
    }

    @GetMapping("/{id}")
    public String showReleaseInfo(@PathVariable long id, Model model){
        if (service.isPresent(id)){
            Release release = service.getRelease(id);
            model.addAttribute("release", release);
            getVideoIds(model, release);
        } else {
            model.addAttribute("error", "Release not found");
        }
        return "client/releases/view";
    }

    private void getPages(Model model, Page<Release> releases) {
        int pages = releases.getTotalPages();
        if (pages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, pages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }

    private void getVideoIds(Model model, Release release){
        StringBuilder builder = new StringBuilder();
        for(YouTubeVideo video : release.getPlaylist()){
            builder.append(video.getVideoId());
            builder.append(",");
        }
        if (builder.length() > 0){
            builder.deleteCharAt(builder.length()-1);
        }
        model.addAttribute("videoIds", builder.toString());
    }
}