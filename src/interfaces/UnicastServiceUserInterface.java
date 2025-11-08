package interfaces;

public interface UnicastServiceUserInterface {
    /**
     * Primitiva utilizada para notificar a chegada de uma mensagem.
     * @param source Entidade origem (ucsap_id).
     * @param message Mensagem recebida.
     */
    void UPDataInd(short sendingEntity, String message); // UPDataInd(short, String): void
}