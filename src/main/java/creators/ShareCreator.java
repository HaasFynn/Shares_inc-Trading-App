package creators;

import entities.Share;

import java.time.LocalDateTime;
import java.util.Random;

public class ShareCreator {
    private final static Random random = new Random();

    private static final String[] nameList = new String[]{"QuantumTech Industries", "BioGen Innovations", "AeroFusion Dynamics", "NexusNano Solutions", "OmniVista Ventures", "SolarFlare Innovations", "RoboHarbor Technologies", "SynthGenix Systems", "AquaSphere Innovations", "VeloByte Dynamics", "EchoWave Technologies", "NebulaCraft Enterprises", "QuantumHarbor Holdings", "BioSynth Innovations", "AeroGlobe Dynamics", "NeuralNet Nexus", "TitanWave Ventures", "HelioCraft Innovations", "CyberGenix Systems", "AquaSynth Dynamics", "QuantumQuasar Technologies", "BioNexa Innovations", "AeroVista Ventures", "NanoGenix Dynamics", "SynthSpectra Technologies", "OmniCraft Innovations", "RoboPulse Dynamics", "AeroSynth Solutions", "QuantumNova Ventures", "BioHarbor Innovations"};

    private static final String[] shortlList = new String[]{"QTI", "BGI", "AFD", "NNS", "OVV", "SFI", "RHT", "SGS", "ASI", "VBD", "EWT", "NCE", "QHH", "BSI", "AGD", "NNN", "TWV", "HCI", "CGS", "ASD", "QQT", "BNI", "AVV", "NGD", "SST", "OCI", "RPD", "ASS", "QNV", "BHI"};

    private static final Double[] ppsList = new Double[]{45.67, 32.89, 78.12, 54.23, 65.43, 89.76, 41.09, 72.54, 60.98, 55.76, 67.34, 49.21, 80.45, 43.67, 75.32, 58.90, 69.12, 62.45};

    private static final Double[] dividendList = new Double[]{45.67, 3.21, 78.12, 2.43, 65.43, 1.98, 54.23, 2.76, 89.76, 1.54, 41.09, 2.32, 72.54, 1.89, 60.98, 2.11, 55.76, 1.76, 67.34, 2.09, 49.21, 2.45, 80.45, 1.32, 43.67, 2.67, 75.32, 1.90, 58.90, 2.12, 69.12, 1.54, 62.45, 2.32};

    private static final Integer[] existingShares = new Integer[]{1500, 321, 800, 243, 1200, 198, 1000, 276, 500, 154, 1500, 232, 700, 189, 900, 211, 1100, 176, 1000, 209, 1300, 245, 600, 132, 1400, 267, 800, 190, 1000, 212, 1200, 154, 900, 232};


    public static Share getNewShare() {
        Share share = new Share();
        int randNameIndex = randEntry(nameList);
        share.name = nameList[randNameIndex];
        share.shortl = shortlList[randNameIndex];
        share.pricePerShare = randomEntry(ppsList);
        share.stockReturn = randomEntry(dividendList);
        share.existingSharesAmount = randomEntry(existingShares);
        share.date = LocalDateTime.now();
        return share;
    }

    private static <T> T randomEntry(T[] list) {
        return list[randEntry(list)];
    }

    private static <T> int randEntry(T[] list) {
        return random.nextInt(list.length - 1);
    }
}
