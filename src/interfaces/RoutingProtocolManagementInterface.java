package interfaces;

// <<interface>> RoutingProtocolManagementInterface
public interface RoutingProtocolManagementInterface {

    /**
     * Primitiva utilizada pela aplicação para requisitar a tabela de distância de um nó.
     * Corresponde a Get Distance Table (5.1.1).
     * @param node Identificador do nó cuja tabela é requisitada (1 a 15).
     * @return True se identificador for válido; false caso contrário[cite: 159].
     */
    boolean getDistanceTable(short node);

    /**
     * Primitiva utilizada pela aplicação para requisitar o custo do enlace conectando dois nós (A/B).
     * Corresponde a Get Link Cost (5.1.2).
     * @param nodeA Identificador do nó A[cite: 163].
     * @param nodeB Identificador do nó B[cite: 164].
     * @return True se os dois identificadores de nós forem válidos e houver um enlace entre eles; false caso contrário[cite: 166, 167].
     */
    boolean getLinkCost(short nodeA, short nodeB);

    /**
     * Primitiva utilizada pela aplicação para redefinir o custo do enlace conectando dois nós (A/B).
     * Corresponde a Set Link Cost (5.1.3).
     * @param nodeA Identificador do nó A[cite: 172].
     * @param nodeB Identificador do nó B[cite: 173].
     * @param cost Novo valor do enlace (inteiro entre 1 e 15, ou -1 para infinito)[cite: 174, 259].
     * @return True se os IDs e o novo valor forem válidos, e houver um enlace entre A e B; false caso contrário[cite: 176, 177].
     */
    boolean setLinkCost(short nodeA, short nodeB, int cost);
}
