package com.cms.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BranchMiniResponse {

    private UUID id;

    private String name;

    private String code;

    private DepartmentMiniResponse department;
}