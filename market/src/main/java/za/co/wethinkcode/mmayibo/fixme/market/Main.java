package za.co.wethinkcode.mmayibo.fixme.market;

class Main {
    public static void main(String args[]) {
        Market market = new Market("localhost", 5030);
        new Thread(market).start();
    }
}