package com.wishare.contract.apps.fo.remind;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityIdF implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> communityIds;

}
