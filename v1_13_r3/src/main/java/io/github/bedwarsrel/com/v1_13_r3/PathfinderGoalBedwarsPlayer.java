package io.github.bedwarsrel.com.v1_13_r3;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalMeleeAttack;

public class PathfinderGoalBedwarsPlayer extends PathfinderGoalMeleeAttack {

  private EntityCreature creature = null;

  public PathfinderGoalBedwarsPlayer(EntityCreature name, double name3, boolean name4) {
    super(name, name3, name4);
    this.creature = name;
  }

  @Override
  public void e() {
    this.creature.getNavigation().a(this.creature.getGoalTarget());
  }

}
