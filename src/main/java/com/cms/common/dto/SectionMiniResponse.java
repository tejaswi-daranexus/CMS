package com.cms.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SectionMiniResponse {

    private UUID id;

    private String name;

    private BranchMiniResponse branch;
}