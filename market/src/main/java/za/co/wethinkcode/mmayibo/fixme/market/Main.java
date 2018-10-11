package za.co.wethinkcode.mmayibo.fixme.market;

class Main {
    public static void main(String args[]) {
        MarketClient marketClient = new MarketClient("localhost", 5000);
        new Thread(marketClient).start();
    }
}