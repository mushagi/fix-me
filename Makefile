MARKET_DIR=./market
BROKER_DIR=./broker
ROUTER_DIR=./router

define generate_mvn_jar
    mvn clean package $1
endef

all : router market broker

market :
    $(call generate_mvn_jar, $(MARKET_DIR))
router :
    $(call generate_mvn_jar, $(ROUTER_DIR))
broker : 
    $(call generate_mvn_jar, $(BROKER_DIR))


