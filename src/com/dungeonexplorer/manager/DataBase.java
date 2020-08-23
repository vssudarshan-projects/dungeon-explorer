package com.dungeonexplorer.manager;

import com.dungeonexplorer.assets.GameObject;
import com.dungeonexplorer.assets.character.Monster;
import com.dungeonexplorer.assets.character.NonPlayableCharacter;
import com.dungeonexplorer.assets.character.PlayerAvatar;
import com.dungeonexplorer.assets.character.Stats;
import com.dungeonexplorer.assets.equipment.Armour;
import com.dungeonexplorer.assets.equipment.Scroll;
import com.dungeonexplorer.assets.equipment.Weapon;
import com.dungeonexplorer.assets.stage.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataBase {

    static Map<Integer, Monster> monsters;
    static Map<Integer, Weapon> weapons;
    static Map<Integer, Armour> armours;
    static Map<Integer, Scroll> scrolls;
    static Map<Integer, GameObject> treasures;
    //  static PlayerAvatar playerAvatar;

    public DataBase() {
        weapons = new HashMap<>();
        armours = new HashMap<>();
        monsters = new HashMap<>();
        treasures = new HashMap<>();
        scrolls = new HashMap<>();
        initializeWeapons();
        initializeArmours();
        initializeScrolls();
        //  initializeMonsters();

    }

    private ArrayList<Integer> initializeMonsters() {

        Monster goblin = new Monster(1, 1, "Goblin",
                "a cute or ugly green creature, your take.",
                new Stats(100,100,1, 0, 1,1), weapons.get(2),
                null, null, true);

        Monster goblinWarrior = new Monster(2, 2, "Goblin Warrior",
                "A goblin warrior hardened by battle.",
                new Stats(100,100,3, 3, 3,1), weapons.get(1)
                , armours.get(1), null, true);


        monsters.put(goblin.getId(), goblin);
        monsters.put(goblinWarrior.getId(), goblinWarrior);

        return new ArrayList<>(monsters.keySet());
    }

    private void initializeWeapons() {

        Weapon rustedSword = new Weapon(3, 1, "Rusted Sword", "A rusted sword, it can still hurt you.", com.dungeonexplorer.assets.equipment.Constants.STRENGTH_MODIFIER, 1.15);
        Weapon claw = new Weapon(4, 2, "Claw", "The natural enemy of beautiful skin.", com.dungeonexplorer.assets.equipment.Constants.STRENGTH_MODIFIER, 1);

        weapons.put(rustedSword.getId(), rustedSword);
        weapons.put(claw.getId(), claw);

    }

    private void initializeArmours() {

        Armour rustedChainMail = new Armour(5, 1, "Rusted Chain Mail", "It's old and smells but get the job done, for now.", com.dungeonexplorer.assets.equipment.Constants.DEFENCE_MODIFIER, 1.1);
        armours.put(rustedChainMail.getId(), rustedChainMail);
    }

    private void initializeScrolls() {

        Scroll lesserHPScroll = new Scroll(6, 1, "Scroll: Lesser Healing", "The blessing of the goddess grants you 20 hp", com.dungeonexplorer.assets.equipment.Constants.HEALTH_MODIFIER, 20);
        Scroll waterBulletScroll = new Scroll(7, 2, "Scroll: Water Bullet", "A spell that does water damage", com.dungeonexplorer.assets.equipment.Constants.KNOWLEDGE_MODIFIER, 10);

        scrolls.put(lesserHPScroll.getId(), lesserHPScroll);
        scrolls.put(waterBulletScroll.getId(), waterBulletScroll);

    }


    public PlayerAvatar initializePlayerAvatar() {
        return new PlayerAvatar(1, "Acriloth", new Stats(100,100,5, 5, 5,1), weapons.get(1), armours.get(1), null,null, true);

    }

    public Stage initializeStage() {

        /*Initializing Training Grounds*/

        NonPlayableCharacter sergeantSanders = new NonPlayableCharacter(7, 1, "Sergeant Sanders", "A scary drill sergent, better not look him in the eye.", new Stats(100,100,0, 0, 0,1), null, null, null, false);
        NonPlayableCharacter trainingDummy = new NonPlayableCharacter(8, 2, "Training Dummy Bill", "Bill can take you.", new Stats(100,100,0, 0, 10,1), null, null, null, true);

        Map<String, ZoneObject> zoneObjects = new HashMap<>();

        ZoneObject sergeantSandersLA = new ZoneObject("Sergeant Sanders",sergeantSanders, false, true);
        ZoneObject trainingDummyLA = new ZoneObject("Training Dummy", trainingDummy, false, true);

        zoneObjects.put("Sergeant Sanders", sergeantSandersLA);
        zoneObjects.put("Training Dummy", trainingDummyLA);
        ZonePortal startPortal = new ZonePortal(0, 0, new XY(0, 0), null);
        ZonePortal toGrassyPlains = new ZonePortal(0, 1, new XY(5, 10), null);
        ZoneProperty trainingGroundProperty = new ZoneProperty(Constants.SAFE_ZONE, new XY(3, 3));
        Zone trainingGround = new Zone(0, "Training Ground", "Training grounds of the Dungeon Survey Unit.", new XY(5, 0), zoneObjects, trainingGroundProperty);


        /*Initializing GRASSY PLAINS*/

        //monster list
        ArrayList<Integer> monsterList = initializeMonsters();

        //treasure list
        ArrayList<Integer> treasureList = initializeTreasures();

        ZonePortal toTrainingGrounds = new ZonePortal(3, 1, new XY(0, 0), null);
        ZoneProperty grassyPlainProperty = new ZoneProperty(Constants.PVP_ZONE, new XY(3, 3));
        Zone grassyPlain = new Zone(3, "Grassy Plains", "The place is covered in tall grasses where monsters could hide.", new XY(5, 5), null ,grassyPlainProperty);

        grassyPlain.setObjectOptions(monsterList, treasureList, 5, 5, 10, 1);


        /*Adding Portals*/

        toTrainingGrounds.setDestZonePortal(toGrassyPlains);
        toGrassyPlains.setDestZonePortal(toTrainingGrounds);

        trainingGroundProperty.addZonePortal(startPortal);
        trainingGroundProperty.addZonePortal(toGrassyPlains);
        grassyPlainProperty.addZonePortal(toTrainingGrounds);

        /*Map Initialized*/

        Map<Integer, Zone> stageMap = new HashMap<>();
        stageMap.put(trainingGround.getZoneId(), trainingGround);
        stageMap.put(grassyPlain.getZoneId(), grassyPlain);


        return new Stage(1, "Training Grounds", new XY(5, 5), stageMap);

    }

    private ArrayList<Integer> initializeTreasures() {

        ArrayList<Integer> treasureList = new ArrayList<>();

        for (Map.Entry<Integer, Scroll> entry : scrolls.entrySet()) {
            treasureList.add(entry.getValue().getGameObjectId());
            treasures.put(entry.getValue().getGameObjectId(), entry.getValue());
        }
        for (Map.Entry<Integer, Armour> entry : armours.entrySet()) {
            treasureList.add(entry.getValue().getGameObjectId());
            treasures.put(entry.getValue().getGameObjectId(), entry.getValue());
        }
        for (Map.Entry<Integer, Weapon> entry : weapons.entrySet()) {
            treasureList.add(entry.getValue().getGameObjectId());
            treasures.put(entry.getValue().getGameObjectId(), entry.getValue());
        }

        return treasureList;

    }


}
