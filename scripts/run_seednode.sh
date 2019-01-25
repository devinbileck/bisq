#!/usr/bin/env bash

PUBLIC_IPV4=$(curl -s http://169.254.169.254/metadata/v1/interfaces/public/0/ipv4/address)

cd ~/
~/bisq/bisq-seednode --appName=bisq-BTC_REGTEST_Seed_2002 --baseCurrencyNetwork=BTC_REGTEST --useLocalhostForP2P=true --nodePort=2002 --myAddress=$PUBLIC_IPV4:2002 --useDevPrivilegeKeys=true --daoActivated=true --genesisBlockHeight=111 --genesisTxId=30af0050040befd8af25068cc697e418e09c2d8ebd8d411d2240591b9ec203cf --fullDaoNode=true --rpcUser=bitcoin --rpcPassword=$RPC_PASSWORD --rpcPort=18443 --rpcBlockNotificationPort=5120 >/dev/null 2>bisq-BTC_REGTEST_Seed_2002.log
