import kaptainwutax.biomeutils.source.NetherBiomeSource;
import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.featureutils.structure.*;
import kaptainwutax.featureutils.structure.generator.structure.RuinedPortalGenerator;
import kaptainwutax.featureutils.structure.generator.structure.VillageGenerator;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.mcutils.state.Dimension;
import kaptainwutax.mcutils.util.pos.CPos;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.terrainutils.terrain.OverworldTerrainGenerator;

import java.util.Random;

public class Main {
    public class RandomChecker {
        public static boolean isRandom(long worldSeed) {
            long a = worldSeed;
            long b = 18218081, c = 1L << 48, d = 7847617,
                    e = ((((d * ((24667315 * (a >>> 32) + b * (int) a + 67552711) >> 32)
                            - b * ((-4824621 * (a >>> 32) + d * (int) a + d) >> 32)) - 11) * 0xdfe05bcb1365L) % c);
            return ((((0x5deece66dl * e + 11) % c) >> 16) << 32)
                    + (int) (((0xbb20b4600a69L * e + 0x40942de6baL) % c) >> 16) == a;
        }
    }

    public static void main(String[] Args){
        ChunkRand rand = new ChunkRand();
        MCVersion mc = MCVersion.v1_16_1;
        Village village = new Village(mc);
        RuinedPortal ruinedPortal = new RuinedPortal(Dimension.OVERWORLD, mc);
        PillagerOutpost pillagerOutpost = new PillagerOutpost(mc);
        BastionRemnant bastionRemnant1 = new BastionRemnant(mc);
        BastionRemnant bastionRemnant2 = new BastionRemnant(mc);
        Fortress fortress = new Fortress(mc);
        RuinedPortalGenerator ruinedPortalGenerator = new RuinedPortalGenerator(mc);
        long strSeed;
        boolean loop = true;
        long worldSeed = 0;
        while(loop){

            strSeed = new Random().nextLong() + 1;

            CPos villageCoords = (village.getInRegion(strSeed,0,0,rand)==null) ? (new CPos(1000,1000)) : (village.getInRegion(strSeed,0,0,rand));
            CPos ruinedPortalCoords = (ruinedPortal.getInRegion(strSeed,0,0,rand)==null) ? (new CPos(1000,1000)) : (ruinedPortal.getInRegion(strSeed,0,0,rand));
            CPos bastionRemnant1Coords = (bastionRemnant1.getInRegion(strSeed,0,0,rand)==null) ? (new CPos(1000,1000)) : (bastionRemnant1.getInRegion(strSeed,0,0,rand));
            CPos bastionRemnant2Coords = (bastionRemnant2.getInRegion(strSeed,-1,-1,rand)==null) ? (new CPos(1000,1000)) : (bastionRemnant2.getInRegion(strSeed,-1,-1,rand));
            CPos fortressCoords = (fortress.getInRegion(strSeed,-1,0,rand)==null) ? (new CPos(1000,1000)) : (fortress.getInRegion(strSeed,-1,0,rand));
            CPos fortressCoords2 = (fortress.getInRegion(strSeed,0,-1,rand)==null) ? (new CPos(1000,1000)) : (fortress.getInRegion(strSeed,0,-1,rand));

            if(villageCoords.getX() > 5 || villageCoords.getZ() >5)continue;
            if(ruinedPortalCoords.getX() > 5 || ruinedPortalCoords.getZ() >5)continue;
            if(bastionRemnant1Coords.getX() > 8 || bastionRemnant1Coords.getZ() >8)continue;
            if(bastionRemnant2Coords.getX() < -8 || bastionRemnant2Coords.getZ() <-8)continue;
            if((fortressCoords.getX() < -8 || fortressCoords.getZ() >8) || (fortressCoords.getX() > 8 || fortressCoords.getZ() < -8))continue;

            CPos fortressQuadran;

            if((fortressCoords.getX() < -100 || fortressCoords.getZ() >100)) fortressQuadran = fortressCoords;
            else fortressQuadran = fortressCoords2;
            long upper16;
            System.out.println("Found Structure Seed!");
            for(upper16 = 0; upper16 != 0x10000 ; upper16++){

                worldSeed = (strSeed | upper16 << 48);

                OverworldBiomeSource obs = new OverworldBiomeSource(mc,worldSeed);
                NetherBiomeSource nbs = new NetherBiomeSource(mc,worldSeed);

                if(!(village.canSpawn(villageCoords,obs))) continue;
                if(!(ruinedPortal.canSpawn(ruinedPortalCoords,obs))) continue;
                if(!(bastionRemnant1.canSpawn(bastionRemnant1Coords,nbs))) continue;
                if(!(bastionRemnant2.canSpawn(bastionRemnant2Coords,nbs))) continue;
                if(!(fortress.canSpawn(fortressQuadran,nbs))) continue;

                boolean random = RandomChecker.isRandom(worldSeed);
                if(random) continue;

                loop = false;
                break;
            }
        }
        System.out.println(worldSeed);
    }
}
