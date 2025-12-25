package com.wishare.finance.infrastructure.remote.vo.spacePermission;

import java.util.List;
import lombok.Data;

@Data
public class SpacePermissionV {
    private List<String> communityIds;
    private List<String> spaceIds;
}
