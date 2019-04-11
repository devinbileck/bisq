#!/usr/bin/env bash
generate_101_blocks.sh &
bitcoin-qt -datadir=. #-printtoconsole 
