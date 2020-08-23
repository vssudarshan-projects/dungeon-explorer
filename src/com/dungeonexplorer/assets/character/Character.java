package com.dungeonexplorer.assets.character;

import com.dungeonexplorer.assets.equipment.Armour;
import com.dungeonexplorer.assets.equipment.Scroll;
import com.dungeonexplorer.assets.equipment.Weapon;

import java.util.*;

public class Character {

    private final int id;
    private String name;
    private final Stats stats;
    private Weapon weapon;
    private Armour armour;
    private final Map<Integer, Scroll> scrolls;
    private boolean isVulnerable;

//    public Character() {
//        id =0;
//        stats=null;
//        scrolls=null;
//    }

    public Character(int id, String name, Stats stats, Weapon weapon, Armour armour, List<Scroll> scrolls, boolean isVulnerable) {
        this.id = id;
        this.name = name;
        this.stats = new Stats(stats);
        this.weapon = weapon;
        this.armour = armour;
        this.scrolls = scrolls == null ? new HashMap<>() : setScrolls(scrolls);
        this.isVulnerable = isVulnerable;
    }

    private Map<Integer, Scroll> setScrolls(List<Scroll> scrolls) {

        Map<Integer, Scroll> scrollMap = new HashMap<>();
        int slotNumber = 1;
        for (Scroll scroll : scrolls)
            if (slotNumber <= Constants.MAX_EQUIPPED_SCROLL)
                scrollMap.put(slotNumber++, scroll);
            else
                break;

        return scrollMap;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    protected Weapon getWeapon() {
        return weapon;
    }

    protected Armour getArmour() {
        return armour;
    }

    protected List<Scroll> getScrolls() {

        LinkedList<Scroll> scrollList = new LinkedList<>();

        for (Map.Entry<Integer, Scroll> entry : scrolls.entrySet())
            scrollList.add(entry.getValue());

        return scrollList;
    }

//    protected Map<Integer, Scroll> getScrollMap() {
//        return scrolls;
//    }

    public Stats getStats() {
        return stats;
    }

    protected List<String> getCharDetails(String description) {


        List<String> stringList = new ArrayList<>();

        if (description.equals(""))
            stringList.add(this.getName());
        else
            stringList.add(this.getName() + ": " + description);

        String string = ("\nLevel : " + this.getStats().getLevel());
        String[] stringVitals = getVitals();
        for (int i = 1; i < stringVitals.length; i++)
            string = string.concat("\n" + stringVitals[i]);

        stringList.add(string);

        stringList.add("\nStrength: " + this.getStats().getStrength() +
                "\t\tDefence: " + this.getStats().getDefence() +
                "\t\t\tKnowledge: " + this.getStats().getKnowledge());

        if (getWeapon() != null)
            stringList.add("\nWeapon: " + getWeapon().getName());

        if (getArmour() != null)
            stringList.add("\nArmour : " + getArmour().getName());

        if (getScrolls().size() != 0) {
            for (String scrollString : getSlotScrolls()) {
                stringList.add("\n" + scrollString);
            }
        }
        return stringList;
    }

    protected String[] getSlotScrolls() {

        String[] stringArray = new String[scrolls.size()];

        int index = 0;
        for (Map.Entry<Integer, Scroll> entry : scrolls.entrySet())
            stringArray[index++] = "Scroll Slot " + entry.getKey() + ": " + entry.getValue().getName();

        return stringArray;
    }

    protected String[] getVitals() {

        return new String[]{name,
                "Current Health: " + stats.health + "/" + stats.getMaxHealth(),
                "Current Mana: " + stats.mana + "/" + stats.getMaxMana()};
    }


    public boolean isVulnerable() {
        return isVulnerable;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    protected void setArmour(Armour armour) {
        this.armour = armour;
    }


    protected void addScrolls(Scroll scroll) {
        int slotNumber;
        boolean okay = false;

        for (slotNumber = 1; !okay; slotNumber++) {
            okay = true;
            for (Integer number : scrolls.keySet())
                if (slotNumber == number) {
                    okay = false;
                    break;
                }
        }
        scrolls.put(slotNumber - 1, scroll);
    }

    protected void removeScroll(int slotNumber) {
        scrolls.remove(slotNumber);
    }

    protected void removeScrolls() {
        scrolls.clear();
    }

    protected void removeTopScroll() {
        Iterator<Map.Entry<Integer, Scroll>> iterator = scrolls.entrySet().iterator();
        iterator.next();
        for (Map.Entry<Integer, Scroll> entry : scrolls.entrySet())
            if (iterator.hasNext())
                scrolls.replace(entry.getKey(), iterator.next().getValue());

        scrolls.remove(scrolls.size()); //key starts from 1, which is same as slot number
    }

    protected void setVulnerable(boolean vulnerable) {
        isVulnerable = vulnerable;
    }


    protected void useWeapon(Character target) {
        calculateDamage(target, com.dungeonexplorer.assets.equipment.Constants.STRENGTH_MODIFIER,
                com.dungeonexplorer.assets.equipment.Constants.DEFENCE_MODIFIER);
    }

    protected void useScroll(int slotNumber, Character target) {
        Scroll scroll = this.scrolls.get(slotNumber);

        if (scroll == null)
            return;

        switch (scroll.getModifierType()) {

            case com.dungeonexplorer.assets.equipment.Constants.HEALTH_MODIFIER:

                this.stats.health += scroll.getBaseModifier();
                if (this.stats.health > this.stats.getMaxHealth())
                    this.stats.health = this.stats.getMaxHealth();
                break;

            case com.dungeonexplorer.assets.equipment.Constants.MANA_MODIFIER:

                this.stats.mana += scroll.getBaseModifier();
                if (this.stats.mana > this.stats.getMaxMana())
                    this.stats.mana = this.stats.getMaxMana();
                break;

            case com.dungeonexplorer.assets.equipment.Constants.KNOWLEDGE_MODIFIER:
                calculateDamage(target, com.dungeonexplorer.assets.equipment.Constants.KNOWLEDGE_MODIFIER,
                        com.dungeonexplorer.assets.equipment.Constants.KNOWLEDGE_MODIFIER);

        }
        this.scrolls.remove(slotNumber);

    }


    private void calculateDamage(Character target, int attackModifierType, int defenceModifierType) {
        double damage = 0, modifier = 1.0, targetDefence = 0, targetDefenceModifier = 1.0, healthPoints;

        if (attackModifierType == com.dungeonexplorer.assets.equipment.Constants.STRENGTH_MODIFIER)
            damage = this.stats.getStrength();
        else if (attackModifierType == com.dungeonexplorer.assets.equipment.Constants.KNOWLEDGE_MODIFIER)
            damage = this.stats.getKnowledge();

        if (this.weapon != null && this.weapon.getModifierType() == attackModifierType)
            modifier = modifier * this.weapon.getBaseModifier();

        if (this.armour != null && this.armour.getModifierType() == attackModifierType)
            modifier = modifier * this.armour.getBaseModifier();

        damage = damage * modifier;

        if (defenceModifierType == com.dungeonexplorer.assets.equipment.Constants.DEFENCE_MODIFIER)
            targetDefence = target.stats.getDefence();
        else if (defenceModifierType == com.dungeonexplorer.assets.equipment.Constants.KNOWLEDGE_MODIFIER)
            targetDefence = target.stats.getKnowledge();


        if (target.getArmour() != null && target.getArmour().getModifierType() == defenceModifierType)
            targetDefenceModifier = targetDefenceModifier * target.armour.getBaseModifier();

        targetDefence = targetDefence * targetDefenceModifier;

        healthPoints = targetDefence - (damage * randomize(Constants.DAMAGE_RANDOMIZE));
        healthPoints = healthPoints >= 0 ? 0 : healthPoints;
        target.stats.health = (int) (target.stats.health + healthPoints);

        if (target.stats.health < 0)
            target.stats.health = 0;
    }

    //delayed effect
//multithreading
    public void revive() {
        getStats().health = 100;

    }

    protected int randomize(int limit) {
        while (true) {
            int value = (int) (Math.random() * limit);
            if (value != 0)
                return value;
        }
    }

}
