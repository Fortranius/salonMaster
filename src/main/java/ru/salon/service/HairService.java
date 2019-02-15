package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.salon.model.Hair;
import ru.salon.model.HairCategory;
import ru.salon.model.MasterCategory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HairService {

    public List<HairCategory> getAllHairCategories() {
        List<HairCategory> hairs = new ArrayList<>();
        hairs.add(HairCategory.builder().price(BigDecimal.valueOf(30)).type(MasterCategory.START).build());
        hairs.add(HairCategory.builder().price(BigDecimal.valueOf(40)).type(MasterCategory.MIDDLE).build());
        hairs.add(HairCategory.builder().price(BigDecimal.valueOf(50)).type(MasterCategory.MIDDLE_PLUS).build());
        hairs.add(HairCategory.builder().price(BigDecimal.valueOf(60)).type(MasterCategory.TOP).build());
        return hairs;
    }

    public List<Hair> getAllHairs() {
        List<Hair> hairs = new ArrayList<>();
        hairs.add(Hair.builder().minLength(40).maxLength(59).price(BigDecimal.valueOf(140)).build());
        hairs.add(Hair.builder().minLength(60).maxLength(69).price(BigDecimal.valueOf(160)).build());
        hairs.add(Hair.builder().minLength(70).maxLength(79).price(BigDecimal.valueOf(180)).build());
        hairs.add(Hair.builder().minLength(80).price(BigDecimal.valueOf(200)).build());
        return hairs;
    }
}
