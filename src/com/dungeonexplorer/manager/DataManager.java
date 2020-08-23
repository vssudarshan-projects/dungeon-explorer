package com.dungeonexplorer.manager;

import com.dungeonexplorer.assets.GameObject;
import com.dungeonexplorer.assets.character.Monster;

import java.util.Map;

public class DataManager {


   Map<Integer, Monster> getMonsterList(){
          return DataBase.monsters;
    }

   Map<Integer, GameObject> getTreasureList() {
       return DataBase.treasures;
   }
}
