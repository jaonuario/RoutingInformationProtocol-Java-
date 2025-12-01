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

    public boolean recalculate() {
        int[] oldVector = Arrays.copyOf(localVector, localVector.length);

        int[] newVector = new int[localVector.length];
        Arrays.fill(newVector, INF);
        newVector[allNodeIds.indexOf(localNodeId)] = 0;

        for (int yIndex = 0; yIndex < allNodeIds.size(); yIndex++) {
            short destinationY = allNodeIds.get(yIndex);
            int minCost = INF;
            
            if (destinationY == localNodeId) continue; 

            for (short neighborV : linkCosts.keySet()) {
                int costXV = linkCosts.get(neighborV);

                if (costXV == INF) continue; 

                int[] vectorV = neighborVectors.getOrDefault(neighborV, null);
                if (vectorV == null) continue; 

                int costVY = vectorV[yIndex]; 

                // Se o vizinho V não tem rota para Y, ou se a rota através de V é infinita, ignora
                if (costVY == INF) continue;

                int totalCost = costXV + costVY; 

                if (minCost == INF || totalCost < minCost) {
                    minCost = totalCost;
                }
            }
            
            newVector[yIndex] = minCost;
        }

        this.localVector = newVector;
        
        return !Arrays.equals(oldVector, newVector);
    }
}

