package com.wishare.finance.infrastructure.remote.vo.space;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommunityOrgV implements Serializable {

    private static final long serialVersionUID = 1L;

    private String communityId;

    private List<String> orgIds;

}
