MARKET_DIR=./market
BROKER_DIR=./broker
ROUTER_DIR=./router

define generate_mvn_jar
    mvn clean package $1
endef

all : router market broker

market :
	mvn
	
router :
	javac 

broker : 
	javac


