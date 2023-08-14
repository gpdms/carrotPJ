package com.exercise.carrotproject.domain.member.ouath;

import com.exercise.carrotproject.domain.enumList.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OauthServiceFactory {
    private final List<OauthService> oauthServiceList;
    private final Map<Role, OauthService> factoryCache;

    public OauthService find(final Role role) {
        OauthService oauthService = factoryCache.get(role);
        if(oauthService != null) {
            return oauthService;
        }

        oauthService = oauthServiceList.stream()
                .filter(s -> s.supports(role))
                .findFirst()
                .orElseThrow();
        factoryCache.put(role, oauthService);

        return oauthService;
    }
}
