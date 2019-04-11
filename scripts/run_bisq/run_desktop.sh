#!/usr/bin/env bash

PUBLIC_IPV4=$(curl -s http://169.254.169.254/metadata/v1/interfaces/public/0/ipv4/address)

cd ~/bisq
./bisq-desktop --appName=bisq-BTC_REGTEST_desktop --baseCurrencyNetwork=BTC_REGTEST --useLocalhostForP2P=true --nodePort=7777 --myAddress=$PUBLIC_IPV4:7777 --useDevPrivilegeKeys=true --daoActivated=true --genesisBlockHeight=111 --genesisTxId=30af0050040befd8af25068cc697e418e09c2d8ebd8d411d2240591b9ec203cf --seedNodes=157.230.223.247:2002 --bitcoinRegtestHost=157.230.223.247 >/dev/null 2>bisq-BTC_REGTEST_desktop.log
