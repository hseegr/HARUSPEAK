package com.haruspeak.batch.dto.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MomentTagContext {
        private int userId;
        private String createdAt;
        private Set<String> tags;
}
