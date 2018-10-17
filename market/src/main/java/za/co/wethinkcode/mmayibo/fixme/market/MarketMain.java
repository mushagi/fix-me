package za.co.wethinkcode.mmayibo.fixme.market;

class MarketMain {
    public static void main(String args[]){
        String marketUserName = getMarketIdFromArguments(args);

        MarketClient marketClient = new MarketClient("localhost", 5000, marketUserName);

        new Thread(marketClient).start();
    }

    private static String getMarketIdFromArguments(String[] args) {
        if  (args.length != 1){
            System.out.println("Usage:\n\t\t\t./market [Username]");
            System.exit(0);
        }
        return args[0];
    }
}