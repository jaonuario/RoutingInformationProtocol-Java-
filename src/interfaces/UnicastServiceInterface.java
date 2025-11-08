package interfaces;

public interface UnicastServiceInterface {
    /**
     * Primitiva utilizada para o envio de uma mensagem para um usuário do serviço de unicast.
     * @param destination Entidade destino (ucsap_id).
     * @param message Mensagem a ser enviada.
     */
    void UPDataReq(short destinationEntity, String message); // UPDataReq(short, String): boolean
}