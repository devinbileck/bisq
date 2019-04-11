#!/usr/bin/env bash

cd ~/
wget -O /home/bisq/dao-setup.zip https://github.com/bisq-network/bisq/blob/master/docs/dao-setup.zip?raw=true
unzip dao-setup.zip
rm dao-setup.zip
cp -r dao-setup/Bitcoin-regtest/regtest ~/.bitcoin
cp -r dao-setup/bisq-BTC_REGTEST_Alice_dao ~/.local/share/bisq
cp -r dao-setup/bisq-BTC_REGTEST_Bob_dao ~/.local/share/bisq
rm -fr dao-setup
