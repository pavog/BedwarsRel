package io.github.bedwarsrel.shop.Specials;

import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.utils.Utils;
import io.github.bedwarsrel.utils.XMaterial;
import org.bukkit.Material;

public class MagnetShoe extends SpecialItem {

  @Override
  public Material getActivatedMaterial() {
    return null;
  }

  @SuppressWarnings("deprecation")
  @Override
  public Material getItemMaterial() {
    String item = BedwarsRel.getInstance()
            .getStringConfig("specials.magnetshoe.boots", "IRON_BOOTS");
    Material material = null;
    if (Utils.isNumber(item)) {
      XMaterial xMaterial = XMaterial.fromId(Integer.valueOf(item));
      material = xMaterial != null ? xMaterial.parseMaterial() : XMaterial.IRON_BOOTS.parseMaterial();
    } else {
      XMaterial xMaterial = XMaterial.fromString(item);
      material = xMaterial != null ? xMaterial.parseMaterial() : XMaterial.IRON_BOOTS.parseMaterial();
    }

    if (material == null) {
      return Material.IRON_BOOTS;
    }

    return material;
  }

}
