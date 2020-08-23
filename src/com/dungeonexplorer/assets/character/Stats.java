package com.dungeonexplorer.assets.character;

public class Stats {
    private final int maxHealth;
    private final int maxMana;
    int health;
    int mana;
    private int strength;
    private int knowledge;
    private int defence;
    private int level;

    public Stats(int health, int mana, int strength, int knowledge, int defence, int level) {
        this.maxHealth = 100;
        this.maxMana = 100;
        this.health = health;
        this.mana = mana;
        this.strength = strength;
        this.knowledge = knowledge;
        this.defence = defence;
        this.level = level;
    }

    public Stats(Stats stats){
        this.maxHealth = 100;
        this.maxMana = 100;
        this.health = stats.health;
        this.mana = stats.mana;
        this.strength = stats.strength;
        this.knowledge = stats.knowledge;
        this.defence = stats.defence;
        this.level = stats.level;

    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public int getStrength() {
        return strength;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public int getDefence() {
        return defence;
    }

    public int getLevel() {
        return level;
    }

}



