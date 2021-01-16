package recordstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import recordstore.entity.Release;
import recordstore.entity.YouTubeVideo;
import recordstore.service.ReleaseService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/releases")
public class ReleaseController {

    private final ReleaseService service;

    public ReleaseController(ReleaseService service) {
        this.service = service;
    }

    @GetMapping
    public String showAllreleases(Model model, @RequestParam("page") Optional<Integer> page){
        int currentPage = page.orElse(1);
        Page<Release> releases = service.getAllReleases(PageRequest.of(currentPage - 1 ,10 ));
        model.addAttribute("releases", releases);
        getPages(model, releases);
        return "client/releases/index";
    }

    @GetMapping("/{id}")
    public String showReleaseInfo(@PathVariable long id, Model model){
        Release release = service.getRelease(id);
        model.addAttribute("release", release);
        getVideoIds(model, release);
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
            builder.append(video.getVideoId() + ",");
        }
        if (builder.length() > 0){
            builder.deleteCharAt(builder.length()-1);
        }
        model.addAttribute("videoIds", builder.toString());
    }
}