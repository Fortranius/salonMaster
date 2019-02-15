package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.salon.model.Hair;
import ru.salon.model.HairCategory;
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
    public void init() throws Exception {
        if (!CollectionUtils.isEmpty(hairRepository.findAll()))
            return;

        hairRepository.deleteAll();
        hairCategoryRepository.deleteAll();

        List<HairCategory> hairsCategory = new ArrayList<>();

        hairsCategory.add(HairCategory.builder().price(BigDecimal.valueOf(30)).type(MasterCategory.START).build());
        hairsCategory.add(HairCategory.builder().price(BigDecimal.valueOf(40)).type(MasterCategory.MIDDLE).build());
        hairsCategory.add(HairCategory.builder().price(BigDecimal.valueOf(50)).type(MasterCategory.MIDDLE_PLUS).build());
        hairsCategory.add(HairCategory.builder().price(BigDecimal.valueOf(60)).type(MasterCategory.TOP).build());
        hairCategoryRepository.saveAll(hairsCategory);

        List<Hair> hairs = new ArrayList<>();
        hairs.add(Hair.builder().minLength(40).maxLength(59).price(BigDecimal.valueOf(140)).build());
        hairs.add(Hair.builder().minLength(60).maxLength(69).price(BigDecimal.valueOf(160)).build());
        hairs.add(Hair.builder().minLength(70).maxLength(79).price(BigDecimal.valueOf(180)).build());
        hairs.add(Hair.builder().minLength(80).price(BigDecimal.valueOf(200)).build());
        hairRepository.saveAll(hairs);
    }

    public List<HairCategory> getAllHairCategories() {
        return hairCategoryRepository.findAll();
    }

    public List<Hair> getAllHairs() {
        return hairRepository.findAll();
    }
}
