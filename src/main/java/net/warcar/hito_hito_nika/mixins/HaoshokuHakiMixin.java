package net.warcar.hito_hito_nika.mixins;

import org.spongepowered.asm.mixin.Mixin;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IHakiAbility;
import xyz.pixelatedw.mineminenomi.api.enums.HakiType;

@Mixin(HaoshokuHakiAbility.class)
public abstract class HaoshokuHakiMixin implements IHakiAbility {
    @Override
    public HakiType getType() {
        return HakiType.HAOSHOKU;
    }
}
