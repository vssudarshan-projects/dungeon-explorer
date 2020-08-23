package com.dungeonexplorer.assets.character;

import com.dungeonexplorer.assets.GameObject;
import com.dungeonexplorer.assets.equipment.Armour;
import com.dungeonexplorer.assets.equipment.Scroll;
import com.dungeonexplorer.assets.equipment.Weapon;

import java.util.List;

public class Monster extends Character implements GameObject {

    private final String description;
    private final int gameObjectId;

    public Monster(int gameObjectId, int monsterId, String name, String description, Stats stats, Weapon weapon, Armour armour, List<Scroll> scrolls, boolean isVulnerable) {
        super(monsterId, name, stats, weapon, armour, scrolls, isVulnerable);
        this.description = description;
        this.gameObjectId = gameObjectId;
    }

    public Monster(Monster monster) {
        super(monster.getId(), monster.getName(), monster.getStats(), monster.getWeapon(), monster.getArmour(), monster.getScrolls(), monster.isVulnerable());
        this.description = monster.description;
        this.gameObjectId = monster.gameObjectId;
    }

    public List<String> getCharDetails() {
        return super.getCharDetails(this.description);
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getGameObjectId() {
        return this.gameObjectId;
    }

    public String[] getCombatStatus() {
        return super.getVitals();
    }

    public void attack(Character character) {


        if (character instanceof PlayerAvatar) {
            PlayerAvatar target = (PlayerAvatar) character;

            int monsterAction = randomize(Constants.MAX_MONSTER_COMBAT_ACTIONS);

            if (monsterAction == Constants.WEAPON_ATTACK) {
                useWeapon(target);

            } else if (monsterAction == Constants.SCROLL_USE) {

                int slotNumber = randomize(Constants.MAX_EQUIPPED_SCROLL);
                if (this.getScrolls().size() > 0 && slotNumber < this.getScrolls().size())
                    useScroll(slotNumber, target);
            }
        }
    }


}
