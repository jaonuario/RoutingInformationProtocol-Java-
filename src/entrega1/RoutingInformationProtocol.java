package entrega1;

import interfaces.*;
import up.UnicastProtocol;

import java.util.Scanner;

/**
 * Classe para testar a primeira entrega
 */
public class RoutingInformationProtocol implements UnicastServiceUserInterface {

    private short localUcsapId;
    private UnicastProtocol protocol;

    /**
     * Construtor padrao
     * @param localUcsapId Identificador UCSAP local.
     * @param configFile Caminho para o arquivo de configuracao.
     */
    public RoutingInformationProtocol(short localUcsapId, String configFile) {
        this.localUcsapId = localUcsapId;
        this.protocol = new UnicastProtocol(localUcsapId, configFile, this);
    }

    /**
     * Implementacao da primitiva UPDataInd para receber a mensagem.
     * @param source Entidade origem (ucsap_id).
     * @param message Mensagem recebida.
     */
    @Override
    public void UPDataInd(short sendingEntity, String message) {
        System.out.println("\n--- MENSAGEM RECEBIDA ---");
        System.out.println("De: UCSAP " + sendingEntity); // entidade origem
        System.out.println("Mensagem: " + message); // mensagem
        System.out.print("\nCMD : ");
    }
    
    /**
     * Inicia o protocolo e o loop de interação com o usuário.
     */
    public void start() {
        protocol.init();

        Thread protocolThread = new Thread(protocol);
        protocolThread.start();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- APLICACAO DE TESTE (UCSAP " + localUcsapId + ") ---");
        System.out.println("CMD: send <destino_ucsap> <mensagem>");
        System.out.println("CMD: exit");

        while (true) {
            System.out.print("\nCMD: ");
            String input = scanner.nextLine();
            
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] parts = input.trim().split(" ", 3);
            if (parts.length == 3 && parts[0].equalsIgnoreCase("send")) {
                try {
                    short destination = Short.parseShort(parts[1]);
                    String message = parts[2];
                    
                    protocol.UPDataReq(destination, message);

                } catch (NumberFormatException e) {
                    System.err.println("ERRO: O ID do destino deve ser um numero.");
                }
            } else {
                System.out.println("Comando invalido. Use 'send <destino_ucsap> <mensagem>' ou 'exit'.");
            }
        }
        
        protocol.stop();
        try {
            protocolThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        scanner.close();
        System.out.println("Aplicação encerrada.");
    }
}