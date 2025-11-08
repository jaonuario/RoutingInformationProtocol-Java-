package up;

import interfaces.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class UnicastProtocol implements UnicastServiceInterface, Runnable {

    // Constantes
    private static final String PDU_TYPE = "UPDREQPDU";
    private static final int MAX_PDU_SIZE = 1024;

    // Atributos 
    private short localUcsapId;
    private int localPort;
    private String configFile;
    private HashMap<Short, UCSAPAddress> ucsapMap;
    private UnicastServiceUserInterface userInterface;
    private DatagramSocket socket;

    private boolean running = true;

    /**
     * Construtor padrao
     * @param localUcsapId Identificador UCSAP local.
     * @param configFile Caminho para o arquivo de configuracao.
     * @param userInterface A camada superior que usara este servico (UnicastServiceUserInterface).
     */
    public UnicastProtocol(short localUcsapId, String configFile, UnicastServiceUserInterface userInterface) {
        this.localUcsapId = localUcsapId;
        this.configFile = configFile;
        this.userInterface = userInterface;
        this.ucsapMap = new HashMap<>();

        if (!loadConfiguration()) {
            System.err.println("Erro: Falha na leitura ou validacao do arquivo de configuracao.");
            System.exit(1);
        }
    }

    /**
     * Inicializa o protocolo: abre o socket UDP.
     */
    public void init() {
        try {
            this.socket = new DatagramSocket(localPort);
            System.out.println("(DEBUG) UnicastProtocol (UCSAP " + localUcsapId + ") iniciado na porta " + localPort);
        } catch (SocketException e) {
            System.err.println("Erro: Nao foi possível abrir o socket UDP na porta " + localPort);
            System.exit(1);
        }
    }

    /**
     * Carrega a configuracao das entidades Unicast a partir do arquivo.
     * @return true se a configuracao for carregada com sucesso, senao false.
     */
    private boolean loadConfiguration() {
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            String line;
            boolean foundLocalEntity = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");
                if (parts.length != 3) {
                    System.err.println("Linha invalida no arquivo de configuracao: " + line);
                    return false;
                }

                short id = Short.parseShort(parts[0]); 
                String host = parts[1];
                int port = Integer.parseInt(parts[2]);
                
                if (id < 0) {
                    System.err.println("ID invalido na configuracao: " + line);
                    return false;
                }

                ucsapMap.put(id, new UCSAPAddress(id, host, port));
                
                if (id == localUcsapId) {
                    this.localPort = port;
                    foundLocalEntity = true;
                }
            }
            
            if (!foundLocalEntity) {
                System.err.println("Entidade (UCSAP " + localUcsapId + ") nao encontrada no arquivo.");
                return false;
            }

            return true;

        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao processar o arquivo de configuracao: " + e.getMessage());
            return false;
        }
    }

    /**
     * Implementação da primitiva UPDataReq - envia uma mensagem unicast nao confiavel.
     * @param destination Entidade destino (ucsap_id).
     * @param message Mensagem a ser enviada.
     */
    @Override
    public void UPDataReq(short destinationEntity, String message) {
        if (!ucsapMap.containsKey(destinationEntity)) {
            System.out.println("(DEBUG) Destino UCSAP " + destinationEntity + " desconhecido.");
            return;
        }

        UCSAPAddress destAddress = ucsapMap.get(destinationEntity);
        
        int dataSize = message.getBytes().length;
        String pduContent = String.format("%s %d %s", PDU_TYPE, dataSize, message);
        
        byte[] sendData = pduContent.getBytes();

        // Nao foi definido o que fazer com mensagens grandes demais. Optou-se por descartar a mensagem.
        if (sendData.length > MAX_PDU_SIZE) {
            System.err.println("Mensagem muito longa (" + sendData.length + " bytes). Maximo permitido: " + MAX_PDU_SIZE);
            return;
        }

        try {
            InetAddress IPAddress = InetAddress.getByName(destAddress.getHostName());
            
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, destAddress.getPortNumber());
            socket.send(sendPacket);
            System.out.println("(DEBUG)[UCSAP " + localUcsapId + "] Enviou PDU para [UCSAP " + destinationEntity + "] (" + pduContent + ")");
            
        } catch (IOException e) {
            System.err.println("Erro ao enviar datagrama para UCSAP " + destinationEntity + ": " + e.getMessage());
        }
    }

    /**
     * Loop de recepcao de datagramas UDP executado em uma thread separada.
     */
    @Override
    public void run() {
        byte[] receiveBuffer = new byte[MAX_PDU_SIZE];

        while (running) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                
                processPDU(receivedData, receivePacket.getPort());
                
            } catch (SocketException e) {
                if (running) {
                    System.err.println("Erro no socket UDP: " + e.getMessage());
                }
            } catch (IOException e) {
                System.err.println("Erro de E/S na recepção: " + e.getMessage());
            }
        }
        System.out.println("(DEBUG) UnicastProtocol (UCSAP " + localUcsapId + ") encerrado.");
    }
    
    /**
     * Processa a PDU recebida e invoca a primitiva UPDataInd.
     * @param pduContent Conteudo da pdu recebida.
     * @param senderPort Porta de origem.
     */
    private void processPDU(String pduContent, int senderPort) {
        String[] parts = pduContent.split(" ", 3);
        if (parts.length < 3 || !parts[0].equals(PDU_TYPE)) {
            System.err.println("[UCSAP " + localUcsapId + "] PDU inválida recebida: " + pduContent);
            return;
        }

        try {
            int dataSize = Integer.parseInt(parts[1]);
            String message = parts[2];
            
            if (message.getBytes().length != dataSize) {
                System.err.println("[UCSAP " + localUcsapId + "] Erro no tamanho da mensagem no PDU: " + pduContent);
                return;
            }
            
            short sourceUcsap = -1;
            for (UCSAPAddress ucsap : ucsapMap.values()) {
                if (ucsap.getPortNumber() == senderPort) {
                    sourceUcsap = ucsap.getUcsapId();
                    break;
                }
            }
            
            if (sourceUcsap == -1) {
                System.err.println("[UCSAP " + localUcsapId + "] Mensagem recebida de porta desconhecida: " + senderPort);
                return;
            }

            if (userInterface != null) {
                userInterface.UPDataInd(sourceUcsap, message);
            }

        } catch (NumberFormatException e) {
            System.err.println("[UCSAP " + localUcsapId + "] Erro de formato numérico na PDU: " + pduContent);
        }
    }

    /**
     * Para o loop de recepcao
     */
    public void stop() {
        running = false;
        if (socket != null) {
            socket.close();
        }
    }
}