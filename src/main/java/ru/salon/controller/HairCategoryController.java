package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.salon.model.Hair;
import ru.salon.model.HairCategory;
import ru.salon.service.HairService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class HairCategoryController {

    private HairService hairService;

    @GetMapping("/getAllHairCategories")
    public List<HairCategory> getAllHairCategories() {
        return hairService.getAllHairCategories();
    }

    @GetMapping("/getAllHairs")
    public List<Hair> getAllHairs() {
        return hairService.getAllHairs();
    }
}
