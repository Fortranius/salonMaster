package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.salon.model.Hair;
import ru.salon.model.HairCategory;
import ru.salon.model.HairType;
import ru.salon.model.MasterCategory;
import ru.salon.repository.HairCategoryRepository;
import ru.salon.repository.HairRepository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HairService {

    private HairRepository hairRepository;
    private HairCategoryRepository hairCategoryRepository;

    @PostConstruct
    public void init() {
        List<HairCategory> hairsCategory = new ArrayList<>();

        hairsCategory.add(HairCategory.builder().id(1000L).price(BigDecimal.valueOf(30)).masterType(MasterCategory.START).hairType(HairType.HAIR_EXTENSION).build());
        hairsCategory.add(HairCategory.builder().id(1001L).price(BigDecimal.valueOf(40)).masterType(MasterCategory.MIDDLE).hairType(HairType.HAIR_EXTENSION).build());
        hairsCategory.add(HairCategory.builder().id(1002L).price(BigDecimal.valueOf(50)).masterType(MasterCategory.MIDDLE_PLUS).hairType(HairType.HAIR_EXTENSION).build());
        hairsCategory.add(HairCategory.builder().id(1003L).price(BigDecimal.valueOf(60)).masterType(MasterCategory.TOP).hairType(HairType.HAIR_EXTENSION).build());
        hairsCategory.add(HairCategory.builder().id(1004L).price(BigDecimal.valueOf(30)).hairType(HairType.HAIR_REMOVAL).build());

        hairCategoryRepository.saveAll(hairsCategory);

        List<Hair> hairs = new ArrayList<>();
        hairs.add(Hair.builder().id(1000L).minLength(40).maxLength(59).price(BigDecimal.valueOf(140)).build());
        hairs.add(Hair.builder().id(1001L).minLength(60).maxLength(69).price(BigDecimal.valueOf(160)).build());
        hairs.add(Hair.builder().id(1002L).minLength(70).maxLength(79).price(BigDecimal.valueOf(180)).build());
        hairs.add(Hair.builder().id(1003L).minLength(80).price(BigDecimal.valueOf(200)).build());
        hairRepository.saveAll(hairs);
    }

    public List<HairCategory> getAllHairCategories() {
        return hairCategoryRepository.findAll();
    }

    public List<Hair> getAllHairs() {
        return hairRepository.findAllByOrderByMinLength();
    }
}
