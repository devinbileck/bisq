#!/usr/bin/env bash

VERSION=0.17.1
export RPC_PASSWORD=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 16 | head -n 1)

cd ~/
wget https://bitcoin.org/bin/bitcoin-core-$VERSION/bitcoin-$VERSION-x86_64-linux-gnu.tar.gz
tar -xvf bitcoin-$VERSION-x86_64-linux-gnu.tar.gz
rm bitcoin-$VERSION-x86_64-linux-gnu.tar.gz
sudo cp -f bitcoin-$VERSION/bin/* /usr/local/bin
rm -fr bitcoin-$VERSION

if [ ! -d ~/.bitcoin ]; then
    mkdir ~/.bitcoin
fi

cat >~/.bitcoin/bitcoin.conf <<EOL
regtest=1
rpcport=18443
server=1
txindex=1
rpcuser=bitcoin
rpcpassword=$RPC_PASSWORD
blocknotify=bash ~/.bitcoin/blocknotify %s
EOL

cat >~/.bitcoin/blocknotify <<EOL
#!/usr/bin/env bash
echo $1 | nc -w 1 127.0.0.1 5120
EOL

chmod +x ~/.bitcoin/blocknotify
