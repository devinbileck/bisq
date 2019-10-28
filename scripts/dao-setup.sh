#!/usr/bin/env bash
# This script will extract and overwrite necessary files for running Bitcoin core and Bisq in regtest mode.
# Prior to running this script, ensure Bitcoin core and any regtest instances of Bisq are not running.
# Requirements:
#  - unzip must be installed (e.g. sudo apt install unzip)

if [ -d /tmp/dao-setup ]; then
    echo "Removing existing /tmp/dao-setup"
    rm -fr /tmp/dao-setup
fi
echo "Extracting dao-setup.zip to /tmp"
unzip dao-setup.zip -d /tmp

if [ ! -d ~/.local/share/Bitcoin ]; then
    echo "Creating ~/.local/share/Bitcoin"
    mkdir -p ~/.local/share/Bitcoin
elif [ -d ~/.local/share/Bitcoin/regtest ]; then
    echo "Removing existing regtest folder ~/.local/share/Bitcoin/regtest"
    rm -fr ~/.local/share/Bitcoin/regtest
fi
echo "Moving /tmp/dao-setup/Bitcoin-regtest/regtest to ~/.local/share/Bitcoin/regtest"
mv /tmp/dao-setup/Bitcoin-regtest/regtest ~/.local/share/Bitcoin/regtest

if [ ! -f ~/.local/share/Bitcoin/bitcoin.conf ]; then
    echo "Moving /tmp/dao-setup/Bitcoin-regtest/bitcoin.conf to ~/.local/share/Bitcoin"
    mv /tmp/dao-setup/Bitcoin-regtest/bitcoin.conf ~/.local/share/Bitcoin
    echo "Updating ~/.local/share/Bitcoin/bitcoin.conf"
    sed -i -e 's/blocknotify/#blocknotify/g' ~/.local/share/Bitcoin/bitcoin.conf
    echo "blocknotify=bash ~/.local/share/Bitcoin/blocknotify %s" >> ~/.local/share/Bitcoin/bitcoin.conf
fi

if [ ! -f ~/.local/share/Bitcoin/blocknotify ]; then
    echo "Moving /tmp/dao-setup/Bitcoin-regtest/blocknotify to ~/.local/share/Bitcoin"
    mv /tmp/dao-setup/Bitcoin-regtest/blocknotify ~/.local/share/Bitcoin
fi

echo "Removing existing regtest application data directories (~/.local/share/bisq-BTC_REGTEST_*)"
rm -fr ~/.local/share/bisq-BTC_REGTEST_*

echo "Moving /tmp/dao-setup/bisq-BTC_REGTEST_Alice_dao to ~/.local/share/bisq-BTC_REGTEST_Alice_dao"
mv /tmp/dao-setup/bisq-BTC_REGTEST_Alice_dao ~/.local/share/bisq-BTC_REGTEST_Alice_dao

echo "Moving /tmp/dao-setup/bisq-BTC_REGTEST_Bob_dao to ~/.local/share/bisq-BTC_REGTEST_Bob_dao"
mv /tmp/dao-setup/bisq-BTC_REGTEST_Bob_dao ~/.local/share/bisq-BTC_REGTEST_Bob_dao

echo "Removing /tmp/dao-setup"
rm -fr /tmp/dao-setup
