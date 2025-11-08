package entrega1;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java Main <UCSAP_ID>");
            System.exit(1);
        }
        
        try {
            short ucsapId = Short.parseShort(args[0]);
            String configFile = "config.txt";
            
            RoutingInformationProtocol app = new RoutingInformationProtocol(ucsapId, configFile);
            app.start();

        } catch (NumberFormatException e) {
            System.err.println("Erro: id e porta devem ser numeros validos.");
        }
    }
}
