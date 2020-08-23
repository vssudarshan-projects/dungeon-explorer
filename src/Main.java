import com.dungeonexplorer.assets.character.PlayerAvatar;
import com.dungeonexplorer.assets.stage.*;
import com.dungeonexplorer.manager.DataBase;
import com.dungeonexplorer.middleware.GameDriver;
import com.dungeonexplorer.middleware.Player;


public class Main {

    public static void main(String[] args) {

        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Free memory: "+ beforeUsedMem);

        DataBase db = new DataBase();

        PlayerAvatar acriloth = db.initializePlayerAvatar();
        Stage currentStage = db.initializeStage();
        Player sudarshan = new Player(1,"sudarshan", acriloth,
                currentStage, new XY(5, 0));

        GameDriver.initialize(sudarshan);

        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Free memory: "+ afterUsedMem);

        long actualMemUsed=afterUsedMem-beforeUsedMem;
       System.out.println("Used Memory: " + actualMemUsed);

    }




}