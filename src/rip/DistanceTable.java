package rip;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistanceTable {
    //Constante
    private static final int INF = -1;                                  // Custo infinito
    
    //Atributos    
    private int[] localVector;                                          // Vetor de distância do proprio no (Dx)
    private final short localNodeId;                                    // Id local
    private final List<Short> allNodeIds;                               // Lista ordenada de todos os nos na rede

    private final Map<Short, int[]> neighborVectors = new HashMap<>();  // Armazena: (UCSAP_ID do Vizinho, Vetor de Distância do Vizinho)
    private final Map<Short, Integer> linkCosts = new HashMap<>();      // O custo do enlace local: (UCSAP_ID do Vizinho, C(X, V))


    public DistanceTable(short localNodeId, List<Short> allNodeIds) {
        this.localNodeId = localNodeId;
        this.allNodeIds = allNodeIds;
        this.localVector = new int[allNodeIds.size()];
        Arrays.fill(localVector, INF);
        setDistance(localNodeId, 0);
    }

    public int getDistance(short destinationId) {
        int index = allNodeIds.indexOf(destinationId);
        return (index != -1) ? localVector[index] : INF;
    }

    private void setDistance(short destinationId, int distance) {
        int index = allNodeIds.indexOf(destinationId);
        if (index != -1) {
            localVector[index] = distance;
        }
    }
}
