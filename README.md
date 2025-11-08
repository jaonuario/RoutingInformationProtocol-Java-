# Routing Information Protocol (Em desenvolvimento)

## Primeira Entrega: Protocolo Unicast Não Confiável

Esta entrega implementa a camada base da pilha de protocolos, focando no **Protocolo de Transferência Unicast Não Confiável** (`UnicastProtocol`), conforme a especificação do projeto (documento ocultado). O protocolo utiliza o serviço de transporte **UDP** para comunicação.

O objetivo é fornecer e testar o serviço de unicast, que permite o envio e recebimento de mensagens (strings) entre entidades.

---

### 1. Resumo e Componentes

| Classe/Interface | Função no Projeto | Detalhe |
| :--- | :--- | :--- |
| **`UnicastProtocol`** | Protocolo de camada inferior | Implementa `UnicastServiceInterface` para enviar dados (`UPDataReq`) e faz uso de sockets UDP. |
| **`UnicastServiceInterface`** | Interface de serviço oferecido | Primitiva de requisição: `UPDataReq(short destination, String message)`. |
| **`UnicastServiceUserInterface`** | Interface de notificação | Primitiva de indicação: `UPDataInd(short source, String message)`. |
| **`UnicastTestApplication`** | Aplicação de teste | Simula a camada superior (usuário do serviço) e permite testar o envio/recebimento de strings. |
| **`config.txt`** | Arquivo de Endereçamento | Contém o mapeamento `<ucsap_id><espaço><host_name><port_number>` para todas as entidades de protocolo unicast. |

---

### 2. Detalhes Chave da Implementação

#### 2.1. PDU de Requisição de Dados (`UPDREQPDU`)

* **Tamanho Máximo:** 1024 bytes.
* **Codificação Concreta:** `<UPDREQPDU><espaço><tamanho_dados><espaço><dados>`[cite: 98].

#### 2.2. Inicialização e Endereçamento

* Cada entidade deve ser inicializada com um **UCSAP ID** ($\ge 0$) e um **número de porta** ($> 1024$).
* Na inicialização, a entidade deve **ler e interpretar o arquivo de configuração** para encontrar suas próprias informações e as informações de todos os outros nós.
* **Condição de Abortagem:** Se a entidade não conseguir encontrar suas próprias informações no arquivo de configuração, deve abortar.

---

### 3. Como Executar (Teste)

O teste requer a criação do arquivo `config.txt` e a execução da aplicação de teste em terminais separados para simular diferentes nós.

#### 3.1. Arquivo `config.txt` (Exemplo)

```text
1 localhost 1100
2 localhost 1150
```
#### 3.2. Comandos de Execução

Windows:
```bash
.\entrega1.bat
```

Linux (ainda não foi criada uma automação): 
```bash
javac -sourcepath %SRC_DIR% -d %BIN_DIR% %SRC_DIR%\entrega1\Main.java
jar cvfm %JAR_NAME% %MANIFEST_FILE% -C %BIN_DIR% 
```

Execute o Nó 1 (Terminal 1):
```bash
java -jar .\Entrega1.jar 1
```

Execute o Nó 2 (Terminal 1):
```bash
java -jar .\Entrega1.jar 2
```

Comando de Teste: No Terminal 1, envie uma mensagem para o Nó 2 (UCSAP 2):
```bash
Comando (send <destino> <msg> / exit): send 2 Teste de mensagem para o No 2!
```
A mensagem deve ser exibida no Terminal 2 via UPDataInd.
