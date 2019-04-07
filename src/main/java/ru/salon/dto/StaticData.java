package ru.salon.dto;

import lombok.Builder;
import lombok.Data;
import org.json.simple.JSONArray;

@Data
@Builder
public class StaticData {
    private JSONArray columns;
    private JSONArray data;
}
