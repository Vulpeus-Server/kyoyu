package com.vulpeus.kyoyu.client;

import com.vulpeus.kyoyu.placement.KyoyuPlacement;

import java.util.UUID;

public interface ISchematicPlacement {

    void kyoyu$setKyoyuId(UUID uuid);

    UUID kyoyu$getKyoyuId();

    KyoyuPlacement kyoyu$toKyoyuPlacement();

    void kyoyu$updateFromKyoyuPlacement(KyoyuPlacement kyoyuPlacement);

}
